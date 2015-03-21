package com.hsdev.salesrep;

import java.util.ArrayList;
import java.util.List;
import org.andengine.engine.Engine;
import org.andengine.util.adt.color.Color;


public class GameManager {
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static GameManager INSTANCE;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	
	//general game values
	public static int money;
	public static int turn;
	public boolean playersTurn = true;
	public int difficulty = 1;
	
	//race scene
	public float time;
		
	//talk scene values
	public int negotiatedMoney;
	public int actionPoints;
	public int actionPointsUsed;
	public int negotiationStrength;
	public int rounds;
	public PlayerPower clickedPower;
	public boolean negotiationEnded = false;
	
	
		
	

	//player power values
	public int damage;
	//public int actionpoints;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	private GameManager() {
		
		
	}
	
	public static GameManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new GameManager();
		}
		return INSTANCE;
	}
	
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void setDamage(int damage){
		this.damage = damage;
	}
	
	public int getDamage(){
		return this.damage;
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
		
	/*
	Managing power list
	*/
	
	public List<PlayerPower> powersList = new ArrayList<PlayerPower>();
	
	public void createPowersList(Engine engine){
		
		clearPlayersList();
		
		// Parameters: float pX, float pY, ITextureRegion pTextureRegion, Engine engine, String powerName, boolean isActive, int actionPoints, int minDamage, int maxDamage
		
		PlayerPower hypnotize = new PlayerPower(50, 0, ResourceManager.getIstance().playerPowerTR, engine, "Hypnotize", true, 3, 2, 4);
		powersList.add(hypnotize);
		
		PlayerPower reversePsychology = new PlayerPower(50, 0, ResourceManager.getIstance().playerPowerTR, engine, "Reverse psychology", true, 4, 1, 5);
		powersList.add(reversePsychology);
		
		PlayerPower begging = new PlayerPower(50, 0, ResourceManager.getIstance().playerPowerTR, engine, "Begging", true, 1, 0, 1);
		powersList.add(begging);
		
		PlayerPower references = new PlayerPower(50, 0, ResourceManager.getIstance().playerPowerTR, engine, "Good references", true, 2, 1, 3);
		powersList.add(references);
		
		PlayerPower certificate = new PlayerPower(50, 0, ResourceManager.getIstance().playerPowerTR, engine, "Important certificate", true, 3, 2, 4);
		powersList.add(certificate);
		
	}
	
	public void clearPlayersList(){
		powersList.clear();
	}
	
	/*
	Managing opponent list
	*/
	public List<Opponent> opponentList = new ArrayList<Opponent>();
	
	public void clearOpoonentList(){
	
		opponentList.clear();
				
	}
	
	public void addToOpponentList(Opponent opponent){
		
		opponentList.add(opponent);
		
	}
	
	
	public void nextRound(){
		for(int i = 0; i < opponentList.size(); i++){
			opponentList.get(i).setColor(Color.WHITE);	
			opponentList.get(i).attacked = false;
		}
		playersTurn = true;
		actionPoints = 10;
		
	}
	
	public void setTalkScene(Engine engine){
		clearPlayersList();
		createPowersList(engine);
		negotiationStrength = 10;
		actionPoints = 10;
		playersTurn = true;
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	
}
