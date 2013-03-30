package topplintowers.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import topplintowers.MainActivity;
import topplintowers.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;

import android.app.Activity;

public abstract class BaseScene extends Scene
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    protected Engine engine;
    protected MainActivity activity;
    protected ResourceManager resourceManager;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;
    
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    
    public BaseScene()
    {
        this.resourceManager = ResourceManager.getInstance();
        this.engine = resourceManager.mEngine;
        this.activity = resourceManager.mActivity;
        this.vbom = resourceManager.vbom;
        this.camera = resourceManager.mCamera;
        createScene();
    }
    
    //---------------------------------------------
    // ABSTRACTION
    //---------------------------------------------
    
    public abstract void createScene();
    public abstract void onBackKeyPressed();   
    public abstract SceneType getSceneType();  
    public abstract void disposeScene();
}

