package com.kamil.platform;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

	public World world;
	public Body body;
	public Sprite s;
	public Animations animations;
	private Animation walk, idle;
	private boolean isHitted = false;
	public int lives = 3;

	public Player(World world) {
		this.world = world;
		this.animations = new Animations();
		animations.loadTextures();
		this.walk = animations.walkAnimation;
		this.idle = animations.idleAnimation;
	}

	public void makePlayer(int x, int y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.fixedRotation = true;
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();

		shape.setAsBox(1f, 2f);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape= shape;
		fdef.friction = 0;
		
		body.createFixture(fdef).setUserData("playerBody");
		shape.dispose();
		
		EdgeShape edge = new EdgeShape();
		edge.set(new Vector2(-2,2), new Vector2(2,2));
		fdef.shape = edge;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("head");
		
		EdgeShape edgel = new EdgeShape();
		edgel.set(new Vector2(-2,2), new Vector2(-2,0));
		fdef.shape = edgel;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("left");
		
		EdgeShape edger = new EdgeShape();
		edger.set(new Vector2(2,2), new Vector2(2,0));
		fdef.shape = edger;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("right");
		
	}

	private TextureRegion currentFrame;
	boolean turn = false;
	float stateTime = 0;

	public void updatePlayer(SpriteBatch sb, float stateTime) {

	}
	float tmpState = 0.0f;
	
	public void inputHandler(Controller controller, SpriteBatch sb, float stateTime) {
		if(!isHitted){
			if(!controller.left && !controller.right){
				body.setLinearVelocity(body.getLinearVelocity().x * 0.9f, body.getLinearVelocity().y);
				currentFrame = (TextureRegion) idle.getKeyFrame(stateTime);
			}
			if (controller.left){
				body.setLinearVelocity(-13, body.getLinearVelocity().y);
				currentFrame = (TextureRegion) walk.getKeyFrame(stateTime);
				turn = true;
			}
			if (controller.right){
				body.setLinearVelocity(13, body.getLinearVelocity().y);
				currentFrame = (TextureRegion) walk.getKeyFrame(stateTime);
				turn = false;
			}
			if (controller.jump) {
				body.applyLinearImpulse(0, 20f, body.getPosition().x, body.getPosition().y, true);
				controller.jump = false;
			}
			
			if (turn && !currentFrame.isFlipX())
				currentFrame.flip(true, false);
			else if (!turn && currentFrame.isFlipX())
				currentFrame.flip(true, false);
		}else{
			if(tmpState == 0.0f)
				tmpState = stateTime;
			if(stateTime - tmpState > 0.5f){
				isHitted = false;
				tmpState = 0.0f;
			}
		}
		sb.draw(currentFrame, body.getPosition().x - 1.8f, body.getPosition().y - 1.8f, 4, 4);

	}
	
	public void hit(boolean right){
		if(right)
			body.applyLinearImpulse(-25f, 0, body.getPosition().x, body.getPosition().y, true);
		if(!right)
			body.applyLinearImpulse(25f, 0, body.getPosition().x, body.getPosition().y, true);
		isHitted = true;
		lives --;
	}
}
