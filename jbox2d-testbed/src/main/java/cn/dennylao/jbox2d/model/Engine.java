package cn.dennylao.jbox2d.model;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;

public class Engine {
    private World world;
    private float radius;
    private float speed;
    private float frequencyHz;
    private float dampingRatio;
    private float torque;
    private float friction;
    private float density;
    private WheelJoint wheelJoint;

    public Engine(Builder builder) {
        this.world = builder.world;
        this.radius = builder.radius;
        this.speed = builder.speed;
        this.frequencyHz = builder.frequencyHz;
        this.dampingRatio = builder.dampingRatio;
        this.torque = builder.torque;
        this.density = builder.density;
        this.friction = builder.friction;
    }

    public void assemble(Carframe frame) {
        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(frame.getEngineConnectionPoint());
        final Body wheel = this.world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        final CircleShape circle = new CircleShape();
        circle.m_radius = radius;
        fd.shape = circle;
        fd.density = density;
        fd.friction = friction;
        wheel.createFixture(fd);

        final WheelJointDef jd = new WheelJointDef();
        final Vec2 axis = new Vec2(0.0f, 1.0f);
        jd.initialize(frame.getBody(), wheel, wheel.getPosition(), axis);
        jd.motorSpeed = 0.0f;
        jd.maxMotorTorque = torque;
        jd.enableMotor = true;
        jd.frequencyHz = frequencyHz;
        jd.dampingRatio = dampingRatio;

        this.wheelJoint = (WheelJoint) world.createJoint(jd);
    }

    public void setMotorSpeed(float speed) {
        this.wheelJoint.setMotorSpeed(speed);
    }

    public void enableMotor(boolean flag) {
        this.wheelJoint.enableMotor(flag);
    }

    public static class Builder {
        private World world;
        private float radius = 0.5f;
        private float speed = 50.f;
        private float frequencyHz = 4.0f;
        private float dampingRatio = 0.7f;
        private float torque = 20.0f;
        private float density = 1.2f;
        private float friction = 0.9f;

        public Builder(World world) {
            this.world = world;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder setFrequencyHz(float frequencyHz) {
            this.frequencyHz = frequencyHz;
            return this;
        }

        public Engine build() {
            return new Engine(this);
        }
    }
}
