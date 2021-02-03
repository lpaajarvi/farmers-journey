package fi.tuni.tiko.digging;

/*I hope I don't cause too many problems but instead of creating another abstract sub-class from
  tileBasedObject that implements Hazard, I'm going to use an existing one even though this doesn't
  actually walk, because it is a hazard that does everything else implemented by HazardousWalker/WalkingCreature.

 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class FallingTrap extends HazardousWalker implements Poolable {

  private boolean noticed = false;

  //strength related to other hazards: 2 will get destroy strength 1 hazards without getting destroyed from that impact alone
  //they will get destroyed in contact with other strength 2:s (spikes, other falling traps)
  int hazardStrength = 2;

  private boolean getsDestroyedByFallingPlayer = true;

  static GameTexture fallingTrapNoticed =
    (new GameTexture(new Texture("fallingTrapNoticed.png")));
  static GameTexture standTexture =
    (new GameTexture(new Texture("fallingTrap.png")));
  static Texture fallingTrapVanish = new Texture("fallingTIleVanishing.png");
  static Texture fallingTrapTrigger = new Texture("fallingTrapTriggering.png");
  static Texture fallingTrapFalling = new Texture("fallingTileFalling.png");

  public FallingTrap(int tilePosY, int tilePosX) {
    setStandTexture(standTexture);
    setConcrete(true);
    setWalkingSpeed(0f);
    setOriginalFallingSpeed(3.9f);

    setVanishAnimation(new SheetAnimation(fallingTrapVanish, 1, 8, 8, 60));
    setTriggerAnimation(new SheetAnimation(fallingTrapTrigger, 1, 8, 8, 60));
    setFallAnimation(new SheetAnimation(fallingTrapFalling, 1, 8, 8, 60));

    rectangle = new Rectangle(1.00f, 1.00f, 0.85f, 1.00f);
    putInTilePos(tilePosY, tilePosX);
  }

  @Override
  public void reset() {
    setConcrete(true);
    setWalkingSpeed(0f);
    setOriginalFallingSpeed(3.9f);

    getTriggerAnimation().resetAnimation();
    getFallAnimation().resetAnimation();
    getVanishAnimation().resetAnimation();

    setStatus(READY);
    setVanishing(false);
    noticed = false;

    setX(-24);
    setY(-24);
  }

  @Override
  public void updateMovement(
    GameTile[][] tiles,
    float delta,
    int episode,
    Player player
  ) {
    if (getStatus() == VANISHING) {
      boolean actionContinues = true;
      if (getVanishTimeLeft() > 0) {
        continueVanishAnimation(delta);
      } else {
        actionContinues = false;
      }
      if (!actionContinues) {
        setStatus(DEAD);
      }
    } else if (getStatus() == TRIGGERED) {
      continueTriggering(delta);

      if (getTriggerTimeLeft() <= 0) {
        fallingTrapStartsFalling(tiles, delta);
      }
    }

    if (getStatus() == READY) {
      if (tiles[getTilePosY() + -1][getTilePosX()].isConcrete() == false) {
        fallingTrapStartsFalling(tiles, delta);
      }
    }

    if (getStatus() == FALLING) {
      boolean actionContinues = true;

      if (getTargetGameObjectPosY() <= getY()) {
        actionContinues = false;
      }
      if (actionContinues) {
        continueFalling(delta);
        checkIfNeedToFallEvenMore(tiles);
      } else {
        vanish();

        continueVanishAnimation(delta);
      }
    }
  }

  @Override
  public void checkIfNeedToFallEvenMore(GameTile[][] tiles) {
    if (
      tiles[getTargetTilePosY() + 1][getTilePosX()].isConcrete() == false ||
      tiles[getTargetTilePosY() + 1][getTilePosX()].isVanishing()
    ) {
      setTargetTilePosY(getTargetTilePosY() + 1);
      setTargetGameObjectPosY(getTargetGameObjectPosY() + 1);
    }
  }

  public void fallingTrapStartsFalling(GameTile[][] tiles, float delta) {
    putInTilePos(getTilePosY(), getTilePosX());
    int amountOfTilesToFall = 1;
    boolean continues = true;
    for (int yy = getTilePosY() + 2; yy < tiles.length && continues; yy++) {
      if (tiles[yy][getTilePosX()].isConcrete() == false) {
        amountOfTilesToFall++;
      } else {
        continues = false;
      }
    }
    startFalling(amountOfTilesToFall);
  }

  @Override
  public boolean getGetsDestroyedByFallingPlayer() {
    return true;
  }

  @Override
  public int getHazardStrength() {
    return hazardStrength;
  }

  public boolean isNoticed() {
    return noticed;
  }

  public void setNoticed(boolean noticed) {
    this.noticed = noticed;
  }

  @Override
  public void vanish() {
    setVanishing(true);
    setConcrete(false);
    setVanishTimeLeft(0.6f);
    setStatus(VANISHING);
  }

  public void draw(SpriteBatch batch) {
    //to prevent bugs of colliding with player too easily

    float xFix = -0.075f;
    //this multplied with width of rectangle must be as close to 1.00000f as possible
    float widthMultiplier = 1.1759999f;

    if (getStatus() == READY) {
      if (noticed) {
        batch.draw(
          fallingTrapNoticed,
          getX() + xFix,
          getY(),
          getWidth() * widthMultiplier,
          getHeight()
        );
      } else {
        batch.draw(
          standTexture,
          getX() + xFix,
          getY(),
          getWidth() * widthMultiplier,
          getHeight()
        );
      }
    } else if (getStatus() == FALLING) {
      batch.draw(
        getFallAnimation().getCurrentFrame(),
        getX() + xFix,
        getY(),
        getWidth() * widthMultiplier,
        getHeight()
      );
    } else if (getStatus() == VANISHING) {
      if (getVanishAnimation().getCurrentFrame() != null) {
        batch.draw(
          getVanishAnimation().getCurrentFrame(),
          getX() + xFix,
          getY(),
          getWidth() * widthMultiplier,
          getHeight()
        );
      }
    } else if (getStatus() == TRIGGERED) {
      if (getTriggerAnimation().getCurrentFrame() != null) {
        batch.draw(
          getTriggerAnimation().getCurrentFrame(),
          getX() + xFix,
          getY(),
          getWidth() * widthMultiplier,
          getHeight()
        );
      } else {
        batch.draw(
          standTexture,
          getX() * xFix,
          getY(),
          getWidth() * widthMultiplier,
          getHeight()
        );
      }
    }
  }
}
