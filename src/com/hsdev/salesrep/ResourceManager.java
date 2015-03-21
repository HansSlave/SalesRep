package com.hsdev.salesrep;

import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import android.R.color;
import android.content.Context;
import android.graphics.Typeface;

public class ResourceManager {

	//Tworzenie klasy jako singleton - nie mo¿e byæ drugiej instancji
	private static ResourceManager INSTANCE;

	private ResourceManager() {
		// TODO Auto-generated constructor stub
	}

	public static ResourceManager getIstance(){
		if(INSTANCE == null){
			INSTANCE = new ResourceManager();
		}
		return INSTANCE;
	}
	
	//check if resources are loaded
	public boolean isSplashResourcesLoaded = false;
	public boolean isMainMenuResourcesLoaded = false;
	public boolean isOfficeResourcesLoaded = false;
	public boolean isRaceResourcesLoaded = false;
	public boolean isTalkResourcesLoaded = false;

	//splashScreen
	public BitmapTextureAtlas splashTA;
	public ITextureRegion splashTR;

	//main menu
	public BitmapTextureAtlas mainMenuTA;
	public ITextureRegion mainMenuBackgroundTR;
	public ITextureRegion newGameButtonTR;
	
	//Office
	public BitmapTextureAtlas officeTA;
	public Font digitalFont;

	//Race
	public BitmapTextureAtlas raceTA;
	public ITextureRegion raceBackgroundTR;
	public ITextureRegion whiteCarTR;
	public ITextureRegion maluchTR;

	//Talk
	public BitmapTextureAtlas talkTA;
	public ITextureRegion notesTR;
	public ITextureRegion playerPowerTR;
	public ITextureRegion opponentPowerTR;
	public ITextureRegion pinkFlashTR;
	public Font mBigFont;
	public Font mSmallFont;





	//metody wgrywania textur

	//splashScreen
	public synchronized void loadSplashResources(Engine pEngine, Context context){
		//ustalanie domyslnej sciezki
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// deklaracje atlasu i regionu w atlasie
		splashTA = new BitmapTextureAtlas(pEngine.getTextureManager(), 1024, 512);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTA, context, "splash.jpg", 10, 10);
		//wgranie atlasu do pamiêci
		splashTA.load();
		isSplashResourcesLoaded = true;

	}
	
	public synchronized void unloadSplashResources(){
		splashTA.unload();
		splashTR = null;
		isSplashResourcesLoaded = false;
	}

	//main manu
	public synchronized void loadMainMenuResources(Engine engine, Context context){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/MainMenuGfx/");
		mainMenuTA = new BitmapTextureAtlas(engine.getTextureManager(), 1024, 512);
		mainMenuBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTA, context, "menubackground.png", 10, 10);
		newGameButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTA, context, "newgamebutton.png", 820, 10);
		mainMenuTA.load();
		isMainMenuResourcesLoaded = true;

	}
	
	public synchronized void unloadMainMenuResources(){
		mainMenuTA.unload();
		mainMenuBackgroundTR = null;
		isMainMenuResourcesLoaded = false;
	}
	
	//office
	public synchronized void loadOfficeResources(Engine engine, Context context){
		
		//BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("fonts/");
		digitalFont = FontFactory.createFromAsset(engine.getFontManager(), engine.getTextureManager(), 256, 256, context.getAssets(), "fonts/digital.ttf", 48f, true, android.graphics.Color.BLACK);
		digitalFont.load();
		
		isOfficeResourcesLoaded = true;
	}
	
	public synchronized void unloadOfficeResources(){
		digitalFont.unload();
		isOfficeResourcesLoaded = false;
	}

	//race
	public synchronized void loadRaceResources(Engine engine, Context context){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Race/");
		raceTA = new BitmapTextureAtlas(engine.getTextureManager(), 1024, 1024);
		raceBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(raceTA, context, "racebackground.png", 10, 10);
		whiteCarTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(raceTA, context, "whitecar.png", 820, 10);
		maluchTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(raceTA, context, "maluch.png", 820, 150);

		raceTA.load();

		//fonts
		mBigFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48);
		mSmallFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 16);
		mBigFont.load();
		mSmallFont.load();
		
		isRaceResourcesLoaded = true;
	}
	
	public synchronized void unloadRaceResources(){
		raceTA.unload();
		raceBackgroundTR = null;
		whiteCarTR = null;
		maluchTR = null;
		mBigFont.unload();
		mSmallFont.unload();
		
		isRaceResourcesLoaded = false;
		
	}
	
	
	//talkscene
	public synchronized void loadTalkResources(Engine engine, Context context){

		//textures
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Talk/");
		talkTA = new BitmapTextureAtlas(engine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		notesTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(talkTA, context, "notes.png", 10, 10);
		playerPowerTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(talkTA, context, "playerpowerbackground.png", 10, 500);
		opponentPowerTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(talkTA, context, "opponentpowerbackground.png", 10, 600);
		pinkFlashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(talkTA, context, "pinkflash.png", 900, 10);

		talkTA.load();

		//fonts
		mBigFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48);
		mSmallFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 16);
		mBigFont.load();
		mSmallFont.load();
		
		isTalkResourcesLoaded = true;

	}
	
	public synchronized void unloadTalkResources(){
		talkTA.unload();
		notesTR = null;
		playerPowerTR = null;
		opponentPowerTR = null;
		pinkFlashTR = null;
		mBigFont.unload();
		mSmallFont.unload();
		
		isTalkResourcesLoaded = false;
	}
	
}
