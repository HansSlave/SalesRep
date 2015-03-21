package com.hsdev.salesrep;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

public class SplashScene extends Scene {
	
	private Sprite splashScreen;
	
	public SplashScene(Engine engine) {
		
		this.setBackground(new Background(Color.BLACK));
		
		splashScreen = new Sprite(400, 240, 800, 480, ResourceManager.getIstance().splashTR, engine.getVertexBufferObjectManager());
				
		this.attachChild(splashScreen);
		
	}

}
