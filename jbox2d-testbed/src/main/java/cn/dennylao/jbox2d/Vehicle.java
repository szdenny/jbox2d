package cn.dennylao.jbox2d;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;

public class Vehicle {
    private float hz = 4.0f;
    private float zeta = 0.7f;
    private float speed = 50.0f;
    private World world;
    private Body body;
    private WheelJoint engine;
    private Body wheel[] = new Body[2];

    public Vehicle(Builder builder) {
        this.speed = builder.speed;
        this.world = builder.world;
        this.hz = builder.hz;
        this.zeta = builder.zeta;
    }

    public void forward() {
        engine.setMotorSpeed(speed);
    }

    public void backward() {
        engine.setMotorSpeed(speed);
    }

    public void start() {
        engine.enableMotor(true);
    }

    public void stop() {
        engine.enableMotor(false);
    }

    public Vec2 getPosition() {
        return this.body.getPosition();
    }

    private void makeBody() {
        PolygonShape chassis = new PolygonShape();
        Vec2 vertices[] = new Vec2[8];
        vertices[0] = new Vec2(-1.5f, -0.5f);
        vertices[1] = new Vec2(1.5f, -0.5f);
        vertices[2] = new Vec2(1.5f, 0.0f);
        vertices[3] = new Vec2(0.0f, 0.9f);
        vertices[4] = new Vec2(-1.15f, 0.9f);
        vertices[5] = new Vec2(-1.5f, 0.2f);
        chassis.set(vertices, 6);

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(0.0f, 1.0f);
        body = world.createBody(bd);
        body.createFixture(chassis, 1.0f);
    }

    private void createWheel() {
        CircleShape circle = new CircleShape();
        circle.m_radius = 0.4f;
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = 1.0f;
        fd.friction = 0.9f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(-1.0f, 0.35f);
        wheel[0] = world.createBody(bd);
        wheel[0].createFixture(fd);

        bd.position.set(1.0f, 0.4f);
        wheel[1] = world.createBody(bd);
        wheel[1].createFixture(fd);

        WheelJointDef jd = new WheelJointDef();
        Vec2 axis = new Vec2(0.0f, 1.0f);

        jd.initialize(body, wheel[0], wheel[0].getPosition(), axis);
        jd.motorSpeed = 0.0f;
        jd.maxMotorTorque = 20.0f;
        jd.enableMotor = true;
        jd.frequencyHz = hz;
        jd.dampingRatio = zeta;
        engine = (WheelJoint) world.createJoint(jd);

        jd.initialize(body, wheel[1], wheel[1].getPosition(), axis);
        jd.enableMotor = false;
        jd.frequencyHz = hz;
        jd.dampingRatio = zeta;
    }

    public static class Builder {
        private float hz = 4.0f;
        private float zeta = 0.7f;
        private float speed = 50.0f;
        private World world;

        public Builder setHz(float hz) {
            this.hz = hz;
            return this;
        }

        public Builder setZeta(float zeta) {
            this.zeta = zeta;
            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Vehicle build(World world) {
            this.world = world;
            return new Vehicle(this);
        }
    }
}
