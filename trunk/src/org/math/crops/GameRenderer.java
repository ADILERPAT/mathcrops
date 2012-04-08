package org.math.crops;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class GameRenderer implements GLSurfaceView.Renderer {

	/** Render settings declarations */
	public Context context;
	public int screenW, screenH;
	private GL10 gl;
	public Unprojector unproj;

	/** Assets */
	private Background background;

	/* Needs to be updated for every new element */
	private int NUM_OF_ELEMENTS = 2;
	Vector<Element> gameElements;

	private Element avatar;
	private Element e;

	/** Fonts */
	private TextFont ArialBig;

	/**
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

	private void initGame(GL10 g) {
		this.gl = g;
		unproj = new Unprojector(screenW, screenH, gl);
		gameElements = new Vector<Element>();

		/*
		 * Initializing a background
		 */
		background = new Background(R.drawable.cropfield, gl, context);

		/*
		 * Initializing Game Elements
		 */
		avatar = new Element(screenW, screenH, 0.2f, 0.2f, screenW / 2,
				screenH / 2, -1.0f, R.drawable.droid_avatar, "Avatar", gl,
				context);
		// Add element to list of gameElements
		gameElements.add(avatar);

		e = new Element(screenW, screenH, 0.1f, 0.1f, 200f, 200f, -1.0f,
				R.drawable.android, "Droid", gl, context);
		// Add element to list of gameElements
		gameElements.add(e);

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
		 * Drawing game elements
		 */
		drawGameElements();
	}

	private void drawGameElements() {
		int i;
		for (i = 0; i < NUM_OF_ELEMENTS; ++i) {
			gameElements.elementAt(i).draw();
		}
	}

	private void drawText(GL10 gl, String text, int x, int y, float scale) {
		gl.glPushMatrix();
		ArialBig.SetPolyColor(0f, 0f, 0f);
		ArialBig.SetScale(scale);
		ArialBig.PrintAt(gl, text, x, y);
		gl.glPopMatrix();
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

	/**
	 * Update game states based on user input
	 * 
	 * @param touchMoved
	 */
	public void Update(PointF touchMoved) {
		float m[] = unproj.unproject(touchMoved);
		int i;

		for (i = 0; i < NUM_OF_ELEMENTS; ++i) {
			if (gameElements.elementAt(i).picked()) {
				gameElements.elementAt(i).setOffset((new PointF(m[0], m[1])));
				gameElements.elementAt(i).updatePosition(touchMoved);
				//gameElements.elementAt(i).setPicked(false);
			}
		}
	}

	/**
	 * Check for each element if it is being picked
	 * 
	 * @param touhStart
	 */
	public void touchUpdate(PointF touchStart) {
		int i;
		for (i = 0; i < NUM_OF_ELEMENTS; ++i) {
			gameElements.elementAt(i).setPicked(
					gameElements.elementAt(i).isPicked(touchStart));
		}
	}
}
