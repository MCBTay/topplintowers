package topplintowers.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;

import topplintowers.MainActivity;
import topplintowers.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;

import com.badlogic.gdx.math.Vector2;
import com.topplintowers.R;

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
	public SceneType getSceneType() { return SceneType.SCENE_SPLASH; }

	@Override
	public void disposeScene() {
		mBackground.detachSelf();
		mBackground.dispose();
		
		this.detachSelf();
		this.dispose();
	}
}
