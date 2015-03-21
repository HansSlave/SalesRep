package com.hsdev.salesrep;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import android.app.Activity;

public class SceneManager {
	
	private Engine engine;
	private Activity activity;
	
	Scene splashScene, mainMenuscene, officeScene, raceScene, talkScene;
	
	
	//KONSTRUKTOR KLASY
	
		public SceneManager(Engine engine, Activity activity) {
			this.engine = engine;
			this.activity = activity;
			
		}
		
	
	//DEFINIOWANIE SYSTEMU WYBORU SCENY
		
	public enum AllScenes{
		SPLASH,
		MAINMENU, 
		OFFICE, 
		RACE, 
		TALK};
	
	private AllScenes currentScene;
	
	public AllScenes getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		
		this.currentScene = currentScene;
		switch (currentScene) {
		case MAINMENU:
			engine.setScene(mainMenuscene);
			break;
		
		case SPLASH:
			engine.setScene(splashScene);
			break;
			
		case OFFICE:
			engine.setScene(officeScene);
			break;
			
		case RACE:
			engine.setScene(raceScene);
			break;
			
		case TALK:
			engine.setScene(talkScene);
			break;

		default:
			break;
		}
	}

	//METODY TWORZACE KOLEJNE SCENY
	
	public Scene createSplashScene(){
		
		splashScene = new SplashScene(engine);
		return splashScene;
	}
	
	public Scene createMainMenuScene(){
		if(ResourceManager.getIstance().isSplashResourcesLoaded == true){
			ResourceManager.getIstance().unloadSplashResources();
		}
		if(ResourceManager.getIstance().isRaceResourcesLoaded == true){
			ResourceManager.getIstance().unloadRaceResources();;
		}
		if(ResourceManager.getIstance().isTalkResourcesLoaded == true){
			ResourceManager.getIstance().unloadTalkResources();
		}
		if(ResourceManager.getIstance().isOfficeResourcesLoaded == true){
			ResourceManager.getIstance().unloadOfficeResources();
		}
		
		ResourceManager.getIstance().loadMainMenuResources(engine, activity);
		mainMenuscene = new MainMenuScene(engine, this);
		return mainMenuscene;
	}
	
	public Scene createOfficeScene(){
		if(ResourceManager.getIstance().isTalkResourcesLoaded == true){
			ResourceManager.getIstance().unloadTalkResources();
		}
		if(ResourceManager.getIstance().isMainMenuResourcesLoaded == true){
			ResourceManager.getIstance().unloadMainMenuResources();
		}
		if(ResourceManager.getIstance().isRaceResourcesLoaded == true){
			ResourceManager.getIstance().unloadRaceResources();;
		}
				
		ResourceManager.getIstance().loadOfficeResources(engine, activity);
		officeScene = new OfficeScene(engine, this);
		return officeScene;
	}
	
	public Scene createRaceScene(){
		if(ResourceManager.getIstance().isOfficeResourcesLoaded == true){
			ResourceManager.getIstance().unloadOfficeResources();
		}
		ResourceManager.getIstance().loadRaceResources(engine, activity);
		raceScene = new RaceScene(engine, this);
		return raceScene;
	}
	
	public Scene createTalkScene(){
		if(ResourceManager.getIstance().isRaceResourcesLoaded == true){
			ResourceManager.getIstance().unloadRaceResources();
		}
		ResourceManager.getIstance().loadTalkResources(engine, activity);
		talkScene = new TalkScene(engine, this);
		return talkScene;
	}
	

}
