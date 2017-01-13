package gdx.aiimproved;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.Random;

public class AIimproved extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    ShapeRenderer SR;
    Random random;
    BitmapFont font;
    float fUserX, fUserY, fUserWidth, fUserHeight, fEnX, fEnY;
    int nEnHealth, nUserHealth, nHitDelay = 30, nMoney = 50, nEnSpeed, nThrowOffbar, nBarDelay, nDir = 0;
    boolean[] actions = new boolean[2];
    boolean isNear = false, canAttack = false, isHit = false, isEaten = false;
    String sMoney;

    @Override
    public void create() {
        batch = new SpriteBatch();
        SR = new ShapeRenderer();
        font = new BitmapFont();
        random = new Random();
        fUserWidth = 50;
        fUserHeight = fUserWidth;
        fUserX = Gdx.graphics.getWidth() / 2 - (fUserWidth / 2);
        fUserY = Gdx.graphics.getHeight() / 2 - (fUserHeight / 2);
        fEnX = 0;
        fEnY = 0;
        nEnSpeed = 3;
        nEnHealth = 50;
        nBarDelay = 10;
        nThrowOffbar = Gdx.graphics.getWidth() / 2;
        nUserHealth = Gdx.graphics.getWidth();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player();
        actionradius();
        enemy();
        money();
        attack();
        playerHealth();
        ThrowOff();
        hit();
        // System.out.println(canAttack);
    }

    public void player() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.CYAN);
        SR.rect(fUserX, fUserY, fUserWidth, fUserHeight);
        SR.end();
        playermovement();
    }

    public void playerHealth() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.RED);
        SR.rect(0, Gdx.graphics.getHeight() - 30, nUserHealth, 30);
        SR.end();
    }

    public void playermovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            fUserY += 2;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                fUserX -= 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                fUserX += 2;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            fUserY -= 2;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                fUserX -= 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                fUserX += 2;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fUserX -= 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fUserX += 2;
        }
    }

    public void hit() {
        if (fUserX + 50 >= fEnX && fUserX <= fEnX + 50
                && fUserY + 50 >= fEnY && fUserY <= fEnY + 50) {
            isHit = true;
            isEaten = true;
        }
        if (isHit == true) {
            if (nHitDelay == 30) {
                nUserHealth -= 25;
            }
            nHitDelay--;
        }
        if (nHitDelay <= 0) {
            isHit = false;
            nHitDelay = 30;
        }
    }

    public void ThrowOff() {
        if (isEaten == true) {
            ThrowOffBar();
            if (nBarDelay <= 0) {
                nThrowOffbar -= 15;
                nBarDelay = 10;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                nThrowOffbar += 25;
            }
            if (nThrowOffbar >= Gdx.graphics.getWidth()) {
                isNear = false;
                nDir = random.nextInt(5) * 1;
                if (nDir == 1) {
                    fEnX -= 200;
                } else if (nDir == 2) {
                    fEnX += 200;
                } else if (nDir == 3) {
                    fEnY -= 200;
                } else if (nDir == 4) {
                    fEnY += 200;
                }
                isEaten = false;
            } else if (nThrowOffbar <= 0) {
                fUserX = Gdx.graphics.getWidth() / 2 - (fUserWidth / 2);
                fUserY = Gdx.graphics.getHeight() / 2 - (fUserHeight / 2);
                fEnX = 0;
                fEnY = 0;
                nMoney--;
                isNear = false;
                isEaten = false;
                nUserHealth = Gdx.graphics.getWidth();
            }
            nBarDelay--;
        } else if (isEaten == false) {
            nThrowOffbar = Gdx.graphics.getWidth() / 2;
        }
    }

    public void ThrowOffBar() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.YELLOW);
        SR.rect(0, 0, nThrowOffbar, 30);
        SR.end();
    }

    public void actions() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            actions[0] = true;
            actions[1] = false;
            System.out.println("Attack");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            actions[0] = false;
            actions[1] = true;
            canAttack = false;
            System.out.println("Mining");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            actions[0] = false;
            actions[1] = false;
            canAttack = false;
        }
    }

    public void actionradius() {
        actions();
        if (actions[0] == true) {
            SR.begin(ShapeType.Line);
            SR.setColor(Color.RED);
            SR.ellipse(fUserX - 75, fUserY - 75, 200, 200);
            SR.end();
        } else if (actions[1] == true) {
            SR.begin(ShapeType.Line);
            SR.setColor(Color.DARK_GRAY);
            SR.ellipse(fUserX - 75, fUserY - 75, 200, 200);
            SR.end();
        }
    }

    public void enemy() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.BLACK);
        SR.rect(fEnX, fEnY, 50, 50);
        SR.end();
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.GREEN);
        SR.rect(fEnX, fEnY - 10, nEnHealth, 10);
        SR.end();
        enemyradius();
        enemymove();
        if (nEnHealth <= 0) {
            fEnX = 0;
            fEnY = 0;
            isNear = false;
            isEaten = false;
            nMoney++;
            nEnHealth = 50;
        } else if (nUserHealth <= 0) {
            fUserX = Gdx.graphics.getWidth() / 2 - (fUserWidth / 2);
            fUserY = Gdx.graphics.getHeight() / 2 - (fUserHeight / 2);
            fEnX = 0;
            fEnY = 0;
            nMoney--;
            isNear = false;
            isEaten = false;
            nUserHealth = Gdx.graphics.getWidth();
        }
    }

    public void enemyradius() {
        SR.begin(ShapeType.Line);
        SR.ellipse(fEnX - 75, fEnY - 75, 200, 200);
        SR.end();
    }

    public void near() {
        if (((fUserX >= fEnX - 75) || (fUserX + 50 >= fEnX - 75)) && fUserX <= fEnX + 125
                && ((fUserY >= fEnY - 75)||(fUserY + 50 >= fEnY - 75)) && fUserY <= fEnY + 125) {
            isNear = true;
        }
    }

    public void enemymove() {
        near();
        if (isNear == true) {
            if (fEnX > fUserX) {
                fEnX -= nEnSpeed;
            } else if (fEnX < fUserX) {
                fEnX += nEnSpeed;
            }
            if (fEnY > fUserY) {
                fEnY -= nEnSpeed;
            } else if (fEnY < fUserY) {
                fEnY += nEnSpeed;
            }
        }
    }

    public void attack() {
        if (actions[0] == true) {
            if (fEnX + 50 >= fUserX - 75 && fEnX <= fUserX + 125
                    && fEnY + 50 >= fUserY - 75 && fEnY <= fUserY + 125) {
                canAttack = true;
                //System.out.println("Yes");
            } else {
                canAttack = false;
                // System.out.println("No");
            }
        }
    }

    public void money() {
        sMoney = Integer.toString(nMoney);
        batch.begin();
        font.setColor(Color.BLACK);
        font.draw(batch, "$ " + sMoney, 0, (Gdx.graphics.getHeight() - 40));
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        SR.dispose();
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
        if (canAttack == true) {
            if (Gdx.input.getX() >= fEnX && Gdx.input.getX() <= fEnX + 50
                    && (Gdx.graphics.getHeight() - Gdx.input.getY()) >= fEnY
                    && (Gdx.graphics.getHeight() - Gdx.input.getY()) <= fEnY + 50) {
                nEnHealth -= 5;
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
