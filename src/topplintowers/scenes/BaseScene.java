package topplintowers.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import topplintowers.MainActivity;
import topplintowers.resources.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;

public abstract class BaseScene extends Scene
{    
    protected Engine engine;
    protected MainActivity activity;
    protected ResourceManager resourceManager;
    protected VertexBufferObjectManager vbom;
    protected SmoothCamera camera;
    
    public BaseScene()
    {
        this.resourceManager = ResourceManager.getInstance();
        this.engine = resourceManager.mEngine;
        this.activity = resourceManager.mActivity;
        this.vbom = resourceManager.vbom;
        this.camera = resourceManager.mCamera;
        createScene();
    }
    
    public abstract void createScene();
    public abstract void onBackKeyPressed();
    public abstract void onMenuKeyPressed();
    public abstract SceneType getSceneType();  
    public abstract void disposeScene();
}

