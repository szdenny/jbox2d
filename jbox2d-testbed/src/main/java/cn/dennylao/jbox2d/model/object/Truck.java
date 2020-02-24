package cn.dennylao.jbox2d.model.object;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public final class Truck extends Vehicle {
    private Carriage carriage;

    public Truck(Builder builder) {
        super(builder);
        this.carriage = builder.carriage;
    }

    public static class Builder extends Vehicle.Builder {
        private final float wheelRadius = 0.6f;
        private final float WIDTH = 4.6f;
        private final float HEIGHT = 0.2f;
        private World world;
        private Wheel wheel;
        private Carriage carriage;

        public Builder(World world) {
            this.world = world;
        }

        protected Chassis makeFrame(Vec2 position, float density) {
            //TODO make truck frame beyond the chassis
            return new Chassis.Builder(world).setPosition(position).setDensity(10.0f).setWidth(WIDTH)
                    .setWheelbase(WIDTH - 2 * wheelRadius).setDampingRatio(2.9f).setFrequencyHz(4.0f).build();
        }

        private void assembleWheel(Chassis frame) {
            this.wheel = new Wheel.Builder(world).setRadius(wheelRadius).build();
            wheel.assemble(frame, frame.getFrontConnectionPoint());
        }

        private void assembleEngine(Chassis frame) {
            this.engine = new Engine.Builder(world).setTorque(200.0f).setRadius(wheelRadius).setSpeed(50.0f).build();
            engine.assemble(frame, frame.getRearConnectionPoint());
        }

        private void assembleCarriage(Chassis frame) {
            this.carriage = (Carriage) new Carriage.Builder(world).setPosition(new Vec2(-WIDTH - 3.0f, 1.5f)).build();
            carriage.assemble(frame, frame.getRearHookPoint());
        }

        @Override
        public Vehicle build() {
            final float density = 1.0f;
            this.chassis = makeFrame(position, density);

            assembleWheel(chassis);
            assembleEngine(chassis);
            assembleCarriage(chassis);
            super.setChassis(chassis);
            return new Truck(this);
        }
    }
}
