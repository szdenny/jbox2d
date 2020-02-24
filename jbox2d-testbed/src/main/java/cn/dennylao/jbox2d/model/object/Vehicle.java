package cn.dennylao.jbox2d.model.object;

import org.jbox2d.common.Vec2;

public abstract class Vehicle {
    private Chassis chassis;
    private Engine engine;
    private Vec2 position;

    public Vehicle(Builder builder) {
        this.chassis = builder.chassis;
        this.engine = builder.engine;
        this.position = builder.position;
    }

    public Engine getEngine() {
        return this.engine;
    }

    public Chassis getChassis() {
        return chassis;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setChassis(Chassis chassis) {
        this.chassis = chassis;
    }

    public void forward(float speed) {
        engine.setMotorSpeed(-speed);
    }

    public void backward(float speed) {
        engine.setMotorSpeed(speed);
    }

    public void start() {
        engine.enableMotor(true);
    }

    public void stop() {
        engine.setMotorSpeed(0);
        engine.enableMotor(false);
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getPosition() {
        return this.chassis.getPosition();
    }

    public static abstract class Builder {
        protected Chassis chassis;
        protected Engine engine;
        protected Vec2 position = new Vec2(0, 0);

        public Builder setPosition(Vec2 position) {
            this.position = position;
            return this;
        }

        public Builder setChassis(Chassis chassis) {
            this.chassis = chassis;
            return this;
        }

        public Builder setEngine(Engine engine) {
            this.engine = engine;
            return this;
        }

        public abstract Vehicle build();
    }
}
