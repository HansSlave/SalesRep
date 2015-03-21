package com.hsdev.salesrep;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import com.hsdev.salesrep.SceneManager.AllScenes;


public class MainActivity extends BaseGameActivity {


	private Engine engine;
	private Camera camera;

	private SceneManager sceneManager;


	private float HEIGHT = 480f;
	private float WIDTH = 800f;

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		engine = new LimitedFPSEngine(pEngineOptions, 60);
		//engine = new FixedStepEngine(pEngineOptions, 60);
		return engine;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, WIDTH, HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new CropResolutionPolicy(WIDTH, HEIGHT), camera);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		return engineOptions;

	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
					throws IOException {

		sceneManager = new SceneManager(engine, this);
		ResourceManager.getIstance().loadSplashResources(engine, this);



		//ta linia oznacza zakoñczenie tworzenia sceny
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {

		//ta linia tworzy pierwsz¹ scenê (wywoluje metodê w SceneManager)
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.createSplashScene());

	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
					throws IOException {

		//rejestrowanie timera do zmiany w³aczenia sceny menu po up³ywie pewnego czasu
		engine.registerUpdateHandler(new TimerHandler(4f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {

				engine.unregisterUpdateHandler(pTimerHandler);


				//TODO tu powinny siê wgrywaæ resources dla main menu!
				// ResourceManager.getInstance().loadmainmenuresources() ???


				sceneManager.createMainMenuScene();
				sceneManager.setCurrentScene(AllScenes.MAINMENU);

			}
		}));


		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

}