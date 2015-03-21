/**
 * 
 */
package com.hsdev.salesrep;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.graphics.Color;


/**
 * @author user
 *
 */
public class PlayerPower extends Sprite {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	GameManager gm = GameManager.getInstance();
	Random randomGenerator = new Random();

	private Text actionpointsText;
	private Text powerNameText;
	private Text damageText;

	public String powerName;
	public boolean isActive;
	public int powerActionPoints;
	public int minDamage;
	public int maxDamage;
	public int minDamModificator;
	public int maxDamModificator;
	public boolean isClicked = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PlayerPower(float pX, float pY, ITextureRegion pTextureRegion, Engine engine, String powerName, boolean isActive, int actionPoints, int minDamage, int maxDamage) {
		super(pX, pY, 300, 80, pTextureRegion, engine.getVertexBufferObjectManager());

		this.powerName = powerName;
		this.isActive = isActive;
		this.powerActionPoints = actionPoints;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;

		actionpointsText = new Text(250, 40, ResourceManager.getIstance().mBigFont, Integer.toString(actionPoints), 10, engine.getVertexBufferObjectManager());
		this.attachChild(actionpointsText);
		powerNameText = new Text(150, 50, ResourceManager.getIstance().mSmallFont, powerName, engine.getVertexBufferObjectManager());
		this.attachChild(powerNameText);
		damageText = new Text(150, 30, ResourceManager.getIstance().mSmallFont, "Strenght: " + Integer.toString(minDamage) + " - " + Integer.toString(maxDamage), engine.getVertexBufferObjectManager());
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

			if(isClicked == false && gm.actionPoints > 0 && gm.actionPoints >= powerActionPoints && gm.playersTurn == true){
				int	damage = randomGenerator.nextInt(maxDamage - minDamage + 1) + minDamage;
				gm.setDamage(damage);
				gm.clickedPower = this;

				for(int i = 0; i < gm.powersList.size(); i++){
					gm.powersList.get(i).isClicked = false;
					gm.powersList.get(i).setColor(Color.WHITE);
				}
				gm.actionPointsUsed = powerActionPoints;
				this.setColor(Color.BLUE);
				isClicked = true;
				return true;
			}
			if(isClicked == true && gm.playersTurn == true){
				
				gm.clickedPower = null;
				gm.setDamage(0);
				this.setColor(Color.WHITE);
				isClicked = false;
				return true;
			}



		}

		return false;

	}



	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


}
