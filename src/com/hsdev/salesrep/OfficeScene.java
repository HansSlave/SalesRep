package com.hsdev.salesrep;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.text.format.Time;

import com.hsdev.salesrep.SceneManager.AllScenes;

public class OfficeScene extends Scene{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	Engine engine;
	SceneManager sceneManager;
	Random generator = new Random();
	
	Text difficultyText;
	Text timeToClientText;
	Text moneyToEarnText;
	
	int time;

	boolean phoneVisible = false;
	boolean goButtonPressed = false;

	// ===========================================================
	// Constructors
	// ===========================================================


	public OfficeScene(Engine engine, final SceneManager sceneManager) {

		this.engine = engine;
		this.sceneManager = sceneManager;

		this.setBackground(new Background(Color.BLUE));

		Rectangle button = new Rectangle(400, 240, 100, 100, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){

					showMiniGame();
					phoneVisible = true;

				}
				return true;
			}

		};
		button.setColor(Color.YELLOW);


		this.registerTouchArea(button);
		this.setOnSceneTouchListener(getOnSceneTouchListener());
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.attachChild(button);

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	float deltaTime = 0.5f;
	float seconds = 0;


	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		super.onManagedUpdate(pSecondsElapsed);

		if (phoneVisible == true && goButtonPressed == false) {

			seconds = seconds + pSecondsElapsed;

			if (seconds > deltaTime) {
				
				seconds = 0;
				
				GameManager.getInstance().difficulty = generator.nextInt(4)+1;
				time = generator.nextInt(50) + 10;
				GameManager.getInstance().time = time;
				GameManager.getInstance().negotiatedMoney = (generator.nextInt(100) + 1) * 100;
				
				difficultyText.setText(Integer.toString(GameManager.getInstance().difficulty));
				timeToClientText.setText(Integer.toString(time));
				moneyToEarnText.setText((Integer.toString(GameManager.getInstance().negotiatedMoney) + "$"));
				
			}
		}

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void showMiniGame(){

		Rectangle phone = new Rectangle(400, 240, 400, 400, engine.getVertexBufferObjectManager());
		
		Sprite greenButton = new Sprite(300, 100, ResourceManager.getIstance().greenButton, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
				
				if(pSceneTouchEvent.isActionDown() == true){
					goButtonPressed = true;
					this.registerUpdateHandler(new TimerHandler(2, false, new ITimerCallback() {
						
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							sceneManager.createRaceScene();
							sceneManager.setCurrentScene(AllScenes.RACE);
							
						}
					}));
					
				}
				
				return true;				
			}
		};
		
		this.registerTouchArea(greenButton);
		this.attachChild(phone);
		phone.attachChild(greenButton);

		difficultyText = new Text(100, 350, ResourceManager.getIstance().digitalFont, "0", 3, engine.getVertexBufferObjectManager());
		phone.attachChild(difficultyText);

		timeToClientText = new Text(200, 350, ResourceManager.getIstance().digitalFont, "0", 3, engine.getVertexBufferObjectManager());
		phone.attachChild(timeToClientText);

		moneyToEarnText = new Text(300, 350, ResourceManager.getIstance().digitalFont, "0", 7, engine.getVertexBufferObjectManager());
		phone.attachChild(moneyToEarnText);





	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
