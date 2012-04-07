package org.math.crops;

/**
 * A simple textured quad object
 * 
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Quad {
	// Our vertices.
	private float vertices[] = { 
			-140.0f, -100.0f, 0.0f, // 0, Bottom Left
			-140.0f, 100.0f, 0.0f, // 1, Top Left
			140.0f, 100.0f, 0.0f, // 2, Top Right
			140.0f, -100.0f, 0.0f, // 3, Bottom Right
	};

	// The order we like to connect them.
	private short[] indices = { 0, 1, 2, 0, 2, 3 };

	// tex coords
	private float[] uvs = { 0, 0, 0, 1, 1, 1, 1, 0 };

	// Our vertex buffer.
	private FloatBuffer vertexBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer;

	// Texture buffer
	private FloatBuffer texBuffer;

	public Quad(float w, float h) {
		
		Resize(w, h);
		
		
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);

		// Texture buffer
		ByteBuffer tbb = ByteBuffer.allocateDirect(4 * 2 * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(uvs);
		texBuffer.position(0);

	}

	private void Resize(float w, float h) {
		vertices[0] = -w;
		vertices[1] = -h;
		vertices[2] = 0.0f;
		
		vertices[3] = -w;
		vertices[4] = h;
		vertices[5] = 0.0f;
		
		vertices[6] = w;
		vertices[7] = h;
		vertices[8] = 0.0f;
		
		vertices[9] = w;
		vertices[10] = -h;
		vertices[11] = 0.0f;
	}

	public void draw(GL10 gl) {
		gl.glDisable(GL10.GL_DEPTH_TEST);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		// Enable texturing
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);

		// Disable the vertex buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}