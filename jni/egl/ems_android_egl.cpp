/*
 * egl draw
 * author: uplusplus
 * date: 2010/8/3
 * */
#include <GLES/gl.h>
#include <malloc.h>
#include "base.h"
#include "hello_mesh.h"
#include "ems_android_egl.h"

#define FIXED(value) ((long)(value * 65536))

static int isContextChanged = 0;
static int surface_width = 320, surface_height = 480;
static int image_width = 240, image_height = 320;
static int scaled_width = 256, scaled_height = 512;

static unsigned char *image_pixel = NULL;

GLfixed vertices[8] = { 0 };
const static GLfixed texture[8] = { 0x00000, 0x00000, 0x10000, 0x00000, 0x00000,
		0x10000, 0x10000, 0x10000 };
const static GLfixed colors_ligth[16] = { 0x10000,0x10000,0x10000,0x10000,
												0x10000,0x10000,0x10000,0x10000,
												0x10000,0x10000,0x10000,0x10000,
												0x10000,0x10000,0x10000,0x10000};

const static GLfixed colors_rgb[16] = { FIXED(0.5),0x00000,0x00000,0x10000,
												0x00000,0x00000,FIXED(0.5),0x10000,
												0x00000,FIXED(0.5),0x00000,0x10000,
												0x10000,0x10000,0x10000,0x10000};

static void ResetContext();



void draw_hello(){
	DMSG((STDOUT,"draw hello\n"));
	glPushMatrix();
	glClear(GL_COLOR_BUFFER_BIT);
    glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	glDisable(GL_TEXTURE_2D);
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState (GL_COLOR_ARRAY);

    glViewport(0, 0, surface_width, surface_height);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
	glOrthox(0, FIXED(surface_width), 0, FIXED(surface_height),  -0x10000,
			0x10000);
	glTranslatef(0,surface_height/2.0,0.0f);
	glVertexPointer (2, GL_FIXED, 0, helloVertex);
	glColorPointer (4, GL_FIXED, 0, helloColor);
	glDrawArrays (GL_TRIANGLES ,0 ,156);

	glPopMatrix();
}

void egl_Draw() {
	if (!image_pixel){
		draw_hello();
		return;
	}
	if (isContextChanged) {
		ResetContext();
		return;
	}
	//DMSG((STDOUT,"draw egl"));
	glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, image_width, image_height,
			GL_LUMINANCE, GL_UNSIGNED_BYTE, (GLvoid *) image_pixel);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void egl_Update() {
	//glFinish();
	//Andr_JNI_UpdateScreen();
}

void egl_Resize(int w, int h) {
	DMSG((STDOUT,"view surface_width:%d surface_height:%d.\n", w, h));
	surface_width = w;
	surface_height = h;
	ResetContext();
}

void egl_Init() {
	DMSG((STDOUT,"GL_VENDOR:\t%s\n", glGetString(GL_VENDOR)));
	DMSG((STDOUT,"GL_RENDERER:\t%s\n", glGetString(GL_RENDERER)));
	DMSG((STDOUT,"GL_VERSION:\t%s\n", glGetString(GL_VERSION)));
	DMSG((STDOUT,"GL_EXTENSIONS:\t%s\n", glGetString(GL_EXTENSIONS)));

	glEnableClientState(GL_VERTEX_ARRAY);
	glEnableClientState(GL_TEXTURE_COORD_ARRAY);
	glEnable(GL_TEXTURE_2D);

	ResetContext();
}

void egl_Uninit() {

}

//注意：所有的opengles相关的函数必须位于java层的opengl上下文下，从另外的线程调用这些函数将没有效果
//这个函数是从我们的线程调用的，没有环境，所以这里只能标记，等到画窗体时再执行opengl函数
void egl_SetImage(unsigned char* pixel, int w, int h) //这里是实际上的初始化的地方，没有设置image_pixel之前，一切都没有作用。
		{
	DMSG((STDOUT,"image width:%d  _height:%d.\n", w, h));
	image_width = w;
	image_height = h;
	image_pixel = pixel;
	isContextChanged = 1;
	//ResetContext();
}

void egl_Swap(unsigned char* pixel)
{
	image_pixel = pixel;
}

static void ResetContext() {
	unsigned char *data;
	isContextChanged = 0;
	scaled_width = 2;
	while (scaled_width < image_width)
		scaled_width <<= 1;
	scaled_height = 2;
	while (scaled_height < image_height)
		scaled_height <<= 1;

	DMSG((STDOUT, "scaled_width %d scaled_height %d\n", scaled_width, scaled_height));
	data = (unsigned char *) malloc(
			scaled_width * scaled_height * sizeof(char));

	draw_hello();

	glViewport(0, 0, surface_width, surface_height);
	glClearColorx(FIXED(0.1f), FIXED(0.2f), FIXED(0.3f), 0x10000);
	glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrthox(0, FIXED(surface_width), FIXED(surface_height), 0, 0x10000,
			-0x10000);
	glTexParameterx(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameterx(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameterx(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameterx(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

	glBindTexture(GL_TEXTURE_2D, 0);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, scaled_width, scaled_height, 0,
			GL_LUMINANCE, GL_UNSIGNED_BYTE, (GLvoid *) data);

	vertices[2] = vertices[6] =
			FIXED( surface_width * scaled_width / image_width );
	vertices[5] = vertices[7] =
			FIXED( surface_height * scaled_height / image_height);

	DMSG((STDOUT,"surface_width=%d,surface_height=%d,image_width=%d,image_height=%d\r\n",surface_width,surface_height,image_width,image_height));
	glVertexPointer(2, GL_FIXED, 0, vertices);
	glTexCoordPointer(2, GL_FIXED, 0, texture);
	glColorPointer (4, GL_FIXED, 0, colors_rgb);

	free(data);
}
