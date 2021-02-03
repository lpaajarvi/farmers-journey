package fi.tuni.tiko.digging;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;
import static fi.tuni.tiko.digging.Player.DIGGING;
import static fi.tuni.tiko.digging.Player.READY;
import static fi.tuni.tiko.digging.PlayerControls.NOQUEU;
import static fi.tuni.tiko.digging.PlayerControls.TRYDOWN;
import static fi.tuni.tiko.digging.PlayerControls.TRYLEFT;
import static fi.tuni.tiko.digging.PlayerControls.TRYRIGHT;
import static fi.tuni.tiko.digging.PlayerControls.TRYUP;
import static fi.tuni.tiko.digging.ScreenHelper.CAMERACENTER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;

public class PlayScreen extends GameScreen {

  private GestureDetector playScreenDetector;

  private ArrayList<MenuButton> buttons;

  private Player player;
  private PlayerControls playerControls;

  private Stage currentStage;

  private ArrayList<Stage> allStages;
  private ArrayList<LevelStats> allLevelStats;

  ResourceUI resourceUI;

  //private SpriteBatch hudBatch;

  TilePools tilePools;
  HazardPools hazardPools;

  TileAnimationPools tileAnimationPools;

  MenuButton pauseButton;
  //MenuButton pauseButton2;

  GameTexture pauseButtonTexture = new GameTexture(
    new Texture("menus/buttonPause.png")
  );
  GameTexture pauseButtonPressedTexture = new GameTexture(
    new Texture("menus/buttonPausePressed.png")
  );

  GameTexture levelText = new GameTexture(new Texture("numbers/level.png"));

  //Viewport gameport;

  int helperWidth;
  int helperHeight;

  //private BitmapFont font;

  public PlayScreen(
    MainGame mainGame,
    ScreenHelper screenHelper,
    PlayScreenHelper playScreenHelper
  ) {
    super(mainGame, screenHelper);
    buttons = new ArrayList<>();

    this.resourceUI = mainGame.resourceUI;
    player = screenHelper.player;
    playerControls = playScreenHelper.playerControls;
    allStages = playScreenHelper.allStages;
    allLevelStats = playScreenHelper.allLevelStats;
    this.currentStage = mainGame.currentStage;

    tilePools = currentStage.tilePools;
    hazardPools = currentStage.hazardPools;
    tileAnimationPools = currentStage.tileAnimationPools;

    pauseButton =
      new MenuButton(
        pauseButtonTexture,
        pauseButtonPressedTexture,
        1.34f,
        1.34f,
        MAIN_MENU
      );
    pauseButton.setX(1.0f);
    pauseButton.setY(0.5f);

    buttons.add(pauseButton);

    pressedArea = new Rectangle(-1f, -1f, pressedAreaSize, pressedAreaSize);

    playScreenDetector = new GestureDetector(this);

    gameport =
      new ExtendViewport(
        TILES_IN_ROWS_WITHOUT_EDGES + 2 * UNDIGGABLE_MARGIN,
        12.8f,
        camera
      );
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(playScreenDetector);
    //gameport.apply();

    //gameport.update(466,12, true);
    //gameport.update(233, 245);

    if (mainGame.musicOn) {
      mainGame.audio.music.play();
    }
  }

  @Override
  public void render(float delta) {
    batch.setProjectionMatrix(camera.combined);
    clearScreen();

    if (player.getTilePosY() != currentStage.tiles.length - 2) {
      screenHelper.updateCameraPosition(
        currentStage,
        gameport,
        5,
        helperWidth,
        helperHeight
      );
    } else {
      screenHelper.forceUnzoom(gameport, helperWidth, helperHeight);
      System.out.println("alin kerros");
      if (
        currentStage.tiles[currentStage.tiles.length -
          2][player.getTilePosX()] instanceof EntranceTile
      ) {
        currentStage.proceedToNextLevel(this);
      }
    }

    mainGame.checkSpecialTiles(delta);
    mainGame.checkVanishingTiles(delta);

    mainGame.checkDeadHazards();
    mainGame.checkPlayerHazardCollision();
    mainGame.checkHazardHazardCollision();

    mainGame.checkIfHazardsGetTriggeredOrNoticed();

    mainGame.checkHazardMovement(delta);

    mainGame.checkPlayersUnwantedMovement();

    if ((player.getStatus() == READY) && (playerControls.getQueu() != NOQUEU)) {
      playerControls.checkQueu(player, currentStage);
    }

    mainGame.controlPlayer();

    player.updateMovement(delta, currentStage);

    tileAnimationPools.checkResourceGainedAnimations(delta);

    if (actionActivated) {
      continueActionCountdown(Gdx.graphics.getDeltaTime(), buttons);
      //System.out.println("this worked");
      //screenHelper.forceUnzoom(gameport, helperWidth, helperHeight);

    }

    batch.begin();

    batch.draw(mainGame.getFarm(), +0.9f, -4f, 7.4f, 5f);
    currentStage.draw(batch);

    tileAnimationPools.draw(batch);

    player.draw(batch);

    drawPlayScreenElements();
    batch.end();

    screenHelper.moveCamera();
  }

