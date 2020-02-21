package cn.dennylao.jbox2d.model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public final class Truck extends Vehicle {

    public Truck(Builder builder) {
        super(builder);
    }

    public static class Builder extends Vehicle.Builder {
        private final float wheelRadius = 0.4f;
        private final float WIDTH = 3.0f;
        private final float HEIGHT = 1.0f;
        private World world;
        private Engine engine;
        private Wheel wheel;
        private Carframe frame;
        private float density = 1.0f;

        public Builder(World world) {
            this.world = world;
        }

        protected Carframe makeFrame(Vec2 position, float density) {
            Vec2 vertices[] = new Vec2[8];
            vertices[0] = new Vec2(-1.0f / 2 * WIDTH, 0.5f * HEIGHT);
            vertices[1] = new Vec2(-1.0f / 2 * WIDTH, -0.5f * HEIGHT);
            vertices[2] = new Vec2(1.0f / 2 * WIDTH, -0.5f * HEIGHT);
            vertices[3] = new Vec2(1.0f / 2 * WIDTH, 0.5f * HEIGHT);

            return new Carframe.Builder(world).setPosition(position).setChassis(vertices, 4).build();
        }

        private void assembleWheel(Carframe frame) {
            this.wheel = new Wheel.Builder(world).build();
            wheel.assemble(frame);
        }

        private void assembleEngine(Carframe frame) {
            this.engine = new Engine.Builder(world).setFrequencyHz(hz).setSpeed(speed).build();
            engine.assemble(frame);
        }

        @Override
        public Vehicle build() {
            this.frame = makeFrame(position, density);

            assembleWheel(frame);
            assembleEngine(frame);
            super.setDensity(3.0f).setHz(6.0f).setZeta(1.0f);
            return new Truck(this);
        }
    }
}
