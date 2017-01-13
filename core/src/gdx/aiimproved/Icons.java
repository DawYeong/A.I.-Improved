/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.aiimproved;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 *
 * @author benny
 */
public final class Icons {
    
    SpriteBatch batch = new SpriteBatch();
    Sprite icon;
    ShapeRenderer SR = new ShapeRenderer();
    String sName;
    float fX, fY, fWidth, fHeight;
    
    public Icons(String sName, float fX, float fY, float fWidth, float fHeight) {
        this.sName = sName;
        this.icon = new  Sprite(new Texture(Gdx.files.internal(sName)));
        this.fX = fX;
        this.fY = fY;
        this.fWidth = fWidth;
        this.fHeight = fHeight;
        icons();
        dispose();
    }
    void icons() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.WHITE);
        SR.rect(fX, fY, fWidth, fHeight);
        SR.end();
        batch.begin();
        batch.draw(icon, fX, fY, fWidth, fHeight);
        batch.end();
    }
    public void dispose() {
        batch.dispose();
        SR.dispose();
    }
}
