package uy.com.agm.gamefour.game.tools;

import com.badlogic.gdx.math.Vector3;

import uy.com.agm.gamefour.game.WorldController;
import uy.com.agm.gamefour.game.WorldRenderer;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    private WorldController worldController;
    private  WorldRenderer worldRenderer;
    private Jumper jumper;

    public GameWorld(WorldController worldController, WorldRenderer worldRenderer) {
        this.worldController = worldController;
        this.worldRenderer = worldRenderer;

        // Creates Jumper in the game world
        Vector3 pos = worldRenderer.getGameCamera().position;
        jumper = new Jumper(pos.x / 2, pos.y / 2);
    }

    public Jumper getJumper() {
        return jumper;
    }
}

