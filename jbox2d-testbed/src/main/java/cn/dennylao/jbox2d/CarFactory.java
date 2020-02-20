package cn.dennylao.jbox2d;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.WheelJointDef;

public class CarFactory {
    private CarFactory() {
    }

    public static Body create(World world) {
        float length = 3.0f;
        float height = 1.0f;
        float wheelRadius = 0.4f;
        float hz = 3.0f;
        float zeta = 0.6f;
        float speed = 50.0f;

        final PolygonShape chassis = new PolygonShape();
        Vec2 vertices[] = new Vec2[8];
        vertices[0] = new Vec2(-1.5f, -0.5f);
        vertices[1] = new Vec2(1.5f, -0.5f);
        vertices[2] = new Vec2(1.5f, 0.0f);
        vertices[3] = new Vec2(0.0f, 0.9f);
        vertices[4] = new Vec2(-1.15f, 0.9f);
        vertices[5] = new Vec2(-1.5f, 0.2f);
        chassis.set(vertices, 6);

        final CircleShape circle = new CircleShape();
        circle.m_radius = wheelRadius;

        //make car body
        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(0.0f, 1.0f);
        final Body body = world.createBody(bd);
        body.createFixture(chassis, 1.0f);

        //create car wheel
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = 1.0f;
        fd.friction = 0.9f;

        bd.position.set(-1.0f, 0.35f);
        final Body wheel = world.createBody(bd);
        wheel.createFixture(fd);

        //assemble
        final WheelJointDef jd = new WheelJointDef();
        final Vec2 axis = new Vec2(0.0f, 1.0f);

        jd.initialize(body, wheel, wheel.getPosition(), axis);
        jd.motorSpeed = 0.0f;
        jd.maxMotorTorque = 20.0f;
        jd.enableMotor = true;
        jd.frequencyHz = hz;
        jd.dampingRatio = zeta;
        world.createJoint(jd);

        return body;
    }
}
