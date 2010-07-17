/**
 * Created at 2:21:03 PM Jul 17, 2010
 */
package org.jbox2d.testbed.framework;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.callbacks.DestructionListener;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Collision;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.structs.collision.Manifold;
import org.jbox2d.structs.collision.PointState;
import org.jbox2d.structs.collision.WorldManifold;

/**
 * @author Daniel Murphy
 */
public abstract class TestbedTest implements ContactListener{
	public static final int MAX_CONTACT_POINTS = 2048;

	// keep these static so we don't have to recreate them every time
	public final static ContactPoint[] points = new ContactPoint[MAX_CONTACT_POINTS];
	static {
		for(int i=0; i<MAX_CONTACT_POINTS; i++){
			points[i] = new ContactPoint();
		}
	}
	
	public Body groundBody;
	private AABB worldAABB;
	private int pointCount;
	private final DestructionListener destructionListener;
	public final DebugDraw debugDraw;
	public World world;
	private Body bomb;
	public Joint mouseJoint;
	private final Vec2 bombSpawnPoint = new Vec2();
	private boolean bombSpawning = false;
	private final Vec2 mouseWorld = new Vec2();
	private int stepCount;
	private int textLine;
	
	public TestbedTest(DebugDraw argDebugDraw){
		debugDraw = argDebugDraw;
		destructionListener = new DestructionListener() {
			
			public void sayGoodbye(Fixture fixture) {
			}
			
			public void sayGoodbye(Joint joint) {
				if(mouseJoint == joint){
					mouseJoint = null;
				}else{
					
				}
			}
		};
		
		Vec2 gravity = new Vec2(0, -10f);
		world = new World(gravity, true);
		bomb = null;
		textLine = 30;
		mouseJoint = null;
		pointCount = 0;
		
		world.setDestructionListener(destructionListener);
		world.setContactListener(this);
		world.setDebugDraw(debugDraw);
		
		bombSpawning = false;
		
		stepCount = 0;
		
		BodyDef bodyDef = new BodyDef();
		groundBody = world.createBody(bodyDef);
	}
	
	private final Color3f color1 = new Color3f(.3f, .95f, .3f);
	private final Color3f color2 = new Color3f(.3f, .3f, .95f);
	private final Color3f color3 = new Color3f(.9f, .9f, .9f);
	private final Vec2 p1 = new Vec2();
	private final Vec2 p2 = new Vec2();
	
	public void step(TestbedSettings settings){
		float timeStep = settings.hz > 0f ? 1f/settings.hz : 0;
		
		if(settings.pause){
			if(settings.singleStep){
				settings.singleStep = false;
			}else{
				timeStep = 0;
			}
			
			debugDraw.drawString(5, textLine, "****PAUSED****", Color3f.WHITE);
			textLine += 15;
		}
		
		int flags = 0;
		flags += settings.drawShapes			* DebugDraw.e_shapeBit;
		flags += settings.drawJoints			* DebugDraw.e_jointBit;
		flags += settings.drawAABBs			* DebugDraw.e_aabbBit;
		flags += settings.drawPairs			* DebugDraw.e_pairBit;
		flags += settings.drawCOMs				* DebugDraw.e_centerOfMassBit;
		debugDraw.setFlags(flags);
		
		world.setWarmStarting(settings.enableWarmStarting);
		world.setContinuousPhysics(settings.enableContinuous);
		
		pointCount = 0;
		
		world.Step(timeStep, settings.velocityIterations, settings.positionIterations);
		
		world.DrawDebugData();
		
		if(timeStep > 0f){
			++stepCount;
		}
		
		if(settings.drawStats){
			// stats
		}
		
		if(mouseJoint != null){
			// mousejoint drawing
		}
		
		if(bombSpawning){
			debugDraw.drawSegment(bombSpawnPoint, mouseWorld, Color3f.WHITE);
		}
		
		if(settings.drawContactPoints){
			float axisScale = .3f;
			
			for(int i=0; i<pointCount; i++){
				
				ContactPoint point = points[i];
				
				if(point.state == PointState.ADD_STATE){
					debugDraw.drawSolidCircle(point.position, 10f, null, color1);
				}
				else if(point.state == PointState.PERSIST_STATE){
					debugDraw.drawSolidCircle(point.position, 5f, null, color2);
				}
				
				if(settings.drawContactNormals){
					p1.set(point.position);
					p2.set(point.normal).mulLocal(axisScale).addLocal(p1);
					debugDraw.drawSegment(p1, p1, color3);
				}
			}
		}
	}
	
	public void keyboard(char key){
		
	}
	
	public void shiftMouseDown(Vec2 p){
		mouseWorld.set(p);
		
		if(mouseJoint != null){
			return;
		}
		
		spawnBomb(p);
	}
	
