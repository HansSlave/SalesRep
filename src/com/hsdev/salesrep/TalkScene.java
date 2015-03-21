package com.hsdev.salesrep;


import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.color.Color;

import com.hsdev.salesrep.SceneManager.AllScenes;

public class TalkScene extends Scene implements IScrollDetectorListener, IOnSceneTouchListener{

	// ===========================================================
	// Constants
	// ===========================================================

	private static final float FREQ_D = 120.0f;
	private static final int STATE_WAIT = 0;
	private static final int STATE_SCROLLING = 1;
	private static final int STATE_MOMENTUM = 2;
	private static final int STATE_DISABLE = 3;
	private static final float MAX_ACCEL = 5000;
	private static final double FRICTION = 0.96f;
	private int mState = STATE_DISABLE;
	private float mBottomYOffset = 70;
	private float mUpperYOffset = 320;
	private int rectangleAmount = 0;



	// ===========================================================
	// Fields
	// ===========================================================	

	Engine engine;
	GameManager gm = GameManager.getInstance();
	SceneManager sceneManager;
	private SurfaceScrollDetector scrollDetector;
	private IEntity mWrapper;
	private TimerHandler thandle;

	public Text actionPointsText;

	private double accel, accel1, accel0;
	private static  int WRAPPER_HEIGHT;
	private long t0;
	private float mCurrentY;

	private int actionPoints;
	private int negotiationsHP;
	public  Text negotiationsText;
	

	// ===========================================================
	// Constructors
	// ===========================================================

	public TalkScene(Engine engine, SceneManager sceneManager) {

		this.engine = engine;
		this.actionPoints = gm.actionPoints;
		this.negotiationsHP = gm.negotiationStrength;
		this.sceneManager = sceneManager;
		
		/*
		Scene is reseted here
		*/
		gm.setTalkScene(engine);
		
		thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				doSetPos();

			}
		});
		this.setBackground(new Background(Color.BLACK));

		scrollDetector = new SurfaceScrollDetector(this);
		this.setOnSceneTouchListener(this);

		//how big wrapper should be
		for(int y = 0; y < gm.powersList.size(); y++){
			if(gm.powersList.get(y).isActive == true){
				rectangleAmount++;
			}
		}

		WRAPPER_HEIGHT = (rectangleAmount - 1) * 80 + 90;

		mWrapper = new Entity(150, mBottomYOffset + 120, 200, WRAPPER_HEIGHT);

		//attaching powers to wrapper
		int y = 0;
		for(int i = 0; i < gm.powersList.size(); i++){

			if(gm.powersList.get(i).isActive == true){
				mWrapper.attachChild(gm.powersList.get(i));
				gm.powersList.get(i).setY(WRAPPER_HEIGHT - (y * 80) - 40);
				this.registerTouchArea(gm.powersList.get(i));
				y++;
			}

		}

		//controlling action points

		actionPointsText = new Text(150, 430, ResourceManager.getIstance().mBigFont, Integer.toString(actionPoints), 3, this.engine.getVertexBufferObjectManager());

		// controlling negotiations strenght;

		negotiationsText = new Text(400, 430, ResourceManager.getIstance().mBigFont, Integer.toString(negotiationsHP), 3, this.engine.getVertexBufferObjectManager());



		//creating notes
		Sprite notes = new Sprite(400, 240, ResourceManager.getIstance().notesTR, this.engine.getVertexBufferObjectManager());



		//completing scene
		this.setBackground(new Background(Color.GREEN));
		this.setOnSceneTouchListener(getOnSceneTouchListener());
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.attachChild(mWrapper);
		this.attachChild(notes); 
		createOpponents();
		this.attachChild(actionPointsText);
		this.attachChild(negotiationsText);

		//this.attachChild(opponent1);
		this.registerUpdateHandler(thandle);
		mState = STATE_WAIT;


	}


	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		super.onManagedUpdate(pSecondsElapsed);

		actionPointsText.setText(Integer.toString(gm.actionPoints));
		negotiationsText.setText(Integer.toString(gm.negotiationStrength));
		
		if(GameManager.getInstance().negotiationEnded == true){
			GameManager.getInstance().negotiationEnded = false;
			changeScene();
		}		
	}

	/*
	Controlling scroll effect
	 */
	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {

		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;

	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		long dt = System.currentTimeMillis() - t0;
		if(dt == 0)
			return;
		double s = pDistanceY / (double)dt * 1000.0; // pixel/second
		accel = (accel0 + accel1 + s) / 3;
		accel0 = accel1;
		accel1 = accel;

		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;

	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mState = STATE_MOMENTUM;

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(mState == STATE_DISABLE){
			return true;
		}
		if(mState == STATE_MOMENTUM){
			accel0 = accel1 = accel = 0;
			mState = STATE_WAIT;
		}

		scrollDetector.onTouchEvent(pSceneTouchEvent);
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected synchronized void doSetPos(){

		if (accel == 0) {
			return;
		}

		if (mCurrentY > (WRAPPER_HEIGHT / 2) - mUpperYOffset ) {
			mCurrentY = (WRAPPER_HEIGHT / 2) - mUpperYOffset;
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
		}
		if (mCurrentY < (-WRAPPER_HEIGHT / 2 ))  {
			mCurrentY = (-WRAPPER_HEIGHT / 2 ) ;
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
		}
		//mWrapper.setPosition(0, -mCurrentY);
		mWrapper.setY(-mCurrentY + mBottomYOffset);



		if (accel < 0 && accel < -MAX_ACCEL)
			accel0 = accel1 = accel = - MAX_ACCEL;
		if (accel > 0 && accel > MAX_ACCEL)
			accel0 = accel1 = accel = MAX_ACCEL;

		double ny = accel / FREQ_D;
		if (ny >= -1 && ny <= 1) {
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
			return;
		}
		if (! (Double.isNaN(ny) || Double.isInfinite(ny)))
			mCurrentY += ny;
		accel = (accel * FRICTION);
	}

	private void createOpponents(){
		gm.clearOpoonentList();

		for(int i = 0; i < gm.getDifficulty(); i++)
		{
			Opponent opponent = new Opponent(650, 420 - (i * 90), 300, 80, ResourceManager.getIstance().opponentPowerTR, this.engine); 
			this.registerTouchArea(opponent);
			this.attachChild(opponent);
			gm.addToOpponentList(opponent);

		}
	}
	
	public void changeScene(){
		sceneManager.createOfficeScene();
		sceneManager.setCurrentScene(AllScenes.OFFICE);
	}



	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


}


