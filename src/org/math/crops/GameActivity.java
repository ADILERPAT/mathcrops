package org.math.crops;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	/** The OpenGL View */
	private GLSurfaceView glSurface;
	
	// Screen display info
	private Display display;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// sets the Bundle
		super.onCreate(savedInstanceState);
		
		
		// requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Set main.XML as the layout for this Activity
		setContentView(R.layout.main);
				
		// Here I get the screen info
		display = getWindowManager().getDefaultDisplay();
		
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		glSurface = new GameView(this, display.getWidth(), display.getHeight());
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
}
