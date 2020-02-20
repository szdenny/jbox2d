package cn.dennylao.jbox2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class Car {
    //car body
    private Body body;

    //driving wheel
    private Body drivingWheel;

    private float speed;
    private float hz = 3.0f;
    private float zeta = 0.6f;

    private Car() {

    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vec2 getPosition() {
        return this.body.getPosition();
    }
}
