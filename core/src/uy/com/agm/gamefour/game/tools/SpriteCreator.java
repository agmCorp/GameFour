package uy.com.agm.gamefour.game.tools;

import com.badlogic.gdx.math.Vector3;

import uy.com.agm.gamefour.game.WorldCamera;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class SpriteCreator {
    private static final String TAG = SpriteCreator.class.getName();

    private Jumper jumper;

    public SpriteCreator(WorldCamera worldCamera) {
        // Creates Jumper in the game world
        Vector3 pos = worldCamera.getWorldCamera().position;
        jumper = new Jumper(pos.x / 2, pos.y / 2);
    }

    public Jumper getJumper() {
        return jumper;
    }
}

