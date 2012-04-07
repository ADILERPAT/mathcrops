package org.math.crops;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GLRendererActivity extends Activity {
	/** The OpenGL View */
	private GLSurfaceView glSurface;
	private GlRenderer renderer;
	  
	Display display;
	int width, height;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Set main.XML as the layout for this Activity
				setContentView(R.layout.main);
				
		// here I get the screen info for various calculations
		display = getWindowManager().getDefaultDisplay();
		width = display.getWidth(); // deprecated
		height = display.getHeight(); // deprecated
		
		// Initiate the Open GL view and
        // create an instance with this activity
        glSurface = new GLSurfaceView(this);
        renderer = new GlRenderer(this);
        glSurface.setRenderer(renderer);
		setContentView(glSurface);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		glSurface.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		glSurface.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		finish();
	}
	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    switch (event.getAction()) {
	      case MotionEvent.ACTION_MOVE:
	    	  return super.onTouchEvent(event);
	    	  
	      case MotionEvent.ACTION_DOWN:
	    	  return super.onTouchEvent(event);

	      case MotionEvent.ACTION_UP:
	    	  return super.onTouchEvent(event);
	    	  
	      default:
	        return super.onTouchEvent(event);
	    }
	  }
	
	
}
