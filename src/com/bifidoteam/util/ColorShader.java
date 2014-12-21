package com.bifidoteam.util;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class ColorShader extends Shader {
	
	public ColorShader() {
		
		vertexShaderPrograms = 
				"uniform mat4 uMVPMatrix;" +
			    "attribute vec3 vPosition;" +
	            "attribute vec3 vColor;" +
	            " " +
	            "varying vec4 vColorOut;" + // <-- VARYING keyword: pass argument between vertex and fragment shader!
	            "void main() {" +
		            " vColorOut = vec4(vColor,1.0);"+
		            // the matrix must be included as a modifier of gl_Position
		            // Note that the uMVPMatrix factor *must be first* in order
		            // for the matrix multiplication product to be correct.
		            "  gl_Position = uMVPMatrix*vec4(vPosition,1.0);" +
	            "}";
		
		fragmentShaderPrograms = 
				"precision mediump float;" +
			    "varying vec4 vColorOut;" +
				" " +
			    "void main() {" +
			    	" gl_FragColor = vColorOut;" +
			    "}";
	}
	
	public void setArguments(int GLProgram, float[] mvpMatrix, FloatBuffer vertexCoords, int coordsNumbers, FloatBuffer colorBuffer, int colorNumbers){
		
		// Add program to OpenGL ES environment
        GLES20.glUseProgram(GLProgram);

        // ***********************************************
        // ------> SET VSHADER ARGUMENTS: vPosition <-----
        // ***********************************************
        
        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(GLProgram, "vPosition");


        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, coordsNumbers,
                                     GLES20.GL_FLOAT, false,
                                     coordsNumbers, vertexCoords);
        

        // ***********************************************
        // -------> SET VSHADER ARGUMENTS: vColor <-------
        // ***********************************************
        
        int mColorHandle = GLES20.glGetAttribLocation(GLProgram, "vColor");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mColorHandle, colorNumbers,
                                     GLES20.GL_FLOAT, false,
                                     colorNumbers, colorBuffer);


        // ***********************************************
        // -----> SET VSHADER ARGUMENTS: uMVPMatrix <-----
        // ***********************************************
        
        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(GLProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // ***********************************************
        // ********************* END *********************
        // ***********************************************

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
	}
}
