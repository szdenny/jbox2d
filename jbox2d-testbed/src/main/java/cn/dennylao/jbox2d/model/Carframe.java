package cn.dennylao.jbox2d.model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import java.awt.*;

public class Carframe {
    private Body body;
    private Vec2 position;

    public Carframe(Builder builder){
        //make car body
        final BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position = position;
        final Body body = world.createBody(bd);
        body.createFixture(chassis, density);

    }
    public Vec2 getPosition() {
        return body.getPosition();
    }

    public static class Builder{
        public Builder setShape(Shape shape){
            return this;
        }
        public Carframe build(){
            return new Carframe(this);
        }
    }
}
