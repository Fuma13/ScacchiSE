package com.bifidoteam.util;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ShaderArguments {

	private FloatBuffer vertexCoords;
	private FloatBuffer normalsCoords;
	
	private int numOfCoords = 3;
	private int numOfColors = 3;

	private FloatBuffer uvCoords;
	
	private ShortBuffer drawOrder;
	
	private int GLProgram;
	private FloatBuffer colorBuffer;
	
	private int textureId;
	
	private float[] mvpMatrix;
	
//	public ShaderArguments(int GLProgramId, FloatBuffer vertex, FloatBuffer normals, FloatBuffer uv, FloatBuffer order, float[] filterColor, int texture) {
//		GLProgram = GLProgramId;
//		vertexCoords= vertex;
//		normalsCoords = normals;
//		uvCoords = uv;
//		drawOrder = order;
//		color = filterColor;
//		textureId = texture;
//	}
	
	public int getTextureId() {
		return textureId;
	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public FloatBuffer getVertexCoords() {
		return vertexCoords;
	}
	public void setVertexCoords(FloatBuffer vertexCoords) {
		this.vertexCoords = vertexCoords;
	}
	public FloatBuffer getNormalsCoords() {
		return normalsCoords;
	}
	public void setNormalsCoords(FloatBuffer normalsCoords) {
		this.normalsCoords = normalsCoords;
	}
	public FloatBuffer getUvCoords() {
		return uvCoords;
	}
	public void setUvCoords(FloatBuffer uvCoords) {
		this.uvCoords = uvCoords;
	}
	public ShortBuffer getDrawOrder() {
		return drawOrder;
	}
	public void setDrawOrder(ShortBuffer drawOrder2) {
		this.drawOrder = drawOrder2;
	}
	public int getGLProgram() {
		return GLProgram;
	}
	public void setGLProgram(int gLProgram) {
		GLProgram = gLProgram;
	}
	public float[] getMvpMatrix() {
		return mvpMatrix;
	}

	public void setMvpMatrix(float[] mvpMatrix) {
		this.mvpMatrix = mvpMatrix;
	}
	
	public int getNumOfCoords() {
		return numOfCoords;
	}

	public void setNumOfCoords(int numOfCoords) {
		this.numOfCoords = numOfCoords;
	}

	public int getNumOfColors() {
		return numOfColors;
	}

	public void setNumOfColors(int numOfColors) {
		this.numOfColors = numOfColors;
	}

	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	public void setColorBuffer(FloatBuffer colorBuffer) {
		this.colorBuffer = colorBuffer;
	}

}
