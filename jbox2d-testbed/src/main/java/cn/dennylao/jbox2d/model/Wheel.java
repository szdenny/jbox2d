package cn.dennylao.jbox2d.model;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Wheel {
    private World world;
    private float radius;
    private float density;
    private float friction;

    public Wheel(Builder builder) {
        this.world = builder.world;
        this.radius = builder.radius;
        this.density = builder.density;
        this.friction = builder.friction;
    }

    public void assemble(Carframe frame) {
        final CircleShape circle = new CircleShape();
        circle.m_radius = radius;

        //create car wheel
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = density;
        fd.friction = friction;

        final Vec2 bodyPosition = frame.getPosition();
        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(frame.getWheelConnectionPoint());
        final Body wheel = this.world.createBody(bd);
        wheel.createFixture(fd);
    }

    public static class Builder {
        private World world;
        private float radius;
        private float density;
        private float friction;

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
