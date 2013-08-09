/*
 * namesocket.cpp
 *
 *  Created on: Aug 8, 2013
 *      Author: uplusplus
 */

#include "namesocket.h"
#include "base.h"
#include "ems_android_egl.h"

/*
 * Create a UNIX-domain socket address in the Linux "abstract namespace".
 *
 * The socket code doesn't require null termination on the filename, but
 * we do it anyway so string functions work.
 */
int makeAddr(const char* name, struct sockaddr_un* pAddr, socklen_t* pSockLen)
		{
	int nameLen = strlen(name);
	if (nameLen >= (int) sizeof(pAddr->sun_path) - 1) /* too long? */
		return -1;
	pAddr->sun_path[0] = '\0'; /* abstract namespace */
	strcpy(pAddr->sun_path + 1, name);
	pAddr->sun_family = AF_LOCAL;
	*pSockLen = 1 + nameLen + offsetof(struct sockaddr_un, sun_path);
	return 0;
}

static void* thread_read(void *arg) {
	name_socket *reader = (name_socket*) arg;
	reader->open_socket();
	while (reader->state) {
		reader->read_data();
		reader->swap();
	}
}

static int count = 0;
int name_socket::open_socket() {
	char tmp[19] = { 0 }, *buf_last;

	if (clientSock != -1) {
		close(clientSock);
		clientSock = -1;
	}

	clientSock = accept(lfd, NULL, NULL);
	if (clientSock < 0) {
		perror("server accept");
		return 0;
	}

	retry:
	while (!(nread = read(clientSock, tmp, 18)))
		sleep(1);

	if (18 != nread && strncmp(tmp, "ABCD", 4)) {
		DMSG((STDOUT, "failed to receive!\n"));
		return 0;
	}

	sscanf(tmp, "ABCD%05u%05uEFGH", &width, &height);
	DMSG((STDOUT, "open_fifo:width is %u, height is %u\n", width, height));
	if (!width || !height)
		goto retry;

	row_width = width;
	width = (width + 3) & ~3;

	buf_last = buf;
	buf_swap = 0;
	buf = (char*) malloc(width * height * 2);
	DMSG((STDOUT, "open_fifo:pixel address %p\n", (void*) buf));
	egl_SetImage((unsigned char*) buf, width, height);
	if (buf_last)
		free(buf_last);
	buf_size = width * height;
	count = 0;
	return 1;
}

static void printfv(char* msg, int len) {
	char c, l = '0';
	int count = 0;

	while (len--) {
		c = *msg++;
		if (c == l) {
			count++;
		} else {
			if (count > 0)
				DMSG((STDOUT, " %c[%d]", l == 0 ? '0' : l, count));
			l = c;
			count = 1;
		}
	}
	if (count > 0)
		DMSG((STDOUT, " %c[%d]", l == 0 ? '0' : l, count));
	DMSG((STDOUT, "\n"));
}

int name_socket::read_data() {
	unsigned int readed = 0;
	char *cursor = private_buf();

	while (readed < height) {
		nread = read(clientSock, cursor, row_width);
		if (nread < 0) {
			if (errno == EINTR || errno == EWOULDBLOCK || errno == EAGAIN) {
				DMSG((STDOUT, "there is no data, try again!\n"));
				sleep(1);
				continue;
			} else {
				DMSG((STDOUT, "name socket is closed! 0  \n"));
				open_socket();
				readed = 0;
				cursor = buf;
				continue;
			}
		} else if (nread == 0) {
			DMSG((STDOUT, "name socket is closed! 0  \n"));
			open_socket();
			readed = 0;
			cursor = buf;
			continue;
		} else if (nread != row_width) {
			while (nread < row_width) {
				nread += read(clientSock, cursor + nread, row_width - nread);
			}
		}
		cursor += width;
		readed++;
		//DMSG((STDOUT,"."));
	}
	DMSG((STDOUT, "%d", count++));
}

name_socket::name_socket()
{
	struct sockaddr_un sockAddr;
	socklen_t sockLen;
	int result = 1;

	state = 0;
	lfd = clientSock = -1;

	if (makeAddr(SOCKET_NAME, &sockAddr, &sockLen) < 0)
		return;
	lfd = socket(AF_LOCAL, SOCK_STREAM, PF_UNIX);
	if (lfd < 0) {
		perror("client socket()");
		return;
	}
	if (bind(lfd, (const struct sockaddr*) &sockAddr, sockLen) < 0) {
		perror("server bind()");
		return;
	}
	if (listen(lfd, 5) < 0) {
		perror("server listen()");
		return;
	}
	state = 1;

	if (pthread_create(&read_thread, NULL, thread_read, this) != 0) {
		perror("read thread creation failed");
		return;
	}

	buf = 0;
	DMSG((STDOUT,"prepare to read fifo!\n"));
}

name_socket::~name_socket()
{

}

char* name_socket::private_buf() {
	return buf + (width * height);
}

void name_socket::swap() {
	memcpy(buf, private_buf(), buf_size);
}

