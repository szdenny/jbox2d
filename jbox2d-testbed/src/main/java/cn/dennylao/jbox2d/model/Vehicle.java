package cn.dennylao.jbox2d.model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.WheelJoint;

public abstract class Vehicle {
    private float hz = 4.0f;
    private float zeta = 0.7f;
    private float speed = 50.0f;
    private Carframe frame;
    private Engine engine;
    private Vec2 position;

    public Vehicle(Builder builder) {
        this.speed = builder.speed;
        this.hz = builder.hz;
        this.zeta = builder.zeta;
        this.frame = builder.frame;
        this.engine = builder.engine;
        this.position = builder.position;
    }

    public void forward() {
        engine.setMotorSpeed(speed);
    }

    public void backward() {
        engine.setMotorSpeed(-speed);
    }

    public void start() {
        engine.enableMotor(true);
    }

    public void stop() {
        engine.setMotorSpeed(0);
        engine.enableMotor(false);
    }

    public Vec2 getPosition() {
        return this.frame.getPosition();
    }


    public static abstract class Builder {
        protected float hz = 4.0f;
        protected float zeta = 0.7f;
        protected float speed = -50.0f;
        private float density = 1.0f;
        protected Carframe frame;
        protected Engine engine;
        protected Vec2 position;

        public Builder setHz(float hz) {
            this.hz = hz;
            return this;
        }

        public Builder setZeta(float zeta) {
            this.zeta = zeta;
            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder setDensity(float density) {
            this.density = density;
            return this;
        }

        public Builder setPosition(Vec2 position) {
            this.position = position;
            return this;
        }

        public Builder setFrame(Carframe frame){
            this.frame = frame;
            return this;
        }

        public Builder setEngine(Engine engine){
            this.engine = engine;
            return this;
        }

        public abstract Vehicle build();
    }
}
