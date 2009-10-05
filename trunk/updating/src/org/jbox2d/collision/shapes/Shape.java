/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 * 
 * JBox2D homepage: http://jbox2d.sourceforge.net/
 * Box2D homepage: http://www.box2d.org
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package org.jbox2d.collision.shapes;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Segment;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.structs.collision.MassData;
import org.jbox2d.structs.collision.RayCastInput;
import org.jbox2d.structs.collision.RayCastOutput;
import org.jbox2d.structs.collision.ShapeType;

/**
 * A shape is used for collision detection. You can create a shape however you like.
 * Shapes used for simulation in World are created automatically when a Fixture
 * is created.
 */
public abstract class Shape {
	/** Unique id for shape for sorting (C++ version uses memory address) */
	public int uid; 
	/**
	 * Used to generate uids - not initialized on applet reload,
	 * but that's okay since these just have to be unique.
	 */
	static private int uidcount = 0;

	public ShapeType m_type;
	public float m_radius;

	public Shape() {
		uid = uidcount++; //Java version only (C++ version sorts by memory location)
		m_type = ShapeType.UNKNOWN_SHAPE;
	}
	
	/**
	 * Get the type of this shape. You can use this to down cast to the concrete shape.
	 * @return the shape type.
	 */
	public ShapeType getType() {
		return m_type;
	}

	/**
	 * Test a point for containment in this shape. This only works for convex shapes.
	 * @param xf the shape world transform.
	 * @param p a point in world coordinates.
	 */
	public abstract boolean testPoint( final Transform xf, final Vec2 p);

	/**
	 * Cast a ray against this shape.
	 * @param output the ray-cast results.
	 * @param input the ray-cast input parameters.
	 * @param transform the transform to be applied to the shape.
	 */
	public abstract void raycast( RayCastOutput output, RayCastInput input, Transform transform);


	/**
	 * Given a transform, compute the associated axis aligned bounding box for this shape.
	 * @param aabb returns the axis aligned box.
	 * @param xf the world transform of the shape.
	 */
	public abstract void computeAABB(final AABB aabb, final Transform xf);

	/**
	 * Compute the mass properties of this shape using its dimensions and density.
	 * The inertia tensor is computed about the local origin, not the centroid.
	 * @param massData returns the mass data for this shape.
	 * @param density the density in kilograms per meter squared.
	 */
	public abstract void computeMass(final MassData massData, final float density);
	
	
	/**
	 * Compute the volume and centroid of this shape intersected with a half plane
	 * @param normal the surface normal
	 * @param offset the surface offset along normal
	 * @param xf the shape transform
	 * @param c returns the centroid
	 * @return the total volume less than offset along normal
	 */
	public abstract float computeSubmergedArea(Vec2 normal,
									  float offset,
									  Transform xf, 
									  Vec2 c);
	
	
	public abstract Shape clone();
}