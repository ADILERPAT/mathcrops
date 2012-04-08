/**
 * @author Federico Commisso
 */
package org.math.crops;

import javax.microedition.khronos.opengles.GL;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView {

	private GameRenderer mRenderer;

	// Touch Input Handlers
	private PointF touchStart;
	private PointF touchMoved;

	/** Constructor to set the handed over context */
	public GameView(Context context, int w, int h) {
		super(context);

		// Init input handlers
		initInputHandlers();

		setFocusable(true);

		// Wrapper set so the renderer can
		// access the gl transformation matrixes.
		setGLWrapper(new GLSurfaceView.GLWrapper() {
			@Override
			public GL wrap(GL gl) {
				return new MatrixTrackingGL(gl);
			}
		});
		

		/*
		 * My workaround so far was to change the app from RGB565 to RGBA8888.
		 * Insert these lines before setting the renderer:
		 * 
		 * Must be aware that this solution will only work on devices that
		 * support RGBA8888.
		 */
		getHolder().setFormat(PixelFormat.RGBA_8888);
		setEGLConfigChooser(8, 8, 8, 8, 0, 0);

		// set the mRenderer member
		mRenderer = new GameRenderer(context, w, h);
		setRenderer(mRenderer);

		// Render the view only when there is a change
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	private void initInputHandlers() {
		touchStart = new PointF();
		touchMoved = new PointF();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			touchMoved = getDeltaMove(e);
			mRenderer.Update(touchMoved);
			
			requestRender();
			return true;

		case MotionEvent.ACTION_DOWN:
			touchStart = getTouchPoint(e);
			float t[] = mRenderer.unproj.unproject(touchStart);
			touchStart.set(t[0], t[1]);
			mRenderer.touchUpdate(touchStart);
			
			requestRender();
			return true;

		case MotionEvent.ACTION_UP:
			touchStart.set(0f, 0f);
			touchMoved.set(0f, 0f);
			mRenderer.touchUpdate(touchStart);
			return true;

		default:
			return super.onTouchEvent(e);
		}
	}

	private PointF getTouchPoint(MotionEvent e) {
		return new PointF(e.getX(), e.getY());
	}

	private PointF getDeltaMove(MotionEvent e) {
		return new PointF(e.getX() - touchStart.x, e.getY() - touchStart.y);
	}
}