package gdx.pickaxe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ItemsWithPickaxe extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    ShapeRenderer SR;
    Sprite pickaxe;
    BitmapFont font;
    float fPlayX, fPlayY, fHitRadX, fHitRadY, fRad, fMouseY, fCurMouseX, fCurMouseY;
    int nIconHit = 0, nGrowth = 0;
    boolean isRadHit = false, isCollecting = false, isClicked;

    @Override
    public void create() {
        batch = new SpriteBatch();
        SR = new ShapeRenderer();
        pickaxe = new Sprite(new Texture(Gdx.files.internal("pickaxe.png")));
        fRad = 200;
        fPlayX = Gdx.graphics.getWidth() / 2 - 25;
        fPlayY = Gdx.graphics.getHeight() / 2 - 25;
        fHitRadX = (Gdx.graphics.getWidth() / 2) - (fRad / 2);
        fHitRadY = (Gdx.graphics.getHeight() / 2) - (fRad / 2);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fMouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        resource();
        player();
        movementAction();
        pickaxeIcon();
        RadToResource();
        if (isClicked == true) {
            fCurMouseX = Gdx.input.getX();
            fCurMouseY = Gdx.input.getY();
        }
    }

    public void player() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.WHITE);
        SR.rect(fPlayX, fPlayY, 50, 50);
        SR.end();
        SR.begin(ShapeType.Line);
        SR.setColor(Color.RED);
        SR.rect(fPlayX, fPlayY, 50, 50);
        SR.end();
    }

    public void movementAction() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            fPlayY += 2;
            fHitRadY += 2;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                fPlayX -= 2;
                fHitRadX -= 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                fPlayX += 2;
                fHitRadX += 2;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            fPlayY -= 2;
            fHitRadY -= 2;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                fPlayX -= 2;
                fHitRadX -= 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                fPlayX += 2;
                fHitRadX += 2;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fPlayX -= 2;
            fHitRadX -= 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fPlayX += 2;
            fHitRadX += 2;
        }
        if (nIconHit == 1) {
            if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                nIconHit = 0;
                isCollecting = false;
                isRadHit = false;
                nGrowth = 0;
                Gdx.input.setCursorCatched(false);
                Gdx.input.setCursorPosition((int) fCurMouseX,(int) fCurMouseY);
            }
        }
    }

    public void hitradius() {
        SR.begin(ShapeType.Line);
        SR.setColor(Color.CYAN);
        SR.ellipse(fHitRadX, fHitRadY, fRad, fRad);
        SR.end();
    }

    public void pickaxeIcon() {
        if (nIconHit == 0) {
            SR.begin(ShapeType.Line);
            SR.setColor(Color.VIOLET);
            SR.rect(Gdx.graphics.getWidth() - 65, 0, 65, 65);
            SR.end();
            SR.begin(ShapeType.Filled);
            SR.setColor(Color.WHITE);
            SR.rect(Gdx.graphics.getWidth() - 65, 0, 65, 65);
            SR.end();
            batch.begin();
            batch.draw(pickaxe, Gdx.graphics.getWidth() - (pickaxe.getWidth() / 9), 10, pickaxe.getOriginX(), pickaxe.getOriginY(), pickaxe.getWidth() / 10, pickaxe.getHeight() / 10, pickaxe.getScaleX(), pickaxe.getScaleY(), 0);
            batch.end();
        } else if (nIconHit == 1) {
            batch.begin();
            batch.draw(pickaxe, Gdx.input.getX() - (pickaxe.getWidth() / 20), fMouseY - (pickaxe.getHeight() / 20), pickaxe.getOriginX(), pickaxe.getOriginY(), pickaxe.getWidth() / 10, pickaxe.getHeight() / 10, pickaxe.getScaleX(), pickaxe.getScaleY(), 0);
            batch.end();
            hitradius();
            isClicked = true;
            Gdx.input.setCursorCatched(true);
        }
    }

    public void resource() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.GRAY);
        SR.rect(500, 50, 75, 350);
        SR.end();
        if (isCollecting == true && isRadHit == true) {
            SR.begin(ShapeType.Filled);
            SR.setColor(Color.YELLOW);
            SR.rect(490, 25, nGrowth, 15);
            SR.end();
            nGrowth++;
            if (nGrowth == 100) {
                nGrowth = 0;
            }
        }
    }

    public void RadToResource() {
        if (nIconHit == 1) {
            if (fHitRadX + fRad >= 500 && fHitRadX <= 575 && fHitRadY + fRad >= 50 && fHitRadY <= 400) {
                isRadHit = true;
            } else {
                isRadHit = false;
                isCollecting = false;
                nGrowth = 0;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        if (nIconHit == 0) {
            if (Gdx.input.getX() >= (Gdx.graphics.getWidth() - 65)
                    && Gdx.input.getX() <= Gdx.graphics.getWidth()
                    && fMouseY >= 0
                    && fMouseY <= 65) {
                nIconHit = 1;
            }
        } else if (isRadHit == true) {
            if (Gdx.input.getX() >= 500
                    && Gdx.input.getX() <= 575
                    && fMouseY >= 50) {
                isCollecting = true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
