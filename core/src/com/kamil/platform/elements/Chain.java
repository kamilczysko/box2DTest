package com.kamil.platform.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Chain {

	public World world;
	float x,y;
	float r;
	int elements;
	Texture t = new Texture(Gdx.files.internal("stone.png"));
	ArrayList<Body> bodies = new ArrayList<Body>();
	Map<Body, Sprite> map = new HashMap();
	
	public Chain(World world , int elements, float x, float y, float r){
		this.world = world;
		this.elements = elements;
		this.x = x;
		this.r = r;
		this.y = y;
		makeChain();
	}
	

	private void makeChain(){
		RevoluteJointDef jd = new RevoluteJointDef();
		
		BodyDef bd = new BodyDef();
        bd.type = BodyType.StaticBody;
        bd.position.set(x, y);
        Body body = world.createBody(bd);
        CircleShape circle = new CircleShape();
        circle.setRadius(r);
        bodies.add(body);
        
        Sprite s = new Sprite(t);
        s.setSize(r*2, r*2);
        s.setOrigin(s.getWidth()/2, s.getHeight()/2);
        s.setPosition(body.getPosition().x-s.getWidth()/2, body.getPosition().y-s.getHeight()/2);
        body.setUserData(s);
        map.put(body, s);
        
        body.createFixture(circle,0.5f);
		
		Body prevBody = body;
		int angle = 0;
		for(int i=1; i<elements+1; i++)
		    {
		        bd = new BodyDef();
		        bd.type = BodyType.DynamicBody;
		        bd.position.set(x, y - i*(r*2));
		        body = world.createBody(bd);
		        circle = new CircleShape();
		        circle.setRadius(r);
		        bodies.add(body);
		        
		        s = new Sprite(t);
		        s.setSize(r*2, r*2);
		        s.setOrigin(s.getWidth()/2, s.getHeight()/2);
		        s.setPosition(body.getPosition().x-s.getWidth()/2, body.getPosition().y-s.getHeight()/2);
		        body.setUserData(s);
		        map.put(body, s);
		        
		        body.createFixture(circle,0.5f);

		        Vector2 anchor = new Vector2(x, body.getPosition().y);
		        jd.initialize(prevBody, body, anchor);
		        world.createJoint(jd);
		        prevBody = body;
		    }
	}
	
	public void update(SpriteBatch sb){
		for(Body b : bodies){
			Sprite s = map.get(b);
			s.setRotation((float)Math.toDegrees(b.getAngle()));
			s.setPosition(b.getPosition().x - s.getWidth()/2, b.getPosition().y - s.getHeight()/2);
			s.draw(sb);
		}
	}
	
	public Map map(){
		return map;
	}
	public ArrayList bodies(){
		return bodies;
	}
}
