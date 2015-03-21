package com.hsdev.salesrep;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import com.hsdev.salesrep.SceneManager.AllScenes;

public class RaceScene extends Scene{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	Engine engine;
	SceneManager sceneManager;

	Sprite background;
	Sprite playerCar;

	private float time;
	Text timeText;

	Text difficultyText;


	private boolean leftPressed = false;
	private boolean rightPressed = false;


	// ===========================================================
	// Constructors
	// ===========================================================

	public RaceScene(Engine engine, final SceneManager sceneManager) {

		this.engine = engine;
		this.sceneManager = sceneManager;

		this.time = GameManager.getInstance().time;

		this.setBackground(new Background(Color.BLACK));
		
		//background
		background = new Sprite(400, 240, ResourceManager.getIstance().raceBackgroundTR, engine.getVertexBufferObjectManager());	

		//text on screen
		timeText = new Text(250, 430, ResourceManager.getIstance().mBigFont, "Time: " + String.format("%.0f", time), 10, engine.getVertexBufferObjectManager());
		difficultyText = new Text(550, 430, ResourceManager.getIstance().mBigFont, "Difficulty: " + Integer.toString(GameManager.getInstance().difficulty), 15, engine.getVertexBufferObjectManager());


		//players car
		playerCar = new Sprite(400, 60, 124, 111, ResourceManager.getIstance().whiteCarTR, engine.getVertexBufferObjectManager()){ //car = new Rectangle(400, 50, 100, 50, engine.getVertexBufferObjectManager())

			@Override
			protected void onManagedUpdate(float pSecondsElapsed){
				super.onManagedUpdate(pSecondsElapsed);
				if(leftPressed == true){
					playerCar.setPosition(playerCar.getX() - 4f, playerCar.getY());
				}
				if(rightPressed == true){
					playerCar.setPosition(playerCar.getX() + 4f, playerCar.getY());
				}

			}
		};

		playerCar.setZIndex(2);


		Rectangle leftbutton = new Rectangle(50, 240, 100, 480, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
					leftPressed = true;
					//rightPressed = false;
				}	
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					leftPressed = false;
				}
				return true;
			}


		};
		leftbutton.setColor(Color.RED);

		Rectangle rightbutton = new Rectangle(750, 240, 100, 480, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
					rightPressed = true;
					//leftPressed = false;
				}	
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
					rightPressed = false;
				}
				return true;
			}
		};
		rightbutton.setColor(Color.RED);


		this.registerTouchArea(rightbutton);
		this.registerTouchArea(leftbutton);
		this.registerUpdateHandler(timerHandler);
		this.setOnSceneTouchListener(getOnSceneTouchListener());
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.attachChild(background);
		this.attachChild(timeText);
		this.attachChild(difficultyText);
		this.attachChild(leftbutton);
		this.attachChild(rightbutton);
		this.attachChild(playerCar);






	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	float elapsedTime = 0;
	float seconds = 0;
	float appearTime = 3;
	float appearPlace = 0;
	float dissapearPlace = 0;
	float lastCarPos = 0;
	int appearPosInd = 0;

	Random generator = new Random();
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		super.onManagedUpdate(pSecondsElapsed);

		elapsedTime = elapsedTime + pSecondsElapsed;

		timeText.setText("Time: " + String.format("%.0f", time - elapsedTime));
		difficultyText.setText("Difficulty: " + Integer.toString(GameManager.getInstance().difficulty));

		seconds = seconds + pSecondsElapsed;



		if(seconds > appearTime){ 
			seconds = 0;
			//appearTime = (generator.nextInt(2)+1);
			appearTime = generator.nextFloat() + 0.5f;
			appearPosInd = generator.nextInt(3);

			switch (appearPosInd) {
			case 0:
				appearPlace = 380;
				dissapearPlace = 200;
				break;

			case 1:
				appearPlace = 400;
				dissapearPlace = 400;
				break;

			case 2:
				appearPlace = 420;
				dissapearPlace = 600;
				break;

			default:
				break;
			}
			lastCarPos = dissapearPlace;

			final Sprite maluch = new Sprite(appearPlace,  300, 10.7f, 9.3f, ResourceManager.getIstance().maluchTR, engine.getVertexBufferObjectManager()){ //final Rectangle othercar = new Rectangle(400, 315, 10, 5, engine.getVertexBufferObjectManager())
				boolean canCollide = true;;

				@Override
				protected void onManagedUpdate(float pSecondsElapsed){
					super.onManagedUpdate(pSecondsElapsed);

					//check if in front of player car
					if(this.getY() > 60){
						this.setZIndex(playerCar.getZIndex() - 1);
					}
					else{
						this.setZIndex(playerCar.getZIndex() + 1);
					}
					this.getParent().sortChildren();

					//check if collision

					if(this.collidesWith(playerCar) && this.getY() < 80 && this.getY() > 30){
						if(canCollide == true){
							GameManager.getInstance().difficulty++;
							canCollide = false;
							
							//check if difficulty is too high
							if(GameManager.getInstance().difficulty > 10){
								
								goToOffice();
								
							}

						}
						this.setColor(Color.RED);
					}

					if(!this.collidesWith(playerCar)){
						canCollide = true;;
					}

					//check if left the scene
					if(this.getY() < -20){
						destroyCar(this);
					}

				}
			};
			Path carpath = new Path(2).to(appearPlace, 300).to(dissapearPlace, -25);
			maluch.registerEntityModifier(new PathModifier(2.0f, carpath));
			ScaleModifier carScaleMod = new ScaleModifier(2.0f, 1f, 13f);
			maluch.registerEntityModifier(carScaleMod);
			this.attachChild(maluch);



		}

	}

	TimerHandler timerHandler = new TimerHandler(GameManager.getInstance().time, false,  new ITimerCallback() {

		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {

			sceneManager.createTalkScene();
			sceneManager.setCurrentScene(AllScenes.TALK);
		}
	});

	// ===========================================================
	// Methods
	// ===========================================================

	public void destroyCar(final Sprite car){
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				car.detachSelf();
				car.dispose();


			}
		});
	}
	
	public void goToOffice(){
		
		this.registerUpdateHandler(new TimerHandler(2.0f, false, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				sceneManager.createOfficeScene();
				sceneManager.setCurrentScene(AllScenes.OFFICE);	
				
			}
		}));
	}
	
	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
