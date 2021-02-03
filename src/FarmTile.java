package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

public class FarmTile extends GameTile implements Poolable {

  static GameTexture farmTexture = new GameTexture(new Texture("farmTile.png"));

  static GameTexture stoneTexture = new GameTexture(
    new Texture("permanentTile.png")
  );

  public FarmTile(int locY, int locX) {
    super(locY, locX);
    diggable = false;

    setConcrete(false);

    setTexture(farmTexture);

    rectangle = new Rectangle(locX, locY, 1.00f, 1.00f);
  }

  @Override
  public void vanish() {}

  //this method will be used after the tile is taken from pool
  public void setInPlace(int locY, int locX) {
    setLocationX(locX);
    setLocationY(locY);

    setX(locX);
    setY(locY);
  }

  public void setStoneFarmInPlace(int locY, int locX) {
    setTexture(stoneTexture);

    setLocationX(locX);
    setLocationY(locY);

    setX(locX);
    setY(locY);
  }

  @Override
  public void reset() {
    setTexture(farmTexture);

    setOccupied(false);
    root = null;
  }
}
