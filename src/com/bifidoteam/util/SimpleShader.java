package com.bifidoteam.util;

import android.opengl.GLES20;

public class SimpleShader extends Shader {

	public SimpleShader() {
	
			vertexShaderPrograms = 
					"uniform mat4 uMVPMatrix;" +
				    "attribute vec3 vPosition;" +
		            " " +
		            "void main() {" +
			            // the matrix must be included as a modifier of gl_Position
			            // Note that the uMVPMatrix factor *must be first* in order
			            // for the matrix multiplication product to be correct.
			            "  gl_Position = uMVPMatrix*vec4(vPosition,1.0);" +
			        "}";
			
			fragmentShaderPrograms = 
					"precision mediump float;" +
					" " +
				    "void main() {" +
				    "  gl_FragColor = vec4(0.0,0.0,0.0,1.0);" +
				    "}";
	}

	@Override
	public void setArguments(ShaderArguments args) {
		
		int GLProgram = args.getGLProgram();
		
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(GLProgram);

        // ***********************************************
        // ------> SET VSHADER ARGUMENTS: vPosition <-----
        // ***********************************************
        
        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(GLProgram, "vPosition");
        checkGlError("vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, args.getNumOfCoords(),
                                     GLES20.GL_FLOAT, false,
                                     args.getNumOfCoords(), args.getVertexCoords());

        // ***********************************************
        // -----> SET VSHADER ARGUMENTS: uMVPMatrix <-----
        // ***********************************************
        
        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(GLProgram, "uMVPMatrix");
        checkGlError("uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, args.getMvpMatrix(), 0);

        // ***********************************************
        // ********************* END *********************
        // ***********************************************
        
        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(mPositionHandle);
        
	}
}
