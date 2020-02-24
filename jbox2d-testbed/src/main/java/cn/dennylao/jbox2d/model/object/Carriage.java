package cn.dennylao.jbox2d.model.object;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public final class Carriage extends Vehicle {
    private World world;
    private Body connectionRod;

    public Carriage(Builder builder) {
        super(builder);
        this.world = builder.world;
        this.connectionRod = builder.connectionRod;
//        PolygonShape bottom = new PolygonShape();
//        bottom.setAsBox(1.5f, 0.15f);
//
//        PolygonShape left = new PolygonShape();
//        left.setAsBox(0.15f, 2.7f, new Vec2(-1.45f, 2.35f), 0.2f);
//
//        PolygonShape right = new PolygonShape();
//        right.setAsBox(0.15f, 2.7f, new Vec2(1.45f, 2.35f), -0.2f);
//
//        BodyDef bd = new BodyDef();
//        bd.type = BodyType.DYNAMIC;
//        bd.position.set(getPosition());
//        body = world.createBody(bd);
//        body.createFixture(bottom, 4.0f);
//        body.createFixture(left, 4.0f);
//        body.createFixture(right, 4.0f);
    }

    public World getWorld() {
        return this.world;
    }

    public void assemble(Chassis frame, Vec2 position) {
        RevoluteJointDef rjd = new RevoluteJointDef();
        System.out.println(position);
        rjd.initialize(connectionRod, frame.getBody(), new Vec2(position.x, getPosition().y));
        getWorld().createJoint(rjd);
    }

    public static class Builder extends Vehicle.Builder {
        private final float wheelRadius = 0.6f;
        private final float WIDTH = 4.6f;
        private final float HEIGHT = 0.2f;
        private World world;
        private Body connectionRod;

        public Builder(World world) {
            this.world = world;
        }

        public World getWorld() {
            return world;
        }

        private Chassis makeFrame(Vec2 position, float density) {
            return new Chassis.Builder(world).setPosition(position).setWheelbase(4.0f).setDensity(2.0f)
                    .setDampingRatio(0.7f).setFrequencyHz(2.9f).build();
        }

        private void assembleWheel(Chassis frame) {
            final Wheel wheel = new Wheel.Builder(world).setRadius(0.4f).build();
            wheel.assemble(frame, frame.getFrontConnectionPoint());

            final Wheel wheel2 = new Wheel.Builder(world).setRadius(0.4f).build();
            wheel2.assemble(frame, frame.getRearConnectionPoint());
        }

        private void assembleConnectionRod(Chassis frame, Vec2 position) {
            final Body prevBody = frame.getBody();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.5f, 0.125f);

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(position.x + 2.0f, position.y);
            connectionRod = getWorld().createBody(bd);
            connectionRod.createFixture(shape, 0.5f);

            RevoluteJointDef rjd = new RevoluteJointDef();
            rjd.initialize(connectionRod, prevBody, new Vec2(position.x + 1.5f, position.y));
            rjd.enableMotor = false;
            getWorld().createJoint(rjd);
        }

        @Override
        public Carriage build() {
            final float density = 1.0f;
            this.chassis = makeFrame(position, density);

            assembleWheel(chassis);
            assembleConnectionRod(chassis, position);
            super.setChassis(chassis);
            return new Carriage(this);
        }
    }
}
