package com.bifidoteam.scacchise.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.bifidoteam.util.MeshInfo;
import com.bifidoteam.util.Shader;
import com.bifidoteam.util.ShaderArguments;

public class GraphicsElement {

//	private FloatBuffer vertexCoords;
//	private FloatBuffer normalsCoords;
//	private FloatBuffer uvCoords;
//	
	private ShortBuffer drawOrder;
	private int drawOrderLength;
	
	int GLProgram;
//	float[] color = new float[4];
//	
//	int IdTexture;
	
	Shader selectedShader;
	ShaderArguments argumentsWrapped;
	
	public GraphicsElement(MeshInfo mesh, Shader shader) {
		
		argumentsWrapped = new ShaderArguments();
		
		// ** 1) creation of the buffers
		argumentsWrapped.setVertexCoords( fromArrayToFloatBuffer(mesh.vertexToFloatArray()) );
		argumentsWrapped.setNormalsCoords( fromArrayToFloatBuffer(mesh.normalToFloatArray()) );
        argumentsWrapped.setUvCoords( fromArrayToFloatBuffer(mesh.uvToFloatArray()) );
        
        drawOrderLength = mesh.getDrawOrder().length;
        drawOrder = fromArrayToShortBuffer(mesh.getDrawOrder());
        argumentsWrapped.setDrawOrder(drawOrder);
         
         
        selectedShader = shader;
        
        // ** 2) compile shaders to OpenGL program
        int vertexShader = shader.loadVertexShader();
        int fragmentShader = shader.loadFragmentShader();

        // ** 2) attach shaders to OpenGL program
        GLProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(GLProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(GLProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(GLProgram);                  // create OpenGL program executables
        
        argumentsWrapped.setGLProgram(GLProgram);
	}
	
	public void setGlobalMatrix(float[] actualGlobalPositionMatrix){
		argumentsWrapped.setMvpMatrix(actualGlobalPositionMatrix);
	}
	
	public void draw(){
		
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(GLProgram); // TODO: c'è già nella funzione interna, lo tolgo?
        
        selectedShader.setArguments(argumentsWrapped);
        
        // Draw the triangle
        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrderLength,
                GLES20.GL_UNSIGNED_SHORT, drawOrder);

        GLES20.glDisableVertexAttribArray(0); //TODO: trovare dove metterlo!!
	}
	
	
	// *********************************************************************************************
	// *********************************** SUPPORT FUNCTIONS ***************************************
	// *********************************************************************************************
	
	private FloatBuffer fromArrayToFloatBuffer(float[] array){
		
		if(array == null)
			return null;
		
		FloatBuffer toReturn;
		
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		array.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        toReturn = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        toReturn.put(array);
        // set the buffer to read the first coordinate
        toReturn.position(0);
        
        return toReturn;
	}
	
private ShortBuffer fromArrayToShortBuffer(short[] array){
		
		if(array == null)
			return null;
		
		ShortBuffer toReturn;
		
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 2 bytes per short)
        		array.length * 2);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        toReturn = bb.asShortBuffer();
        // add the coordinates to the FloatBuffer
        toReturn.put(array);
        // set the buffer to read the first coordinate
        toReturn.position(0);
        
        return toReturn;
	}
	
	// TODO: Implementare il draw!!!! (che chiamera' il setArgument del proprio shader
}
