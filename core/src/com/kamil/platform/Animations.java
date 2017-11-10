package com.kamil.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Animations {

	public Texture walk;
	public TextureRegion[] walkAnim;
	public Animation walkAnimation;
	
	public Texture idle;
	public TextureRegion[] idleAnim;
	public Animation idleAnimation;
	


	public TextureRegion[][] shot;
	public Texture shotTex;
	
	public void loadTextures() {
		walk = new Texture(Gdx.files.internal("heroWalk.png"));
		TextureRegion[][] tmp = TextureRegion.split(walk, walk.getWidth() / 6, walk.getHeight());
		walkAnim = new TextureRegion[6];
		int index = 0;
		for (int j = 0; j < 6; j++)
			walkAnim[index++] = tmp[0][j];

		walkAnimation = new Animation(0.05f, walkAnim);
		walkAnimation.setPlayMode(PlayMode.LOOP);
        
		idle = new Texture(Gdx.files.internal("heroIdle.png"));
		TextureRegion[][] tmpIdle = TextureRegion.split(idle, idle.getWidth() / 3, idle.getHeight());
		idleAnim = new TextureRegion[3];
		int animIndex = 0;
		for (int i = 0; i < 3; i++)
			idleAnim[animIndex++] = tmpIdle[0][i];

		idleAnimation = new Animation(0.5f, idleAnim);
		idleAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
        shot = new TextureRegion[1][1];        
		shotTex = new Texture(Gdx.files.internal("shot.png"));
		shot = TextureRegion.split(shotTex, shotTex.getWidth(), shotTex.getHeight());
	}
	
	
//	public Animations(){
//		loadTextures();
//	}
	
	public Animation getWalkAnimation(){
		return walkAnimation;
	}
	
	public Animation getIdleAnimation() {
		return idleAnimation;
	}
	
	public TextureRegion[][] getShotAnimation() {
		return shot;
	}
	
}
