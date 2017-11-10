package com.kamil.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

	public Stage stage;
	private Viewport view;
	private OrthographicCamera camera;
	Table table;
	GameScreen gameScreen;
	Image life[];
	
	public Hud(GameScreen gameScreen) {

		camera = new OrthographicCamera();
		this.view = new FitViewport(Gdx.graphics.getWidth() * MapLoader.mapScale,
				Gdx.graphics.getHeight() * MapLoader.mapScale, camera);
		stage = new Stage(view, GameScreen.sb);
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		table.top().right();
		table.setFillParent(true);

		life = new Image[3];
		for(int i = 0; i < life.length; i++){
			life[i] = new Image(new Texture("life.png"));
			life[i].setSize(3, 3);
			life[i].setScale(1/4f);
//			table.setSize(10, 5);
			table.add(life[i]);
		}		
		table.setScale(1/4f);
		stage.addActor(table);
	}
	
	public void update(int lives){
		table.clear();
		for(int i = 0; i < lives; i++)
			table.add(life[i]);
		table.scaleBy(1/4f);
	}

	public void draw(){

		stage.draw();
	}
	
	public void resize(int w, int h){
		view.update(w, h);
	}

}
