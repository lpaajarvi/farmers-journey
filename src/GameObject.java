package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {

  GameTexture gameTextureRegion;

  Rectangle rectangle;

  boolean concrete = true;

  public boolean isConcrete() {
    return concrete;
  }

  public void setConcrete(boolean concrete) {
    this.concrete = concrete;
  }

  public float getWidth() {
    return rectangle.width;
  }

  public float getHeight() {
    return rectangle.height;
  }

  public float getX() {
    return rectangle.x;
  }

  public void setX(float x) {
    rectangle.x = x;
  }

  public float getY() {
    return rectangle.y;
  }

  public void setY(float y) {
    rectangle.y = y;
  }

  public Texture getTexture() {
    return gameTextureRegion.getTexture();
  }

  public GameTexture getGameTextureRegion() {
    return gameTextureRegion;
  }

  public void setTexture(GameTexture gameTexture) {
    this.gameTextureRegion = gameTexture;
  }

  public Rectangle getRectangle() {
    return rectangle;
  }

  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  public void dispose() {
    getTexture().dispose();
  }

  public void draw(SpriteBatch batch) {
    batch.draw(gameTextureRegion, getX(), getY(), getWidth(), getHeight());
  }
}
