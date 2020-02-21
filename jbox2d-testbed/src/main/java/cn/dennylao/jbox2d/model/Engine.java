package cn.dennylao.jbox2d.model;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;

public class Engine {
    private World world;
    private Body body;
    private WheelJoint wheelJoint;

    public Engine(Builder builder) {
        this.world = builder.world;
        this.body = builder.body;

        final Vec2 bodyPosition = this.body.getPosition();
        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(bodyPosition.x - WIDTH / 2 + builder.radius, 0.5f);
        final Body wheel = this.world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        final CircleShape circle = new CircleShape();
        circle.m_radius = builder.radius;
        fd.shape = circle;
        fd.density = 1.2f;
        fd.friction = 0.9f;
        wheel.createFixture(fd);

        final WheelJointDef jd = new WheelJointDef();
        final Vec2 axis = new Vec2(0.0f, 1.0f);
        jd.initialize(body, wheel, wheel.getPosition(), axis);
        jd.motorSpeed = 0.0f;
        jd.maxMotorTorque = builder.torque;
        jd.enableMotor = true;
        jd.frequencyHz = builder.frequencyHz;
        jd.dampingRatio = builder.dampingRatio;

        this.wheelJoint = (WheelJoint) this.world.createJoint(jd);
    }

    public void setMotorSpeed(float speed) {
        this.wheelJoint.setMotorSpeed(speed);
    }

    public void enableMotor(boolean flag) {
        this.wheelJoint.enableMotor(flag);
    }

    public static class Builder {
        private World world;
        private Body body;
        private float radius = 0.5f;
        private float speed= 50.f;
        private float frequencyHz = 4.0f;
        private float dampingRatio = 0.7f;
        private float torque = 20.0f;

        public Builder(){
        }
        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder setFrequencyHz(float frequencyHz) {
            this.frequencyHz = frequencyHz;
            return this;
        }

        public Engine build(World world, Body body) {
            this.world = world;
            this.body = body;
            return new Engine(this);
        }
    }
}
