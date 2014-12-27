package com.bifidoteam.util;

import java.util.Vector;

public class MeshInfo {
	Vector<Vector3> vertex;
	Vector<Vector3> normals;
	Vector<Vector3> vertexColors;

	Vector<Vector2> uvCoords;
	
	short[] drawOrder;

	public static final int NUMBER_OF_COORDS_PER_VERTEX =3;
	public static final int NUMBER_OF_COORDS_PER_NORMAL =3;
	public static final int NUMBER_OF_COLOR_PER_VERTEX =3;
	public static final int NUMBER_OF_UV_COORDS =2;
	
	public MeshInfo(){
		vertex = new Vector<Vector3>();
		normals = new Vector<Vector3>();
		uvCoords = new Vector<Vector2>();
	}
	
	// ************************************************************************************
	// **************************** GETTER AND SETTER *************************************
	// ************************************************************************************
	
	public Vector<Vector3> getVertex() {
		return vertex;
	}
	
	public void setVertex(Vector<Vector3> vertex) {
		this.vertex = vertex;
	}
	
	public Vector<Vector3> getNormals() {
		return normals;
	}
	
	public void setNormals(Vector<Vector3> normals) {
		this.normals = normals;
	}
	
	public Vector<Vector2> getUvCoords() {
		return uvCoords;
	}
	
	public void setUvCoords(Vector<Vector2> uvCoords) {
		this.uvCoords = uvCoords;
	}
	
	public short[] getDrawOrder() {
		return drawOrder;
	}

	public void setDrawOrder(short[] drawOrder) {
		this.drawOrder = drawOrder;
	}
	
	public Vector<Vector3> getVertexColors() {
		return vertexColors;
	}

	public void setVertexColors(Vector<Vector3> vertexColor) {
		this.vertexColors = vertexColor;
	}

	
	// ************************************************************************************
	// ************** FUNCTION FOR CREATE FLOAT ARRAY (PASSED TO OPENGL) ******************
	// ************************************************************************************
	

	public float[] vertexToFloatArray(){
		
		return toFloatArray(vertex, NUMBER_OF_COORDS_PER_VERTEX);
	}
	
	public float[] uvToFloatArray(){
		
		return toFloatArray(uvCoords);
	}

	public float[] normalToFloatArray(){
		
		return toFloatArray(normals, NUMBER_OF_COORDS_PER_NORMAL);
	}
	
	public float[] colorToFloatArray(){

		return toFloatArray(vertexColors, NUMBER_OF_COLOR_PER_VERTEX);
	}
	
	private float[] toFloatArray(Vector<Vector3> actualElements, int numOfIterationForElement){
		
		if(actualElements == null)
			return null;
		
		int count = actualElements.size()*numOfIterationForElement;
		
		float[] toReturn = new float[count];
		
		float[] actualValues;
		
		for(int i=0; i<actualElements.size(); i++){
			actualValues = actualElements.get(i).toArray();
			
			for(int j=0; j< numOfIterationForElement; j++)
				toReturn[i*numOfIterationForElement + j] = actualValues[j];
		}
		
		return toReturn;
	}
	
	private float[] toFloatArray(Vector<Vector2> actualElements){
		
		if(actualElements == null)
			return null;
		
		int count = vertex.size()*NUMBER_OF_UV_COORDS;
		
		float[] toReturn = new float[count];
		
		float[] actualValues;
		
		for(int i=0; i<vertex.size(); i++){
			actualValues = vertex.get(i).toArray();
			
			for(int j=0; j< NUMBER_OF_UV_COORDS; j++)
				toReturn[i*NUMBER_OF_UV_COORDS + j] = actualValues[j];
		}
		
		return toReturn;
	}
	
}
