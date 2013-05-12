/* Copyright 2012 Olie

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. 
   
   
   NOTE:  Some modifications made by Bryan Taylor 05.2013
*/

package topplintowers;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Slider extends Entity{

	private Sprite mSlider, mThumb;
	private float mValue, mHeight, mWidth;
	private OnSliderValueChangeListener mListener;
	private TextureRegion mSliderTextureRegion = ResourceManager.mSliderTextureRegion,
			              mSliderThumbTextureRegion = ResourceManager.mSliderThumbTextureRegion;
	
	public Slider(VertexBufferObjectManager vertexBufferObjectManager) {
		if(mSliderTextureRegion == null || mSliderThumbTextureRegion == null)
			throw new NullPointerException("Slider or thumb texture region cannot be null");
		mValue = 0.0f;
		mSlider = new Sprite(0, 0, mSliderTextureRegion, vertexBufferObjectManager);
		
		float mThumbX = -mSliderThumbTextureRegion.getWidth()/2;
		float mThumbY = -mSliderThumbTextureRegion.getHeight()/2 + mSliderTextureRegion.getHeight()/2;
		mThumb = new Sprite(mThumbX, mThumbY, mSliderThumbTextureRegion, vertexBufferObjectManager){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				//restrict the movement of the thumb only on the slider
				float newX = pSceneTouchEvent.getX() - this.getParent().getX();
				if( (newX < (mSlider.getWidth() + mSlider.getX())) && (newX > mSlider.getX()) ) {
					this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2 - this.getParent().getX(), this.getY());
					//find the fraction that the newX is at of the length of the slider
					mValue = (newX/mSlider.getWidth()) * 100;
					if(mListener != null)
						mListener.onSliderValueChanged(mValue);
					return true;
				}
				return false;
			}
			
		};
		mWidth = (mThumb.getWidth() >= mSlider.getWidth()) ? mThumb.getWidth() : mSlider.getWidth();
		mHeight = (mThumb.getHeight() >= mSlider.getHeight()) ? mThumb.getHeight() : mSlider.getHeight();
		this.attachChild(mSlider);
		this.attachChild(mThumb);
	}

	public Sprite getmThumb() 	{ return mThumb; }
	public float getWidth() 	{ return mWidth; }	
	public float getHeight() 	{ return mHeight; }
	public float getValue() 	{ return mValue; }
	
	public void setValue(float value) {
		if (value > 100) mValue = 100;
		else if (value < 0) mValue = 0;
		else mValue = value;
		
		float offset = (mValue * mSlider.getWidth()) / 100;
		
		float mThumbX = -mSliderThumbTextureRegion.getWidth()/2 + offset;
		float mThumbY = -mSliderThumbTextureRegion.getHeight()/2 + mSliderTextureRegion.getHeight()/2;
		mThumb.setPosition(mThumbX, mThumbY);
	}
	
	public void setOnSliderValueChangeListener(OnSliderValueChangeListener sliderValueChangeListener) {
		if(sliderValueChangeListener == null)
			throw new NullPointerException("OnSliderValueChangeListener cannot be null");
		this.mListener = sliderValueChangeListener;
	}
	
	public interface OnSliderValueChangeListener {
		public void onSliderValueChanged(float value);
	}
}