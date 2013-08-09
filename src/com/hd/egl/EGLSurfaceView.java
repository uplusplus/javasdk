/**
 * 
 */
package com.hd.egl;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author uplusplus
 * 
 */

class EGLSurfaceView extends GLSurfaceView {

	EglRenderer mRenderer;
	Timer t;
	TimerTask task;
	
	private void init(){
		this.buildDrawingCache(true);
		mRenderer = new EglRenderer();
		setRenderer(mRenderer);
		setRenderMode(RENDERMODE_WHEN_DIRTY);

		t = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				EGLSurfaceView.this.requestRender();
			}

		};
	}

	public EGLSurfaceView(Context context) {
		super(context);
		init();
	}

	public EGLSurfaceView(Context context, AttributeSet attrs) {
		super(context,attrs);
		init();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		t.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	@Override
	public void onPause(){
		t.cancel();
		super.onPause();
	}
	
	static {
		System.loadLibrary("egl");
	}
}