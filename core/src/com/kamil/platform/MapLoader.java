package com.kamil.platform;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamil.platform.elements.Bridge;
import com.kamil.platform.elements.Chain;
import com.kamil.platform.elements.Crate;

public class MapLoader {
	
	public TiledMap tiledMap;
	private TiledMapRenderer tiledRenderer;
	private World world;
	private Bridge br;
	static float mapScale = 1/8f;
	private float tileSize;
	private Player player;
	private Controller controller;
	
	ArrayList<Crate> crates;
	WorldContactListener contact;
	Monster monster;
	
	public MapLoader(World world, Player player){
		this.world = world;
		this.player = player;
		
		
		
		crates = new ArrayList<Crate>();
		tiledMap = new TmxMapLoader().load("map3.tmx");
		tiledRenderer = new OrthogonalTiledMapRenderer(tiledMap,mapScale);	
		makeFloors();
		makeChains();
		makeBridges();
		makeWalls();
		makeEnd();
		setPlayer();
		makeMonsters();
		controller = new Controller();
		
	}

	public void drawController(){
		controller.draw();
	}
	public void resizeController(int w, int h){
		controller.resize(w, h);
	}
	
	private void setPlayer(){
		Array<EllipseMapObject> object = tiledMap.getLayers().get("player").getObjects().getByType(EllipseMapObject.class);
		Ellipse e1 = object.get(0).getEllipse();
		player.makePlayer((int)(e1.x*mapScale), (int)(e1.y*mapScale));
	}
	
	public void updateMap(OrthographicCamera camera){
		tiledRenderer.setView(camera);
		tiledRenderer.render();
	}
	
	public void updatePlayer(SpriteBatch sb, float stateTime){
		player.inputHandler(controller, sb, stateTime);
	}
	
	public void updateChains(SpriteBatch sb){
		for(Chain c : chain)
			c.update(sb);
		br.updateBridge(sb);
	}
	
	public void updateMonster(SpriteBatch sb, float stateTime){
		monster.monsterUpdate(sb, stateTime);
	}
		
	private void makeFloors(){
		for(MapObject object : tiledMap.getLayers().get("floors").getObjects().getByType(RectangleMapObject.class)){
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			
			BodyDef bdef = new BodyDef();
			bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)*mapScale, (rectangle.getY() + rectangle.getHeight()/2)*mapScale);
			bdef.type = BodyType.StaticBody;
			
			Body mapBody = world.createBody(bdef);
			
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(rectangle.getWidth()/2*mapScale, rectangle.getHeight()/2*mapScale);
			FixtureDef fdef = new FixtureDef();
			fdef.friction = 1f;
			fdef.shape = shape;
			
			mapBody.createFixture(fdef);
		}			
	}
	
	
	private void makeEnd(){
		for(MapObject object : tiledMap.getLayers().get("end").getObjects().getByType(RectangleMapObject.class)){
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			
			BodyDef bdef = new BodyDef();
			bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)*mapScale, (rectangle.getY() + rectangle.getHeight()/2)*mapScale);
			bdef.type = BodyType.StaticBody;
			
			Body mapBody = world.createBody(bdef);
			
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(rectangle.getWidth()/2*mapScale, rectangle.getHeight()/2*mapScale);
			FixtureDef fdef = new FixtureDef();
			fdef.isSensor = true;
			fdef.shape = shape;
			
			mapBody.createFixture(fdef).setUserData("end");
		}			
	}
	
	
	private void makeBridges(){
		Array<EllipseMapObject> object = tiledMap.getLayers().get("bridge1").getObjects().getByType(EllipseMapObject.class);
		Ellipse e1 = object.get(0).getEllipse()
				, e2 = object.get(1).getEllipse();
		
		br = new Bridge.Builder(world)
				.setStartX((e1.x-e1.width/2)*mapScale)
				.setStartY((e1.y-e1.height/2)*mapScale)
				.setEndX((e2.x-e2.width/2)*mapScale)
				.setEndY((e2.y-e2.height/2)*mapScale)
				.setR(1f)
				.build();
		
		br.createBridge();
	}
	
	ArrayList<Chain> chain = new ArrayList<Chain>();
	
	private void makeChains(){
		Random r = new Random();
		for(MapObject object : tiledMap.getLayers().get("chain").getObjects().getByType(EllipseMapObject.class)){
			System.out.println("chain");
			Ellipse e = ((EllipseMapObject) object).getEllipse();
			chain.add(new Chain(world, 15 - r.nextInt(10),e.x*mapScale, e.y*mapScale, 0.8f));
		}		
	}
	
	
	private void makeWalls(){
		for(MapObject object : tiledMap.getLayers().get("crates").getObjects().getByType(RectangleMapObject.class)){
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
//			if(rectangle != null)
				makeWall(5, rectangle.getX()*mapScale, rectangle.getY()*mapScale, 1.5f);
		}			
	}
	
	private void makeMonsters(){
		for(MapObject object : tiledMap.getLayers().get("monster").getObjects().getByType(RectangleMapObject.class)){
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
//			if(rectangle != null)
				monster = new Monster(world, rectangle);
				monster.makePlayer(rectangle.getX()*mapScale+50, rectangle.getY()*mapScale);
		}			
	}
	
	public void makeWall(int base, float x, float y, float size){

		int posX = (int)x;
		int posY = (int)y;
		for(int i = 0 ; i <= base; i++){
			Crate cr = new Crate(world);
			cr.createCrate(posX, posY, size);
			crates.add(cr);
			posX += size*3f;
		}
		if(base <= 0)
			return;
		makeWall(--base, x+1.5f*size, y+2*size, size);
	}
	
	public void makeWall(int x, int y, float size, int tall){
		for(int i = 0 ; i < tall; i++){
			Crate cr = new Crate(world);
			cr.createCrate(x, y, size);
			y += size*2f;
			crates.add(cr);
		}
	}
	
	public void updateWall(SpriteBatch sb){
		for(Crate c : crates){
			c.update();
			c.s.draw(sb);
			
		}
	}
	
	
}
