/**
 * 
 */
package com.hd.egl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

/**
 * @author uplusplus
 * 
 */

class EglRenderer implements GLSurfaceView.Renderer {

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		nativeInit();
	}

	public void onSurfaceDestoryed(GL10 gl, EGLConfig config) {
		nativeDone();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		int tmp;
		if (w < h) {
			tmp = w;
			w = h;
			h = tmp;
		}
		nativeResize(w, h);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		nativeRender();
	}

	private static native void nativeInit();

	private static native void nativeResize(int w, int h);

	private static native void nativeRender();

	private static native void nativeDone();

}