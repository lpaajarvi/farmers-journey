package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

import static fi.tuni.tiko.digging.EntranceTile.entrance;

public class BlankTile extends GameTile implements Poolable {

    static GameTexture blankTexture = new GameTexture(new Texture("blankTile.png"));




    public BlankTile(int locY, int locX) {


        super(locY, locX);


        diggable=false;


        setConcrete(false);


        setTexture(blankTexture);


        rectangle=new Rectangle(locX, locY, 1.00f, 1.00f);

    }

    @Override
    public void vanish () {

    }

    //this method will be used after the tile is taken from pool
    public void setInPlace(int locY, int locX) {
        setLocationX(locX);
        setLocationY(locY);

        setX(locX);
        setY(locY);

    }

    public void setEntranceTexture() {
        setTexture(entrance);
    }

    @Override
    public void reset () {
        setLocationY(-1);
        setLocationX(-1);
        diggable=false;
        setConcrete(false);
        setTexture(blankTexture);
        //getVanishAnimation().resetAnimation();
        setVanishing(false);
        //rectangle out of sight too
        setX(-24);
        setY(-24);
        setOccupied(false);
        root=null;

    }
}
