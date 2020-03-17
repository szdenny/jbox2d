package org.jbox2d.testbed.tests;

import org.iforce2d.Jb2dJson;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.testbed.framework.TestbedTest;

public class OneWayWall extends TestbedTest {
    private Jb2dJson jb2dJson;

    @Override
    public void initTest(boolean deserialized) {
        //load world from RUBE file
        jb2dJson = new Jb2dJson();
        StringBuilder errorMsg = new StringBuilder();
        World world = jb2dJson.readFromFile("one_way_wall.json", errorMsg);
        if (world != null) {
            m_world = world;

            //replace testbed stuff
            groundBody = m_world.createBody(new BodyDef());
            m_world.setDestructionListener(destructionListener);
            m_world.setContactListener(this);
            m_world.setDebugDraw(model.getDebugDraw());
            jb2dJson.getBodyByName("ground").setUserData("ground");
            jb2dJson.getBodyByName("bar").setUserData("bar");
            jb2dJson.getBodyByName("ball").setUserData("ball");
        } else {
            System.out.println(errorMsg);
        }
    }

    @Override
    public String getTestName() {
        return "OneWayWall";
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Body[] checkResult = sortByOneBody(contact, "ball");

        if (checkResult == null) {
            return;
        }

        Body another = checkResult[1];
        isContactWithPlatform(contact, another, true);
    }

    private Vec2 getContactPoint(Contact contact) {
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);

        return worldManifold.points[0];
    }

    private boolean isContactWithPlatform(Contact contact, Body platform, boolean isBeginContact) {
        if (!"bar".equalsIgnoreCase((String) platform.getUserData())) {
            return false;
        }

        Vec2 contactPoint = getContactPoint(contact);
        contactPoint = platform.getLocalPoint(contactPoint);

        System.out.println(contactPoint);
        if (isBeginContact) {
            if (contactPoint.y <= 0) {
                contact.setEnabled(true);
                return true;
            } else {
                contact.setEnabled(false);
            }
        } else {
//            contact.setEnabled(true);
        }

        return false;
    }

    private Body[] sortByOneBody(Contact contact, String targetA) {
        Body tempBodyA = contact.getFixtureA().getBody();
        Body tempBodyB = contact.getFixtureB().getBody();

        String userDataA = (String) tempBodyA.getUserData();
        String userDataB = (String) tempBodyB.getUserData();

        if (userDataA == targetA || userDataA == "bomb") {
            return new Body[]{tempBodyA, tempBodyB};
        } else if (userDataB == targetA || userDataB == "bomb") {
            return new Body[]{tempBodyB, tempBodyA};
        }

        return null;
    }
}
