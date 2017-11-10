package com.kamil.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Controller {
	Viewport view;
	Stage stage;
	boolean left, right, jump;
	OrthographicCamera camera;

	public Controller() {
		camera = new OrthographicCamera();
		this.view = new FitViewport(Gdx.graphics.getWidth()*MapLoader.mapScale	, Gdx.graphics.getHeight()*MapLoader.mapScale, camera);
		stage = new Stage(view, GameScreen.sb);
		Gdx.input.setInputProcessor(stage);
		
		Table table = new Table();
		table.left().bottom();
		table.setSize(100, 100);
		
		Image leftImg = new Image(new Texture("arrows/left.png"));
		Image rightImg = new Image(new Texture("arrows/right.png"));
		Image upImg = new Image(new Texture("arrows/up.png"));

		leftImg.setSize(50, 50);
		rightImg.setSize(50, 50);
		upImg.setSize(50, 50);
		
		stage.addListener(new InputListener(){
			
			
			 @Override
	            public boolean keyDown(InputEvent event, int keycode) {
	                switch(keycode){
	                    case Input.Keys.W:
	                        jump = true;
	                        break;
	                    case Input.Keys.A:
	                        left = true;
	                        break;
	                    case Input.Keys.D:
	                        right = true;
	                        break;
	                }
	                return true;
	            }

	            @Override
	            public boolean keyUp(InputEvent event, int keycode) {
	                switch(keycode){
	                case Input.Keys.W:
                        jump = false;
                        break;
                    case Input.Keys.A:
                        left = false;
                        break;
                    case Input.Keys.D:
                        right = false;
                        break;
	                }
	                return true;
	            }
	            
				@Override
				public boolean touchDown(InputEvent input, float x, float y, int pointer, int button){
					if(x > view.getCamera().viewportWidth/2)
						jump = true;
					return true;
				}
				@Override
				public void touchUp(InputEvent input, float x, float y, int pointer, int button){
					jump = false;
				}
			
			
		});
		
		
		leftImg.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent input, float x, float y, int pointer, int button){
			
				left = true;
				return true;
			}
			@Override
			public void touchUp(InputEvent input, float x, float y, int pointer, int button){
				left = false;
			}
			
		});
		
		rightImg.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent input, float x, float y, int pointer, int button){
				right = true;
				return true;
			}
			@Override
			public void touchUp(InputEvent input, float x, float y, int pointer, int button){
				right = false;
			}
		});
		
		table.add(leftImg);
		table.add(rightImg);
		stage.addActor(table);
		
	}
	
	
	public void draw(){
		stage.draw();
	}


	public boolean isLeft() {
		return left;
	}


	public boolean isRight() {
		return right;
	}


	public boolean isJump() {
		return jump;
	}
	
	public void resize(int w, int h){
		view.update(w, h);
		
	}

}
