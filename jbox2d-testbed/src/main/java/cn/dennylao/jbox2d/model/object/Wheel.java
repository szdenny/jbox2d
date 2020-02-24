package cn.dennylao.jbox2d.model.object;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.WheelJointDef;

public class Wheel {
    private World world;
    private Body body;
    private float radius;
    private float density;
    private float friction;

    public Wheel(Builder builder) {
        this.world = builder.world;
        this.radius = builder.radius;
        this.density = builder.density;
        this.friction = builder.friction;
    }

    public void assemble(Chassis frame, Vec2 position) {
        final CircleShape circle = new CircleShape();
        circle.m_radius = radius;

        //create car wheel
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = density;
        fd.friction = friction;

        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(position);
        body = this.world.createBody(bd);
        body.createFixture(fd);

        final WheelJointDef jd = new WheelJointDef();
        final Vec2 axis = new Vec2(0.0f, 1.0f);
        jd.initialize(frame.getBody(), body, body.getPosition(), axis);
        jd.motorSpeed = 0.0f;
        jd.enableMotor = false;
        jd.frequencyHz = frame.getFrequencyHz();
        jd.dampingRatio = frame.getDampingRatio();

        world.createJoint(jd);
    }

    public static class Builder {
        private World world;
        private float radius = 0.4f;
        private float density = 1.0f;
        private float friction = 0.9f;

        public Builder(World world) {
            this.world = world;
        }

        public Builder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder setDensity(float density) {
            this.density = density;
            return this;
        }

        public Builder setFriction(float friction) {
            this.friction = friction;
            return this;
        }

        public Wheel build() {
            return new Wheel(this);
        }
    }
}
