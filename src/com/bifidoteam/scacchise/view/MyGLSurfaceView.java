package com.bifidoteam.scacchise.view;

import android.content.Context;
import android.opengl.GLSurfaceView;


public class MyGLSurfaceView extends GLSurfaceView {
	
	private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // void android.opengl.GLSurfaceView.setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize)
        // Nota: se non imposto questa funzione lo stencil buffer non funzia!
        setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        
        setRenderer(mRenderer);
		
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}