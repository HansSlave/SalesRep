package com.hsdev.salesrep;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import com.hsdev.salesrep.SceneManager.AllScenes;

public class MainMenuScene extends Scene  {
	
	Sprite background;
	Sprite newGameButton;
	
	
	public MainMenuScene(Engine engine, final SceneManager sceneManager) {
		
		background = new Sprite(400, 240, ResourceManager.getIstance().mainMenuBackgroundTR, engine.getVertexBufferObjectManager());
		newGameButton = new Sprite(400, 280, ResourceManager.getIstance().newGameButtonTR, engine.getVertexBufferObjectManager()){
		
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
					sceneManager.createOfficeScene();
					sceneManager.setCurrentScene(AllScenes.OFFICE);
				}
				return true;
			}
		};
		
		
		//skladanie sceny
		this.registerTouchArea(newGameButton);
		this.setOnSceneTouchListener(getOnSceneTouchListener());
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.attachChild(background);
		this.attachChild(newGameButton);
		
		
	}
	
	
	

}
