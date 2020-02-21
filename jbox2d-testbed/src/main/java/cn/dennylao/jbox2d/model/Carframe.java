package cn.dennylao.jbox2d.model;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.WheelJointDef;

public class Carframe {
    private World world;
    private Body body;
    private float frequencyHz;
    private float dampingRatio;
    private float density;
    private PolygonShape chassis;
    private Vec2 position;
    private Vec2 engineConnectionPoint;
    private Vec2 wheelConnectionPoint;

    public Carframe(Builder builder) {
        this.world = builder.world;
        this.chassis = builder.chassis;
        this.position = builder.position;
        this.density = builder.density;
        this.engineConnectionPoint = builder.engineConnectionPoint;
        this.wheelConnectionPoint = builder.wheelConnectionPoint;

        //make car body
        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position = this.position;
        this.body = world.createBody(bd);
        body.createFixture(chassis, this.density);
    }

    public Body getBody() {
        return this.body;
    }

    public Vec2 getPosition() {
        return body.getPosition();
    }

    public void assembleWheel(Body wheel) {
        final WheelJointDef jd = new WheelJointDef();
        final Vec2 axis = new Vec2(0.0f, 1.0f);
        jd.initialize(body, wheel, wheel.getPosition(), axis);
        jd.frequencyHz = frequencyHz;
        jd.dampingRatio = dampingRatio;
        world.createJoint(jd);
    }

    public void assembleEngine(Engine engine) {
        //do nothing
    }

    public Vec2 getEngineConnectionPoint() {
        return engineConnectionPoint;
    }

    public Vec2 getWheelConnectionPoint() {
        return wheelConnectionPoint;
    }

    public static class Builder {
        private World world;
        private Vec2 position = new Vec2(0.0f, 1.0f);
        private PolygonShape chassis;
        private Vec2 engineConnectionPoint;
        private Vec2 wheelConnectionPoint;
        private float density = 1.0f;

        public Builder(World world) {
            this.world = world;
        }

        public Builder setPosition(Vec2 position) {
            this.position = position;
            return this;
        }

        public Builder setEngineConnectionPoint(float x, float y) {
            this.engineConnectionPoint = new Vec2(x, y);
            return this;
        }

        public Builder setWheelConnectionPoint(float x, float y) {
            this.wheelConnectionPoint = new Vec2(x, y);
            return this;
        }

        public Builder setChassis(final Vec2[] vertices, final int count) {
            chassis = new PolygonShape();
            chassis.set(vertices, count);

            return this;
        }

        public Carframe build() {
            return new Carframe(this);
        }
    }
}
