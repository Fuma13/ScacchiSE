package com.bifidoteam.util;

public class Vector2 {

	float x = 0.0f, y = 0.0f;

	public Vector2() {
	}

	public Vector2(float xIn, float yIn) {
		x = xIn;
		y = yIn;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setValue(float xIn, float yIn) {
		x = xIn;
		y = yIn;
	}

	public void setValue(Vector2 vectorIn) {
		x = vectorIn.x;
		y = vectorIn.y;
	}

	/** Sets <tt>Vec3 this</tt> to zero */
	public void setZero() {
		x = 0.0f;
		y = 0.0f;
	}

	/**
	 * Add and accumulate. <tt>Vec3 a</tt> is added to <tt>Vec3 this</tt>
	 */
	public void add(Vector2 a) {
		x = x + a.x;
		y = y + a.y;
	}

	/**
	 * Subtract and accumulate. <tt>Vec3 a</tt> is subtracted from
	 * <tt>Vec3 this</tt>
	 */
	public void sub(Vector2 a) {
		x = x - a.x;
		y = y - a.y;
	}

	public static float dot(Vector2 a, Vector2 b) {
		return a.x * b.x + a.y * b.y;
	}

	public static void normalise(Vector2 a) {
		float length = a.length();
		a.x = a.x / length;
		a.y = a.y / length;
	}

	/** Returns length of vector */
	public float length() {
		return ((float) Math.sqrt(x * x + y * y));
	}

	/** Returns length of vector squared */
	public float lengthSqd() {
		return (x * x + y * y);
	}

	/** Returns length of vector (b-a) squared */
	public static float distSqd(Vector2 a, Vector2 b) {
		return ((float) (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	public float[] toArray(){
		float[] toReturn = new float[2];
		
		toReturn[0]= x;
		toReturn[1]= y;
		
		return toReturn;
	}

}
