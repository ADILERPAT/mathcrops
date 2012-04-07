package org.math.crops;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Background {
	
	private final int height = 50;
	private final int width = 70;
	
	public Background(int tex, GL10 g, Context con) {
		gl = g;
		plane = new Quad(width, height);
		texture = TextureLoader.loadTexture(gl, con, tex);
	}
	
	public void draw() {
		gl.glPushMatrix();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glTranslatef(0.0f, 0.0f, -100f);
		plane.draw(gl);
		gl.glPopMatrix();
	}
	
	private Quad plane;
	private int texture;
	private GL10 gl;
}
