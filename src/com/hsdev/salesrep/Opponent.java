package com.hsdev.salesrep;

import java.util.Random;
import java.util.Timer;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.graphics.Color;


public class Opponent extends Sprite {

	// ===========================================================
	// Constants
	// ===========================================================
	public int hp;
	private int minDamage;
	private int maxDamage;
	private int damModificator;
	private boolean isAvailable = true;
	public boolean attacked = false;

	// ===========================================================
	// Fields
	// ===========================================================

	GameManager gm = GameManager.getInstance();
	Engine engine;
	Text hpText;
	Text powerNameText;
	Text damageText;
	Random generator = new Random();


	// ===========================================================
	// Constructors
	// ===========================================================


	public Opponent(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion, Engine engine) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, engine.getVertexBufferObjectManager());


		this.engine = engine;

		hp = generator.nextInt(10) + 1;
		hpText = new Text(50, 40, ResourceManager.getIstance().mBigFont, Integer.toString(hp), 10, engine.getVertexBufferObjectManager());
		this.attachChild(hpText);

		maxDamage = generator.nextInt(2)+1;
		damageText = new Text(150, 30, ResourceManager.getIstance().mSmallFont, "Damage: " + Integer.toString(maxDamage), 10, engine.getVertexBufferObjectManager());
		this.attachChild(damageText);



	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================


	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){

		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){

			makePowerFlash(gm.clickedPower);
			int damage = gm.getDamage();			
			gm.actionPoints = gm.actionPoints - gm.actionPointsUsed;
			if(gm.actionPoints == 0){
				gm.playersTurn = false;
			}

			if(damage > -1){
				hp = hp - damage;
				hpText.setText(Integer.toString(hp));
			}
			
			for(int i = 0; i < gm.powersList.size(); i++){
				gm.powersList.get(i).isClicked = false;
				gm.powersList.get(i).setColor(Color.WHITE);
			}
			//what to do with opponent power on kill
			if(hp < 1){
				isAvailable = false;
				Path path = new Path(2).to(this.getX(), this.getY()).to(this.getX() + 300, this.getY());
				this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				this.registerEntityModifier(new PathModifier(1.0f, path));
				hpText.registerEntityModifier(new FadeOutModifier(0.5f));
				damageText.registerEntityModifier(new FadeOutModifier(0.5f));
				this.registerEntityModifier(new FadeOutModifier(0.5f));
			}
			//opponent attack when action points are 0
			if(gm.actionPoints == 0 && gm.playersTurn == false){

				opponentAttack();
				gm.playersTurn = true;

			}


		}

		return true;

	}




	// ===========================================================
	// Methods
	// ===========================================================

	public void opponentAttack(){


		this.registerUpdateHandler(new TimerHandler(2f, true, new ITimerCallback() {
			int i = 0;
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				
				if (i < gm.opponentList.size()) {
					while (gm.opponentList.get(i).isAvailable == false
							|| gm.opponentList.get(i).attacked == true) {
						i++;
					}
					if (gm.opponentList.get(i).isAvailable == true
							&& gm.opponentList.get(i).attacked == false) {
						gm.negotiationStrength = gm.negotiationStrength
								- gm.opponentList.get(i).dealDamage();
						i++;
					}
					if (gm.negotiationStrength < 1 && i == gm.opponentList.size() - 1) {
						gm.negotiationEnded = true;
						gm.playersTurn = false;
					}
				}else{
					
					pTimerHandler.setAutoReset(false);
					
				}
							
			}
		}));
		
		gm.nextRound();

	}

	public int dealDamage(){
		
		this.attacked = true;

		Path attackPath  = new Path(3).to(this.getX(), this.getY()).to(this.getX() - 50, this.getY()).to(this.getX(), this.getY());
		this.registerEntityModifier(new PathModifier(1.0f, attackPath));

		if(hp < 1)
			return 0;

		return maxDamage;

	}

	public void makePowerFlash(PlayerPower playerPower){

		Path flashPath = new Path(2).to(-20, 40).to(320, 40);
		Sprite flash = new Sprite(-200, 40, 40, 80, ResourceManager.getIstance().pinkFlashTR, engine.getVertexBufferObjectManager());
		flash.registerEntityModifier(new PathModifier(0.7f, flashPath));
		playerPower.attachChild(flash);

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================



}
