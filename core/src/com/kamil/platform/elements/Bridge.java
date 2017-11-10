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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Bridge {

	float startX, startY, endX, endY;
	float r;
	World world;
	Texture t;
	Map<Body, Sprite> sprites = new HashMap();
	
	
	public Bridge(Builder builder){
		this.startX = builder.startX;
		this.startY = builder.startY;
		this.endX = builder.endX;
		this.endY = builder.endY;
		this.r = builder.r;		
		this.world = builder.world;
	}


	BodyDef bdef;
	Body bridgeElement;
	RevoluteJointDef jd;
	FixtureDef fdef;
	CircleShape circle;
	Body prevBody;
	Sprite s;
	ArrayList<Body> bodies = new ArrayList<Body>();
	
	public void createBridge(){
		//pierwszy element statyczny
		float x = startX;
		float y = startY;
		int elements = amountOfElements();

		makeBody(x, y, false, true);
		sprites.put(bridgeElement, makeNewSprite());
		bodies.add(bridgeElement);
		prevBody = bridgeElement;
		for(int i = 0; i < elements; i ++){
			x+=r*2;
			makeBody(x, y, true, false);
			sprites.put(bridgeElement, makeNewSprite());

			bodies.add(bridgeElement);
			prevBody = bridgeElement;
		}
		makeBody(x+r*2, y, false, false);
		bodies.add(bridgeElement);
		sprites.put(bridgeElement, makeNewSprite());
	}
	
	public void updateBridge(SpriteBatch sb){
		for(Body b : bodies){
			Sprite s = sprites.get(b);
			s.setRotation((float)Math.toDegrees(b.getAngle()));
			s.setPosition(b.getPosition().x - s.getWidth()/2, b.getPosition().y - s.getHeight()/2);
			s.draw(sb);
		}
	}
	
	private Sprite makeNewSprite(){
		s = new Sprite(new Texture(Gdx.files.internal("stone.png")));
		s.setSize(r*2, r*2);
		s.setOrigin(s.getWidth()/2, s.getHeight()/2);
		s.setPosition(bridgeElement.getPosition().x - s.getWidth()/2, bridgeElement.getPosition().y - s.getHeight()/2);
		return s;
	}
	
	private void makeBody(float x, float y, boolean dynamic, boolean firstElement){
		bdef = new BodyDef();
		if(dynamic)
			bdef.type = BodyType.DynamicBody;
		else
			bdef.type = BodyType.StaticBody;
		bdef.position.set(x, y);
		bridgeElement = world.createBody(bdef);
		circle = new CircleShape();
		circle.setRadius(r);
		fdef = new FixtureDef();
		fdef.shape = circle;
		fdef.density = 0.7f;
		fdef.friction = 0.95f;
		bridgeElement.createFixture(fdef);
		
		if(!firstElement){
			Vector2 anchor = new Vector2(bridgeElement.getPosition().x,
					bridgeElement.getPosition().y);
			jd = new RevoluteJointDef();
			jd.initialize(prevBody, bridgeElement, anchor);
			world.createJoint(jd);
		}
	}

	int amountOfElements(){
		float length = (float) Math.sqrt(
				Math.pow((endX-startX),2) 
				+ Math.pow((endY-startY),2)
				);
		int elements = (int) Math.ceil(
				Math.sqrt(length*length/4 +
						Math.pow(r*2, 2))/r);
		
		return elements;
	}
	

	
	public static class Builder{
		float startX, startY, endX, endY;
		float r;
		World world;

		public Builder(World world){
			this.world = world;
		}
		
		public Builder setStartX(float startX) {
			this.startX = startX;
			return this;
		}

		public Builder setStartY(float startY) {
			this.startY = startY;
			return this;
		}
		public Builder setEndX(float endX) {
			this.endX = endX;
			return this;
		}
		public Builder setEndY(float endY) {
			this.endY = endY;
			return this;
		}
		public Builder setR(float r) {
			this.r = r;
			return this;
		}
		
		public Bridge build(){
			return new Bridge(this);
		}
		
	}
	
}
