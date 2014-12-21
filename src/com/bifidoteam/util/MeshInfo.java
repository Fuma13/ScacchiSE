package com.bifidoteam.util;

import java.util.Vector;

public class MeshInfo {
	Vector<Vector3> vertex;
	Vector<Vector3> normals;
	Vector<Vector2> uvCoords;
	
	float[] drawOrder;

	public static final int NUMBER_OF_COORDS_PER_VERTEX =3;
	public static final int NUMBER_OF_COORDS_PER_NORMAL =3;
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
	
	public float[] getDrawOrder() {
		return drawOrder;
	}

	public void setDrawOrder(float[] drawOrder) {
		this.drawOrder = drawOrder;
	}


	
	// ************************************************************************************
	// ************** FUNCTION FOR CREATE FLOAT ARRAY (PASSED TO OPENGL) ******************
	// ************************************************************************************
	

	public float[] vertexToFloatArray(){
		
		return toFloatArray(NUMBER_OF_COORDS_PER_VERTEX);
	}
	
	public float[] uvToFloatArray(){
		
		return toFloatArray(NUMBER_OF_UV_COORDS);
	}

	public float[] normalToFloatArray(){
		
		return toFloatArray(NUMBER_OF_COORDS_PER_NORMAL);
	}
	
	
	private float[] toFloatArray(int numOfIterationForElement){
		
		int count = vertex.size()*numOfIterationForElement;
		
		float[] toReturn = new float[count];
		
		float[] actualValues;
		
		for(int i=0; i<vertex.size(); i++){
			actualValues = vertex.get(i).toArray();
			
			toReturn[i*numOfIterationForElement] = actualValues[0];
			toReturn[i*numOfIterationForElement + 1] = actualValues[1];
			toReturn[i*numOfIterationForElement + 2] = actualValues[2];
		}
		
		return toReturn;
	}
	
}
