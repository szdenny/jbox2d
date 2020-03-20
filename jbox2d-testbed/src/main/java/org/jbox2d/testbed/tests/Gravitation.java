package org.jbox2d.testbed.tests;

import org.iforce2d.Jb2dJson;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

public class Gravitation extends TestbedTest {
    private boolean isBallInSensor = false;
    Body ball;
    Body sensor;
    Body planet;

    @Override
    public void initTest(boolean deserialized) {
        //load world from RUBE file
        Jb2dJson jb2dJson = new Jb2dJson();
        StringBuilder errorMsg = new StringBuilder();
        World world = jb2dJson.readFromFile("gravitation.json", errorMsg);
        if (world != null) {
            m_world = world;

            //replace testbed stuff
            groundBody = m_world.createBody(new BodyDef());
            m_world.setDestructionListener(destructionListener);
            m_world.setContactListener(this);
            m_world.setDebugDraw(model.getDebugDraw());
            ball = jb2dJson.getBodyByName("ball");
            ball.setLinearDamping(0.2f);
            sensor = jb2dJson.getBodyByName("sensor");
            planet = jb2dJson.getBodyByName("planet");
//            jb2dJson.getBodyByName("ground").setUserData("ground");
//            jb2dJson.getBodyByName("planet").setUserData("planet");
//            jb2dJson.getBodyByName("sensor").setUserData("sensor");
//            jb2dJson.getBodyByName("ball").setUserData("ball");
        } else {
            System.out.println(errorMsg);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
//        System.out.println("preSolve");
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        Body[] checkResult = checkWithTarget(bodyA, bodyB, ball, sensor);
        if (checkResult != null) {
            isBallInSensor = true;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        Body[] checkResult = checkWithTarget(bodyA, bodyB, ball, sensor);
        if (checkResult != null) {
            isBallInSensor = false;
        }
    }

    private Body[] checkWithTarget(Body checkA, Body checkB, Body targetA, Body targetB) {
        if (checkA == targetA && checkB == targetB) {
            return new Body[]{checkA, checkB};
        } else if (checkA == targetB && checkB == targetA) {
            return new Body[]{checkB, checkA};
        }
        return null;
    }

    @Override
    public void step(TestbedSettings settings) {
        super.step(settings);

//        m_world.clearForces();
        if (isBallInSensor) {
            Vec2 force = planet.getPosition().sub(ball.getPosition());
            force.normalize();
            force = force.mul(20 * ball.getMass());
            ball.applyForce(force, ball.getPosition());
            ball.setLinearDamping(0.0f);
        }
        Vec2 p1 = ball.getPosition();
        Vec2 p2 = planet.getPosition();
        float r = p1.sub(p2).length();
        addTextLine("r = " + r);
        addTextLine("ball's velocity : " + ball.getLinearVelocity().length() / r);
        addTextLine("ball's angular velocity:" + ball.getAngularVelocity());
    }

    @Override
    public void keyPressed(char keyChar, int keyCode) {
        super.keyPressed(keyChar, keyCode);

        switch (keyChar) {
            case 'a': {
                Vec2 force = new Vec2(-1, 0);
                ball.applyLinearImpulse(force, ball.getPosition(), true);
                break;
            }
            case 's': {
                Vec2 force = new Vec2(0, -1);
                ball.applyLinearImpulse(force, ball.getPosition(), true);
                break;
            }
            case 'd': {
                Vec2 force = new Vec2(1, 0);
                ball.applyLinearImpulse(force, ball.getPosition(), true);
                break;
            }
            case 'w': {
                Vec2 force = new Vec2(0, 1);
                ball.applyLinearImpulse(force, ball.getPosition(), true);
                break;
            }
        }
    }

    @Override
    public String getTestName() {
        return "Gravitation";
    }
}