	public void mouseUp(Vec2 p){
		if(mouseJoint != null){
			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
		
		if(bombSpawning){
			completeBombSpawn(p);
		}
	}
	
	private final AABB queryAABB = new AABB();
	private final TestQueryCallback callback = new TestQueryCallback();
	
	public void mouseDown(Vec2 p){
		mouseWorld.set(p);
		
		if(mouseJoint != null){
			return;
		}
		
		queryAABB.lowerBound.set(p.x - .001f, p.y - .001f);
		queryAABB.upperBound.set(p.x + .001f, p.y + .001f);
		callback.point.set(p);
		world.queryAABB(callback, queryAABB);
		
		if(callback.fixture != null){
			System.out.println("mouse joint!");
		}
	}
	
	public void mouseMove(Vec2 p){
		mouseWorld.set(p);
		
		if(mouseJoint != null){
			//mouseJoint.setTarget(p);
		}
	}
	
	private final Vec2 p = new Vec2();
	private final Vec2 v = new Vec2();
	
	public void lanchBomb(){
		p.set((float)(Math.random()*30 - 15), 30f);
		v.set(p).mulLocal(-5f);
		launchBomb(p, v);
	}
	
	private final AABB aabb = new AABB();
	
	public void launchBomb(Vec2 position, Vec2 velocity){
		if(bomb != null){
			world.destroyBody(bomb);
			bomb = null;
		}
		// todo optimize this
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(position);
		bd.bullet = true;
		bomb = world.createBody(bd);
		bomb.setLinearVelocity(velocity);
		
		CircleShape circle = new CircleShape();
		circle.m_radius = 0.3f;
		
		FixtureDef fd = new FixtureDef();
		fd.shape = circle;
		fd.density = 20f;
		fd.restitution = 0;
		
		Vec2 minV = new Vec2(position);
		Vec2 maxV = new Vec2(position);
		
		minV.subLocal(new Vec2(.3f,.3f));
		maxV.addLocal(new Vec2(.3f, .3f));
		
		aabb.lowerBound.set(minV);
		aabb.upperBound.set(maxV);
		
		bomb.createFixture(fd);
	}
	
	public void spawnBomb(Vec2 worldPt){
		bombSpawnPoint.set(worldPt);
		bombSpawning = true;
	}
	
	private final Vec2 vel = new Vec2();
	
	public void completeBombSpawn( Vec2 p){
		if(bombSpawning == false){
			return;
		}
		
		float multiplier = 30f;
		vel.set(bombSpawnPoint).subLocal(p);
		vel.mulLocal(multiplier);
		launchBomb(bombSpawnPoint, vel);
		bombSpawning = false;
	}
	
	public void jointDestroyed(Joint joint){
		
	}
	
	/**
	 * @see org.jbox2d.callbacks.ContactListener#beginContact(org.jbox2d.dynamics.contacts.Contact)
	 */
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @see org.jbox2d.callbacks.ContactListener#endContact(org.jbox2d.dynamics.contacts.Contact)
	 */
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @see org.jbox2d.callbacks.ContactListener#postSolve(org.jbox2d.dynamics.contacts.Contact, org.jbox2d.callbacks.ContactImpulse)
	 */
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	private final PointState[] state1 = new PointState[Settings.maxManifoldPoints];
	private final PointState[] state2 = new PointState[Settings.maxManifoldPoints];
	private final WorldManifold worldManifold = new WorldManifold();
	/**
	 * @see org.jbox2d.callbacks.ContactListener#preSolve(org.jbox2d.dynamics.contacts.Contact, org.jbox2d.structs.collision.Manifold)
	 */
	public void preSolve(Contact contact, Manifold oldManifold) {
		Manifold manifold = contact.getManifold();
		
		if(manifold.pointCount == 0){
			return;
		}
		
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		Collision.getPointStates(state1, state2, oldManifold, manifold);
		
		contact.getWorldManifold(worldManifold);
		
		for(int i=0; i<manifold.pointCount && pointCount < MAX_CONTACT_POINTS; i++){
			ContactPoint cp = points[pointCount];
			cp.fixtureA = fixtureA;
			cp.fixtureB = fixtureB;
			cp.position.set(worldManifold.points[i]);
			cp.normal.set(worldManifold.normal);
			cp.state = state2[i];
			++pointCount;
		}
	}
}

class TestQueryCallback implements QueryCallback{

	public final Vec2 point;
	public Fixture fixture;
	
	public TestQueryCallback(){
		point = new Vec2();
		fixture = null;
	}
	
	/**
	 * @see org.jbox2d.callbacks.QueryCallback#reportFixture(org.jbox2d.dynamics.Fixture)
	 */
	public boolean reportFixture(Fixture argFixture) {
		Body body = argFixture.getBody();
		if(body.getType() == BodyType.DYNAMIC){
			boolean inside = argFixture.testPoint(point);
			if(inside){
				fixture = argFixture;
				
				return false;
			}
		}
		
		return true;
	}
	
}