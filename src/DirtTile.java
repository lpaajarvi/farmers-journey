package fi.tuni.tiko.digging;

import static fi.tuni.tiko.digging.MainGame.dirtTextureTileset;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DirtTile extends GameTile implements Poolable {

  GameTexture dirtTexture = dirtTextureTileset[46];

  static GameTexture brokenTexture = new GameTexture(
    new Texture("dirtTileBroken.png")
  );

  static Texture dirtTileVanish = new Texture("dirtTileVanish.png");

  public DirtTile(int locY, int locX) {
    super(locY, locX);
    diggable = true;
    connectingTexture = true;
    tiling = true;

    setConcrete(true);

    setTexture(dirtTexture);

    rectangle = new Rectangle(locX, locY, 1.00f, 1.00f);

    setVanishAnimation(new SheetAnimation(dirtTileVanish, 1, 8, 5, 60));
  }

  @Override
  public void startVanishing(Stage currentStage) {
    super.startVanishing(currentStage);

    setTexture(brokenTexture);
    tiling = false;
    connectingTexture = false;

    updateTiles(locationY, locationX, currentStage);
  }

  public void setInPlace(int locY, int locX) {
    setLocationX(locX);
    setLocationY(locY);

    setX(locX);
    setY(locY);
  }

  @Override
  public void reset() {
    setLocationY(-1);
    setLocationX(-1);
    diggable = true;
    setConcrete(true);

    getVanishAnimation().resetAnimation();
    setVanishing(false);
    //rectangle out of sight too
    setX(-24);
    setY(-24);
    setOccupied(false);

    dirtTexture = dirtTextureTileset[46];
    setTexture(dirtTexture);
    connectingTexture = true;
    tiling = true;
    root = null;
  }

  @Override
  public void updateTexture(int tileNumber) {
    dirtTexture = dirtTextureTileset[tileNumber];
    setTexture(dirtTexture);
  }
}
