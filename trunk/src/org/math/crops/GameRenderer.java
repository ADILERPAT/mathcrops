package org.math.crops;

import java.io.IOException;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class GameRenderer implements GLSurfaceView.Renderer {

	/*
	 * Render settings declarations
	 */
	public Context context;
	public int screenW, screenH;
	private GL10 gl;

	/*
	 * Assets
	 */
	private Background background;
	private Element avatar;

	/*
	 * Fonts
	 */
	private TextFont ArialBig;

	/*
	 * Game states
	 */
	public PointF droidPosOffset;

	/*
	 * Renderer Constructor: needs current context and device screen parameters
	 * from activity/GLSurfaceView which is being called from.
	 */
	public GameRenderer(Context ctxt, int w, int h) {
		context = ctxt;
		screenW = w;
		screenH = h;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		/*
		 * Disable things that aren't needed
		 */
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_CULL_FACE);

		/*
		 * Enable Texture Mapping
		 */
		gl.glEnable(GL10.GL_TEXTURE_2D);

		/*
		 * Enable Smooth Shading
		 */
		gl.glShadeModel(GL10.GL_SMOOTH);

		/*
		 * Depth buffer set up
		 */
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		/*
		 * The Type Of Depth Testing To Do
		 */
		gl.glDepthFunc(GL10.GL_LEQUAL);

		/*
		 * Really Nice Perspective Calculations
		 */
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		/*
		 * Initialize game variables
		 */
		initGame(gl);
	}

	private void initGame(GL10 gl) {
		this.gl = gl;

		background = new Background(R.drawable.cropfield, gl, context);

		avatar = new Element(0.2f, 0.2f, screenW / 2, screenH / 2,
				R.drawable.droid_avatar, gl, context);
		droidPosOffset = new PointF(0, 0);

		// Load font file from Assets
		ArialBig = new TextFont(this.context, gl);
		try {
			ArialBig.LoadFontAlt("ArialBig.bff", gl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		/*
		 * Clear Screen and Depth Buffer
		 */
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		/*
		 * Reset the Modelview Matrix
		 */
		gl.glLoadIdentity();

		/*
		 * Disable depth test for static objects
		 */
		gl.glDisable(GL10.GL_DEPTH_TEST);

		/*
		 * Drawing background
		 */
		background.draw();

		/*
		 * Drawing text
		 */
		drawText(gl, "Math.Crops Text", 400, 200, 1.2f);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		/*
		 * Drawing droid avatar
		 */
		avatar.drawAt(droidPosOffset.x, droidPosOffset.y, -1.0f);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		/*
		 * Reset the curent viewprt
		 */
		gl.glViewport(0, 0, width, height);

		/*
		 * Select and Reset The Projection Matrix
		 */
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		/*
		 * Calculate The Aspect Ratio Of The Window
		 */
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				100.0f);

		/*
		 * Select and Reset The Modelview Matrix
		 */
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	private void drawText(GL10 gl, String text, int x, int y, float scale) {
		gl.glPushMatrix();
		ArialBig.SetPolyColor(0f, 0f, 0f);
		ArialBig.SetScale(scale);
		ArialBig.PrintAt(gl, text, x, y);
		gl.glPopMatrix();
	}

	/**
	 * Unproject 2D touch screen coordinates to 3D world
	 * 
	 * @param x
	 * @param y
	 * @return result : type float []
	 */
	private MatrixGrabber mMatrixGrabber = new MatrixGrabber();

	public float[] unproject(PointF touch) {
		/*
		 * Cache 2D touch location (invert y)
		 */
		float x = touch.x;
		float y = (int) (screenH - touch.y);

		mMatrixGrabber.getCurrentState(gl);
		int[] view = { 0, 0, screenW, screenH };
		float[] pos = new float[4];
		float[] result = null;

		int retval = GLU.gluUnProject(x, y, 0, mMatrixGrabber.mModelView, 0,
				mMatrixGrabber.mProjection, 0, view, 0, pos, 0);

		if (retval != GL10.GL_TRUE) {

			Log.e("unproject", GLU.gluErrorString(retval));

		} else {

			result = new float[3];
			result[0] = pos[0] / pos[3];
			result[1] = pos[1] / pos[3];
			result[2] = pos[2] / pos[3];
			result = pos;

		}
		return result;
	}

	public void Update(PointF touchMoved) {
		float m[] = unproject(touchMoved);

		if (avatarPicked) {
			droidPosOffset.set(new PointF(m[0], m[1]));
			avatar.updatePosition(touchMoved);
		}
	}

	public void touchUpdate(PointF touchStart) {
		/*
		 * Check for each element if it is being picked
		 */
		float pos[] = unproject(avatar.getPosition());
		avatarPicked = avatar.picked(touchStart, new PointF(pos[0], pos[1]));
	}

	private boolean avatarPicked;
}
