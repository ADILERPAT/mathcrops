package org.math.crops;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;
import android.opengl.GLU;
import android.util.Log;

public class Unprojector{
	
	private int screenH, screenW;
	private GL10 gl;
	
	public Unprojector(int width, int height, GL10 g) {
		screenH = height;
		screenW = width;
		gl = g;
	}
	
	/**
	 * Unproject 2D touch screen coordinates to 3D world
	 * 
	 * @param x
	 * @param y
	 * @return result : type float []
	 */
	private MatrixGrabber mMatrixGrabber = new MatrixGrabber();

	public float[] unproject(PointF point) {
		/*
		 * Cache 2D touch location (invert y)
		 */
		float x = point.x;
		float y = (int) (screenH - point.y);

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
}
