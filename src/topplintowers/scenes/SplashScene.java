package topplintowers.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import topplintowers.resources.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;

public class SplashScene extends BaseScene {
	private Sprite mBackground;
	
	@Override
	public void createScene() {
		createBackground();
	}
	
	private void createBackground() {
		mBackground = new Sprite(0, 0, ResourceManager.mSplashTextureRegion, vbom) {
			@Override
		     protected void preDraw(GLState pGLState, Camera pCamera)
		     {
		            super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
		     }
		};
		attachChild(mBackground);
	}
	
	@Override
	public void onBackKeyPressed() { return; }
	
	@Override
	public void onMenuKeyPressed() { return; }

	@Override
	public SceneType getSceneType() { return SceneType.SPLASH; }

	@Override
	public void disposeScene() {
		mBackground.detachSelf();
		mBackground.dispose();
		
		this.detachSelf();
		this.dispose();
	}


}
