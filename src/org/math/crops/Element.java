/**
 * This is a class used to instantiate any game element
 * 
 * 	Note: One must use getPosition, unproject it,
 * 		  and pass it on to the picked() method.
 */
package org.math.crops;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PointF;

public class Element {

	private Quad element;
	private int texture;
	private GL10 gl;
	private final float height;
	private final float width;
	private PointF position;
	private PointF offset;
	private Unprojector unproj;
	private boolean firstTimeInitialized;
	private float depth;
	private boolean picked;
	public String name;

	public Element(int sw, int sh, float w, float h, float x, float y, float z,
			int tex, String n, GL10 g, Context con) {
		this.gl = g;
		this.height = h;
		this.width = w;
		this.position = new PointF(x, y);
		this.offset = new PointF(0, 0);
		this.depth = z;
		this.element = new Quad(width, height);
		this.texture = TextureLoader.loadTexture(gl, con, tex);
		this.name = n;
		this.unproj = new Unprojector(sw, sh, g);
		this.firstTimeInitialized = true;
	}

	public void draw() {
		gl.glPushMatrix();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
		if (firstTimeInitialized) {
			// Set principal location only once
			float p[] = unproj.unproject(position);
			// offset position with init location
			offset.x += p[0];
			offset.y += p[1];
			firstTimeInitialized = false;
		}
		// Translate to given pos
		gl.glTranslatef(offset.x, offset.y, depth);
		// Draw the element
		element.draw(gl);
		gl.glPopMatrix();
	}

	public void updatePosition(PointF move) {
		// Update stored position
		position.set(move);
		// Log.d("position", "[" + position.x + ", " + position.y + "]");
	}

	/**
	 * Returns whether this game element was picked
	 * 
	 * @param touch
	 *            - touch event
	 * @param pos
	 *            - Must be unprojected position for it to work
	 * 
	 * @return if it is picked, it returns the depth of the object
	 * 
	 */
	public boolean isPicked(PointF touch) {
		float p[] = unproj.unproject(position);
		PointF pos = new PointF(p[0], p[1]);
		if (touch.x > pos.x - width && touch.x < pos.x + width
				&& touch.y > pos.y - height && touch.y < pos.y + height) {
			return true;
		} else
			return false;
	}

	/**
	 * Getters/Setters
	 */
	public void setOffset(PointF p) {
		offset.set(p);
	}

	public void setPicked(boolean p) {
		picked = p;
	}

	public float getDepth() {
		return depth;
	}

	public boolean picked() {
		return picked;
	}
}