  //horrible coding but this will have to do for now
  public void drawPlayScreenElements() {
    if (!screenHelper.isZoomed) {
      if (!pauseButton.isPressed()) {
        batch.draw(
          pauseButtonTexture,
          pauseButton.getX() - 0f,
          camera.position.y - 6.4f,
          pauseButton.getWidth(),
          pauseButton.getHeight()
        );
      } else {
        batch.draw(
          pauseButtonPressedTexture,
          pauseButton.getX() - 0f,
          camera.position.y - 6.4f,
          pauseButton.getWidth(),
          pauseButton.getHeight()
        );
      }

      batch.draw(
        resourceUI.meter,
        6.62f,
        camera.position.y - 6.243f,
        1.4f,
        0.5f
      );
      for (int i = 0; i < resourceUI.getLinesToDraw(); i++) {
        batch.draw(
          resourceUI.greenResource,
          6.64f + i * 0.02f,
          camera.position.y - 6.22f,
          0.02f,
          0.455f
        );
      }

      batch.draw(
        levelText,
        camera.position.x - 1.0f,
        camera.position.y - 6.1f,
        1.2f,
        0.6f
      );
      batch.draw(
        mainGame.numbers[mainGame.episode],
        camera.position.x + 0.3f,
        camera.position.y - 6.05f,
        0.25f,
        0.5f
      );
      batch.draw(
        mainGame.numbers[11],
        camera.position.x + 0.55f,
        camera.position.y - 6.05f,
        0.25f,
        0.5f
      );
      if (mainGame.level < 10) {
        batch.draw(
          mainGame.numbers[mainGame.level],
          camera.position.x + 0.8f,
          camera.position.y - 6.05f,
          0.25f,
          0.5f
        );
      } else {
        batch.draw(
          mainGame.numbers[mainGame.level],
          camera.position.x + 0.8f,
          camera.position.y - 6.05f,
          0.5f,
          0.5f
        );
      }
      //case when the zoom is on
    } else {
      float zDivider = 7.2f;
      float zMultiplier = 5.2f;

      if (!pauseButton.isPressed()) {
        batch.draw(
          pauseButtonTexture,
          camera.position.x - 2.55f,
          camera.position.y - 4.58f,
          pauseButton.getWidth() / zDivider * zMultiplier,
          pauseButton.getHeight() / zDivider * zMultiplier
        );
      } else {
        batch.draw(
          pauseButtonPressedTexture,
          camera.position.x - 2.55f,
          camera.position.y - 4.58f,
          pauseButton.getWidth() / zDivider * zMultiplier,
          pauseButton.getHeight() / zDivider * zMultiplier
        );
      }

      batch.draw(
        resourceUI.meter,
        camera.position.x + 1.51f,
        camera.position.y - 4.46f,
        1.4f / zDivider * zMultiplier,
        0.5f / zDivider * zMultiplier
      );
      for (int i = 0; i < resourceUI.getLinesToDraw(); i++) {
        batch.draw(
          resourceUI.greenResource,
          camera.position.x +
          1.51f +
          (0.02f / zDivider * zMultiplier) +
          i *
          (0.02f / zDivider * zMultiplier),
          camera.position.y - 4.45f,
          0.02f / zDivider * zMultiplier,
          0.47f / zDivider * zMultiplier
        );
      }

      batch.draw(
        levelText,
        camera.position.x - 0.72f,
        camera.position.y - 4.36f,
        1.2f / zDivider * zMultiplier,
        0.6f / zDivider * zMultiplier
      );
      batch.draw(
        mainGame.numbers[mainGame.episode],
        camera.position.x + 0.22f,
        camera.position.y - 4.34f,
        0.25f / zDivider * zMultiplier,
        0.5f / zDivider * zMultiplier
      );
      batch.draw(
        mainGame.numbers[11],
        camera.position.x + 0.22f + 0.25f / zDivider * zMultiplier,
        camera.position.y - 4.34f,
        0.25f / zDivider * zMultiplier,
        0.5f / zDivider * zMultiplier
      );
      if (mainGame.level < 10) {
        batch.draw(
          mainGame.numbers[mainGame.level],
          camera.position.x + 0.22f + 0.5f / zDivider * zMultiplier,
          camera.position.y - 4.34f,
          0.25f / zDivider * zMultiplier,
          0.5f / zDivider * zMultiplier
        );
      } else {
        batch.draw(
          mainGame.numbers[mainGame.level],
          camera.position.x + 0.22f + 0.5f / zDivider * zMultiplier,
          camera.position.y - 4.34f,
          0.5f / zDivider * zMultiplier,
          0.5f / zDivider * zMultiplier
        );
      }

      batch.draw(
        resourceUI.meter,
        6.62f,
        camera.position.y - 6.23f,
        1.4f,
        0.5f
      );
      for (int i = 0; i < resourceUI.getLinesToDraw(); i++) {
        batch.draw(
          resourceUI.greenResource,
          6.64f + i * 0.02f,
          camera.position.y - 6.22f,
          0.02f,
          0.48f
        );
      }
    }
  }

