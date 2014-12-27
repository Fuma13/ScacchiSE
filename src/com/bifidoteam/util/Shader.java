package com.bifidoteam.util;

import android.opengl.GLES20;
import android.util.Log;

public abstract class Shader {

	protected static final int SIZE_OF_FLOAT=4;
	
	protected String vertexShaderPrograms;
	protected String fragmentShaderPrograms;
	
	public abstract void draw(ShaderArguments args);

	

	// ***************************************************************************************
	// ************************************** LOADING METHODS ********************************
	// ***************************************************************************************
	
	
	public int loadVertexShader(){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, vertexShaderPrograms);
	    GLES20.glCompileShader(shader);
	    
	    System.out.println("**** Compiling Vertex Shader Log: " + GLES20.glGetShaderInfoLog(shader) );
	    
	    return shader;
	}
	
	public int loadFragmentShader(){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, fragmentShaderPrograms);
	    GLES20.glCompileShader(shader);
	    
	    System.out.println("**** Compiling Fragment Shader Log: " + GLES20.glGetShaderInfoLog(shader) );
	    
	    return shader;
	}
	

	

	// ***************************************************************************************
	// ************************************** SUPPORT METHODS ********************************
	// ***************************************************************************************

    private final String TAG = "Shader";
    protected void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
	
}
