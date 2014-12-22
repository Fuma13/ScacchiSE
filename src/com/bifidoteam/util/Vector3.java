package com.bifidoteam.util;

public class Vector3 {

	float x = 0.0f, y = 0.0f, z = 0.0f;

	public Vector3() {
	}

	public Vector3(float xIn, float yIn, float zIn) {
		x = xIn;
		y = yIn;
		z = zIn;
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

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void setValue(float xIn, float yIn, float zIn) {
		x = xIn;
		y = yIn;
		z = zIn;
	}

	public void setValue(Vector3 vectorIn) {
		x = vectorIn.x;
		y = vectorIn.y;
		z = vectorIn.z;
	}

	/** Sets <tt>Vec3 this</tt> to zero */
	public void setZero() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	/**
	 * Add and accumulate. <tt>Vec3 a</tt> is added to <tt>Vec3 this</tt>
	 */
	public void add(Vector3 a) {
		x = x + a.x;
		y = y + a.y;
		z = z + a.z;
	}

	/**
	 * Subtract and accumulate. <tt>Vec3 a</tt> is subtracted from
	 * <tt>Vec3 this</tt>
	 */
	public void sub(Vector3 a) {
		x = x - a.x;
		y = y - a.y;
		z = z - a.z;
	}

	public static float dot(Vector3 a, Vector3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public void cross(Vector3 a, Vector3 b) {
		x = a.y * b.z - a.z * b.y;
		y = a.z * b.x - a.x * b.z;
		z = a.x * b.y - a.y * b.x;
	}

	public static Vector3 normal(Vector3 a, Vector3 b) {
		Vector3 toReturn = new Vector3();

		toReturn.x = a.y * b.z - a.z * b.y;
		toReturn.y = a.z * b.x - a.x * b.z;
		toReturn.z = a.x * b.y - a.y * b.x;

		Vector3.normalise(toReturn);

		return toReturn;
	}

	/**
	 * Calculate unit normal to triangle given by position vectors a, b and c in
	 * the sense that a->b->c goes right-handed around n. For example, if a, b
	 * and c are clockwise and flat in the plane of view, n is away from the
	 * viewer.
	 * <p>
	 * Alternatively, this is (b-a) cross (c-a) normalised.
	 */
	public static Vector3 normal(Vector3 point1, Vector3 point2, Vector3 point3) {
		Vector3 toReturn = new Vector3();

		toReturn.x = (point2.y - point1.y) * (point3.z - point1.z)
				- (point2.z - point1.z) * (point3.y - point1.y);
		toReturn.y = (point2.z - point1.z) * (point3.x - point1.x)
				- (point2.x - point1.x) * (point3.z - point1.z);
		toReturn.z = (point2.x - point1.x) * (point3.y - point1.y)
				- (point2.y - point1.y) * (point3.x - point1.x);

		Vector3.normalise(toReturn);

		return toReturn;
	}

	public static void normalise(Vector3 a) {
		float length = a.length();
		a.x = a.x / length;
		a.y = a.y / length;
		a.z = a.z / length;
	}

	/** Returns length of vector */
	public float length() {
		return ((float) Math.sqrt(x * x + y * y + z * z));
	}

	/** Returns length of vector squared */
	public float lengthSqd() {
		return (x * x + y * y + z * z);
	}

	/** Returns length of vector (b-a) squared */
	public static float distSqd(Vector3 a, Vector3 b) {
		return ((float) (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z)
				* (a.z - b.z));
	}

	public float[] toArray(){
		float[] toReturn = new float[3];
		
		toReturn[0]= x;
		toReturn[1]= y;
		toReturn[2]= z;
		
		return toReturn;
	}
}