  @Override
  public void resize(int width, int height) {
    gameport.update(width, height);
    helperWidth = width;
    helperHeight = height;
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {
    camera.position.x = CAMERACENTER;
  }

  @Override
  public void dispose() {}

  @Override
  public boolean touchDown(float x, float y, int pointer, int button) {
    //Gdx.app.log("touchDown", "Touchdown: x: "+x+", y: "+y);
    return false;
  }

  @Override
  public boolean tap(float x, float y, int count, int button) {
    pressedArea.setPosition(
      screenHelper.menuAdjustedX(x),
      screenHelper.menuAdjustedY(y)
    );

    for (int i = 0; i < buttons.size(); i++) {
      MenuButton menuButton = buttons.get(i);
      if (pressedArea.overlaps(menuButton.getRectangle())) {
        menuButton.setPressed(true);

        activateAction(menuButton.getActionToPerform());
      }
    }

    return true;
  }

  @Override
  public boolean longPress(float x, float y) {
    if (player.getStatus() == READY) {
      for (
        int yyy = player.getTilePosY();
        yyy < player.getTilePosY() + 7;
        yyy++
      ) {
        currentStage.tiles[yyy][player.getTilePosX()] =
          new BlankTile(yyy, player.getTilePosX());
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean fling(float velocityX, float velocityY, int button) {
    if (player.getStatus() == READY) {
      if ((velocityX > Math.abs(velocityY))) {
        playerControls.tryRight(player, currentStage);
        return true;
      } else if ((velocityX < 0) && Math.abs(velocityX) > Math.abs(velocityY)) {
        playerControls.tryLeft(player, currentStage);
        return true;
      } else if ((velocityY > Math.abs(velocityX))) {
        playerControls.tryDown(player, currentStage);
        return true;
      } else {
        playerControls.tryUp(player, currentStage);
        return true;
      }
    } else {
      if ((velocityX > Math.abs(velocityY))) {
        playerControls.setQueu(TRYRIGHT);
      } else if ((velocityX < 0) && Math.abs(velocityX) > Math.abs(velocityY)) {
        playerControls.setQueu(TRYLEFT);
      } else if ((velocityY > Math.abs(velocityX))) {
        playerControls.setQueu(TRYDOWN);
      } else {
        playerControls.setQueu(TRYUP);
      }

      return true;
    }
  }

  @Override
  public boolean pan(float x, float y, float deltaX, float deltaY) {
    return false;
  }

  @Override
  public boolean panStop(float x, float y, int pointer, int button) {
    return false;
  }

  @Override
  public boolean zoom(float initialDistance, float distance) {
    return false;
  }

  @Override
  public boolean pinch(
    Vector2 initialPointer1,
    Vector2 initialPointer2,
    Vector2 pointer1,
    Vector2 pointer2
  ) {
    return false;
  }

  @Override
  public void pinchStop() {}

  private void clearScreen() {
    Gdx.gl.glClearColor(0.39f, 0.39f, 0.39f, 0.19f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }
}
