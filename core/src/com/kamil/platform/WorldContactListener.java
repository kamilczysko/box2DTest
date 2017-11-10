package com.kamil.platform;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener{

	Player player;
	MyGame myGame;
	
	public boolean leftHit = false;
	public boolean rightHit = false;
	public boolean endGame = false;
	
	public WorldContactListener(Player player, MyGame myGame) {
		this.player = player;
		this.myGame = myGame;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		if((f1.getUserData() == "right" || f2.getUserData() == "right")&&
				(f1.getUserData() == "monsterBody" || f2.getUserData() == "monsterBody")){
			player.hit(true);
		}
		if((f1.getUserData() == "left" || f2.getUserData() == "left")&&
				(f1.getUserData() == "monsterBody" || f2.getUserData() == "monsterBody")){
			player.hit(false);
		}
		
		if((f1.getUserData() == "end" || f2.getUserData() == "end")&&
				(f1.getUserData() == "playerBody" || f2.getUserData() == "playerBody")){
//			myGame.done();
			myGame.end();
		}
	}

	
	@Override
	public void endContact(Contact contact) {
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		if((f1.getUserData() == "right" || f2.getUserData() == "right")&&
				(f1.getUserData() == "monsterBody" || f2.getUserData() == "monsterBody")){
			rightHit = false;
		}
		if((f1.getUserData() == "left" || f2.getUserData() == "left")&&
				(f1.getUserData() == "monsterBody" || f2.getUserData() == "monsterBody")){
			leftHit = false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
