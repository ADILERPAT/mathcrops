package org.math.crops;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

public class Element {

	private final float height;
	private final float width;
	private PointF position;

	public Element(float f, float h, float x, float y, int tex, GL10 g, Context con) {
		gl = g;
		height = h;
		width = f;
		position = new PointF(x, y);
		element = new Quad(width, height);
		texture = TextureLoader.loadTexture(gl, con, tex);
	}

	public void drawAt(float x, float y, float z) {
		gl.glPushMatrix();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glTranslatef(x, y, z);
		element.draw(gl);
		gl.glPopMatrix();
	}
	
	public void updatePosition( PointF move ) {
		// Update stored position
		position.set(move);
		Log.d("position", "[" + position.x + ", " + position.y + "]");
	}
	
	public PointF getPosition() {
		return position;
	}

	public boolean picked(PointF touch, PointF pos) {
		if (touch.x > pos.x - width && touch.x < pos.x + width
				&& touch.y > pos.y - height
				&& touch.y < pos.y + height) {
			return true;
		} else
			return false;
	}

	private Quad element;
	private int texture;
	private GL10 gl;
}
