package com.kamil.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Monster {

	public World world;
	public Body body;
	public Sprite s;
	
	public Texture monster;
	public TextureRegion[] monsterAnim;
	public Animation monsterAnimation;
	Rectangle range;

	public Monster(World world, Rectangle range) {
		this.world = world;
		this.range = range;
	}

	public void makePlayer(float f, float g) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(f, g+1);
		bodyDef.fixedRotation = true;
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();

		shape.setAsBox(2f, 2f);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape= shape;
		fdef.friction = 0;
		fdef.isSensor = true;
		
		body.createFixture(fdef).setUserData("monsterBody");
		shape.dispose();
		

		loadTextures();
	}

	private TextureRegion currentFrame;
	boolean turn = false;
	float stateTime = 0;
	boolean walkright = true;
	
	private void monsterWalk(){
		if(walkright)
			body.setLinearVelocity(4f, 0);
		else
			body.setLinearVelocity(-4f, 0);
		
		if(body.getPosition().x > 90)
			walkright = false;
		if(body.getPosition().x < 20)
			walkright = true;
	
//		System.out.println(body.getPosition().x+" - "+range.getX());
	}

	public void monsterUpdate(SpriteBatch sb, float stateTime) {
		currentFrame = (TextureRegion) monsterAnimation.getKeyFrame(stateTime);
		if(walkright && !currentFrame.isFlipX())
			currentFrame.flip(true, false);
		if(!walkright && currentFrame.isFlipX())
			currentFrame.flip(true, false);
		sb.draw(currentFrame, body.getPosition().x - 2.5f,
				body.getPosition().y - 2.5f, 5, 5);
		monsterWalk();

	}
	
	private void loadTextures(){
		monster = new Texture(Gdx.files.internal("brzydki.png"));
		TextureRegion[][] monsterTmp = TextureRegion.split(monster, monster.getWidth() / 4, monster.getHeight());
		monsterAnim = new TextureRegion[4];
		int mi = 0;
		for (int j = 0; j < 4; j++)
			monsterAnim[mi++] = monsterTmp[0][j];

		monsterAnimation = new Animation(0.1f, monsterAnim);
		monsterAnimation.setPlayMode(PlayMode.LOOP);
	}
	
}
