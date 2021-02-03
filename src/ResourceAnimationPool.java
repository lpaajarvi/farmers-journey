package fi.tuni.tiko.digging;

import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.Iterator;

public class ResourceAnimationPool extends Pool<ResourceGainedAnimation> {


    // The following comment taken from https://www.gamedevelopment.blog/libgdx-object-pooling/
    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public ResourceAnimationPool (int init, int max) {
        super(init, max);

    }


    @Override
    protected ResourceGainedAnimation newObject () {
        return new ResourceGainedAnimation();
    }

    public void putAllBackToPool (ArrayList<ResourceGainedAnimation> list) {
        Iterator<ResourceGainedAnimation> it = list.iterator();
        while (it.hasNext()) {
            ResourceGainedAnimation current = it.next();
            free(current);
            it.remove();

        }
    }
}
