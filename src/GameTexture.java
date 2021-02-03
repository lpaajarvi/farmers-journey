package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.tuni.tiko.digging.util.AnimTools;

public class GameTexture extends TextureRegion {

  public GameTexture(Texture texture) {
    super(texture);
    flip(false, true);
  }

  public GameTexture(Texture texture, boolean flippedX) {
    super(texture);
    flip(false, true);
    if (flippedX) {
      flip(true, false);
    }
  }
}
