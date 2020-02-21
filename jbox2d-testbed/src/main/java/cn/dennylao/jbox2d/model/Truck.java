package cn.dennylao.jbox2d.model;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;

public final class Truck extends Vehicle {
    public Truck(Builder builder) {
        super(builder);
    }

    public static class Builder extends Vehicle.Builder {
        private final float wheelRadius = 0.4f;
        private final float WIDTH = 3.0f;
        private final float HEIGHT = 1.0f;
        private World world;
        private float density = 1.0f;

        public Builder(World world){
            this.world = world;
        }

        protected Carframe makeFrame(Vec2 position, float density) {
            final PolygonShape chassis = new PolygonShape();
            Vec2 vertices[] = new Vec2[8];
            vertices[0] = new Vec2(-1.0f / 2 * WIDTH, 0.5f * HEIGHT);
            vertices[1] = new Vec2(-1.0f / 2 * WIDTH, -0.5f * HEIGHT);
            vertices[2] = new Vec2(1.0f / 2 * WIDTH, -0.5f * HEIGHT);
            vertices[3] = new Vec2(1.0f / 2 * WIDTH, 0.5f * HEIGHT);
            chassis.set(vertices, 4);

            return new Carframe.Builder().build();
        }

        private void assembleWheel(Body body) {
            final CircleShape circle = new CircleShape();
            circle.m_radius = wheelRadius;

            //create car wheel
            FixtureDef fd = new FixtureDef();
            fd.shape = circle;
            fd.density = 1.0f;
            fd.friction = 0.9f;

            final Vec2 bodyPosition = body.getPosition();
            final BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(bodyPosition.x + WIDTH / 2 - wheelRadius, 0.5f);
            final Body wheel = this.world.createBody(bd);
            wheel.createFixture(fd);

            final WheelJointDef jd = new WheelJointDef();
            final Vec2 axis = new Vec2(0.0f, 1.0f);
            jd.initialize(body, wheel, wheel.getPosition(), axis);
            jd.frequencyHz = hz;
            jd.dampingRatio = zeta;
            world.createJoint(jd);
        }

        private Engine assembleEngine(Body body) {
            return new Engine.Builder().setFrequencyHz(hz).setSpeed(speed).build(this.world, body);
        }

        @Override
        public Vehicle build() {
            this.body = makeBody(position, density);

            assembleWheel(body);
            this.engine = assembleEngine(body);
            super.setDensity(3.0f).setHz(6.0f).setZeta(1.0f);
            return new Truck(this);
        }
    }
}
