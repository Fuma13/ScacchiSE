package com.bifidoteam.util;

import android.opengl.GLES20;

public abstract class Shader {

	protected String vertexShaderPrograms;
	
	protected String fragmentShaderPrograms;
	
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
	
}
