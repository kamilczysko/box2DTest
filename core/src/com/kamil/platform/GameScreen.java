package com.kamil.platform;

import java.util.ArrayList;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kamil.platform.elements.Crate;

public class GameScreen implements Screen{
	
	public MyGame game;
	
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	
	private WorldContactListener contact;
	
	private Viewport view;
	public World world;
	
	public Body body;
	public Player player;
	
	public MapLoader mapLoader;
		
	Sprite s;
	public static SpriteBatch sb;
	Texture t;
	
	ArrayList<Crate> crates;
	
	byte playerFlag;//status gracza
	boolean turn = false;
	
	float factor = 12;
	
	float viewWidth = 0;
	float viewHeight = 0;
	
	TextureRegion currentFrame;
	float stateTime;
	byte prevStat = 0;
	Texture stone ;
	
	Hud hud;
	
	public GameScreen(MyGame game){
		
		this.game = game;
		this.sb = game.sb;
		
		viewWidth = Gdx.graphics.getWidth();
		viewHeight = Gdx.graphics.getHeight();
		
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		world = new World(new Vector2(0, -14f), true);

		hud = new Hud(this);
		
		
		player = new Player(world);
		mapLoader = new MapLoader(world, player);
		
		
		view = new FitViewport(viewWidth/factor, viewHeight/factor, camera);
		
		contact = new WorldContactListener(player, game);

		world.setContactListener(contact);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(1 / 30f, 3, 3);
		updateCamera();
		camera.update();
		
		sb.setProjectionMatrix(camera.combined);
		sb.begin();

			stateTime += Gdx.graphics.getDeltaTime();
			player.updatePlayer(sb, stateTime);
			mapLoader.updateWall(sb);
			mapLoader.updateChains(sb);
			mapLoader.updatePlayer(sb, stateTime);
			mapLoader.updateMonster(sb, stateTime);

		sb.end();

		mapLoader.updateMap(camera);
//		debugRenderer.render(world, camera.combined);
		if(Gdx.app.getType() == Application.ApplicationType.Android)
			mapLoader.drawController();
		

		hud.draw();
		hud.update(player.lives);
	}
	
	public int getPlayerLives(){
		return player.lives;
	}

	private void updateCamera(){
		camera.position.set(player.body.getPosition().x, player.body.getPosition().y-2 ,0);
	}
	
	Vector3 getMousePosInGameWorld() {
		 return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		}
	
	Vector3 getCoordInGameWorld(float x, float y) {
		 return camera.unproject(new Vector3(x, y, 0));
		}

	
	int y = 0;
	
	@Override
	public void resize(int w,int h) {
		
		camera.setToOrtho(false, w/factor, h/factor);
		view.update(w, h);
		mapLoader.resizeController(w, h);
		hud.resize(w, h);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {

		debugRenderer.dispose();
		world.dispose();
	}
}
