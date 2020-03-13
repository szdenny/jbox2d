package cn.dennylao.jbox2d.model.object;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Automobile chassis
 */
public class Chassis {
    private World world;
    private Body body;
    private float frequencyHz;
    private float dampingRatio;
    private float density;
    private Vec2 position;
    private float wheelbase;//轴距
    private float width;
    private float height;

    public Chassis(Builder builder) {
        this.world = builder.world;
        this.position = builder.position;
        this.density = builder.density;
        this.height = builder.height;
        this.width = builder.width;
        this.wheelbase = builder.wheelbase;
        this.frequencyHz = builder.frequencyHz;
        this.dampingRatio = builder.dampingRatio;

        //make car body
        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position = this.position;
        bd.angularDamping = 100;
        this.body = world.createBody(bd);

        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);
        body.createFixture(shape, this.density);
    }

    public Body getBody() {
        return this.body;
    }

    public Vec2 getPosition() {
        return body.getPosition();
    }

    public Vec2 getRearConnectionPoint() {
        float x = position.x - wheelbase / 2.0f;
        return new Vec2(x, height * 1.5f);
    }

    public Vec2 getFrontConnectionPoint() {
        float x = position.x + wheelbase / 2.0f;
        return new Vec2(x, height * 1.5f);
    }

    public Vec2 getRearHookPoint() {
        return new Vec2(position.x - width / 2.0f, height);
    }

    public float getFrequencyHz() {
        return this.frequencyHz;
    }

    public float getDampingRatio() {
        return this.dampingRatio;
    }

    public static class Builder {
        private World world;
        private Vec2 position = new Vec2(0.0f, 1.0f);
        private float wheelbase = 4.0f;
        private float width = 6.0f;
        private float height = 0.5f;
        private float frequencyHz = 4.0f;
        private float dampingRatio = 0.7f;
        private float density = 1.0f;

        public Builder(World world) {
            this.world = world;
        }

        public Builder setPosition(Vec2 position) {
            this.position = position;
            return this;
        }

        public Builder setFrequencyHz(float frequencyHz) {
            this.frequencyHz = frequencyHz;
            return this;
        }

        public Builder setDampingRatio(float dampingRatio) {
            this.dampingRatio = dampingRatio;
            return this;
        }

        public Builder setWheelbase(float wheelbase) {
            this.wheelbase = wheelbase;
            return this;
        }

        public Builder setWidth(float width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(float height) {
            this.height = height;
            return this;
        }

        public Builder setDensity(float density) {
            this.density = density;
            return this;
        }

        public Chassis build() {
            return new Chassis(this);
        }
    }
}
