package com.kamil.platform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends Game{
	
	public SpriteBatch sb;
	
	@Override
	public void create () {
		sb = new SpriteBatch();
		setScreen(new GameScreen(this));				
	}

	@Override
	public void render(){
		super.render();
	
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
	

	@Override
	public void resize(int w, int h){
		super.resize(w, h);
	}
	
	@Override
	public void resume(){
	}
	
	public void end(){
		setScreen(new DoneScreen());
		this.dispose();
	}
	
	
		
}
