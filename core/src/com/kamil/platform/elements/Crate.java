package com.kamil.platform.elements;

//import static com.kamil.platform.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Crate{
	
	private World world;
	public Body body;
	private Texture t = new Texture(Gdx.files.internal("crate.png"));
		
	public Sprite s;
	
	public Crate(World world){
		this.world = world;
	}
	
	public void setWorld(World world){
		this.world = world;
	}
	
	public void createCrate(int posX, int posY, float size){
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(posX, posY);
		body = world.createBody(bdef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(size, size);
		s = new Sprite(t);
		s.setSize(size*2, size*2);
		s.setOrigin(s.getWidth()/2, s.getHeight()/2);
		body.setUserData(this);
		FixtureDef fdef = new FixtureDef();
		fdef.density = 0.1f;
		fdef.restitution = 0.3f;
		fdef.friction = 0.9f;
		fdef.shape = box;	
		
		Fixture f = body.createFixture(fdef);
	
		box.dispose();
	}
	

	
	public void update(){
		s.setRotation((float)Math.toDegrees(body.getAngle()));
		s.setPosition(body.getPosition().x - s.getWidth()/2, body.getPosition().y - s.getHeight()/2);
	}
	
	public Body getBody(){
		return body;
	}
	public float getAngle(){
		return (float) Math.toDegrees(body.getAngle());
	}

}
