package com.bifidoteam.scacchise.view;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bifidoteam.util.ColorShader;
import com.bifidoteam.util.MeshInfo;
import com.bifidoteam.util.Vector3;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

public class MyGLRenderer implements Renderer{

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    
    private GraphicsElement testElement;
    
    public MyGLRenderer(){

    }

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        MeshInfo rectInfo = new MeshInfo();
        Vector<Vector3> test = new Vector<Vector3>();
        test.add(new Vector3(-1f, -1f, 0f));
        test.add(new Vector3(1f, 1f, 0f));
        test.add(new Vector3(-1f, 1f, 0f));
        test.add(new Vector3(1f, -1f, 0f));
        rectInfo.setVertex(test);

        Vector<Vector3> colorTest = new Vector<Vector3>();
        colorTest.add(new Vector3(0f, 0f, 1f));
        colorTest.add(new Vector3(0f, 0f, 1f));
        colorTest.add(new Vector3(1f, 0f, 0f));
        colorTest.add(new Vector3(1f, 0f, 0f));
        rectInfo.setVertexColors(colorTest);
        
        rectInfo.setDrawOrder(new 
        					short[]{0, 1, 2, 
        							0, 3, 1});
        
        testElement = new  GraphicsElement(rectInfo, new ColorShader()); // è possibile provare anche il SimpleShader
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		 GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // *** CALUCLATE PROJECTION MATRIX ***
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3f, 7); // 3 - 7 from the camera position
        
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        
        GLES20.glEnable(GLES20.GL_DEPTH_TEST); // <-- Enable ZBuffer calculation
        
        // *** CALUCLATE VIEW MATRIX *** Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 3f, 3f, 3f, 0f, 0f, 0f, 0f, 0f, 1.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // **** ROTATION ****

        float[] scratch = new float[16];

        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0f, 1.0f);
        
        
        // *** END ***
        
        // #1) Draw Cube
        
        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        
        testElement.setGlobalMatrix(scratch);
        
        testElement.draw();
        
        GLES20.glDisable(GLES20.GL_DEPTH_TEST); // <-- Disable ZBuffer calculation
	}

}
