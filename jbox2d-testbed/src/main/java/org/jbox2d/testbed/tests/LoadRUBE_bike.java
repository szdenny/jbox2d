
package org.jbox2d.testbed.tests;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import org.json.JSONException;

import org.iforce2d.Jb2dJson;

/**
 * @author Chris Campbell - www.iforce2d.net
 */
public class LoadRUBE_bike extends TestbedTest {

	Body wheelBody = null;
	int driveState = 0;//1 = left, 2 = right
	
  @Override
  public boolean isSaveLoadEnabled() {
    return true;
  }
  
	/**
	 * @see TestbedTest#initTest(boolean)
	 */
	@Override
	public void initTest(boolean argDeserialized) {
	  if(argDeserialized){
	    return;
	  }
 
  
	  //load world from RUBE file
	  Jb2dJson json = new Jb2dJson();
	  StringBuilder errorMsg = new StringBuilder();
	  World world = json.readFromFile("bike.json", errorMsg);
	  if ( null != world ) {
		  m_world = world;
			
		  //replace testbed stuff
		  groundBody = m_world.createBody(new BodyDef());
		  m_world.setDestructionListener(destructionListener);
		  m_world.setContactListener(this);
		  m_world.setDebugDraw(model.getDebugDraw());
		  
		  wheelBody = json.getBodyByName("bikewheel");
	  }
	  else 
		  System.out.println(errorMsg);

	}
	
	void updateDriveState() {
		if ( null == wheelBody )
			return;
		
		float maxSpeed = 40;
		if ( driveState == 1 )
			wheelBody.setAngularVelocity(maxSpeed);
		else if ( driveState == 2 )
			wheelBody.setAngularVelocity(-maxSpeed);
		else
			wheelBody.setAngularVelocity(0);
	}
	
	/**
	 * @see TestbedTest#keyPressed(char, int)
	 */
	@Override
	public void keyPressed(char key, int argKeyCode) {		
		switch (key) {
			case 'j' :
				driveState |= 1;
				break;
			
			case 'k' :
				driveState |= 2;
				break;
		}
		updateDriveState();
	}
	
	/**
	 * @see TestbedTest#keyReleased(char, int)
	 */
	// Does not work correctly on Linux
	// http://brunez.net63.net/tutorials/keypressfix/keypressfix.php
	@Override
	public void keyReleased(char key, int argKeyCode) {
		switch (key) {
		case 'j' :
			driveState &= ~1;
			break;
		
		case 'k' :
			driveState &= ~2;
			break;
		}
		updateDriveState();
	}
	
	/**
	 * @see TestbedTest#step(TestbedSettings)
	 */
	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		addTextLine("Press j/k to drive");

		getCamera().setCamera(wheelBody.getPosition());
	}

	/**
	 * @see TestbedTest#getTestName()
	 */
	@Override
	public String getTestName() {
		return "Load RUBE - bike";
	}
	
}
