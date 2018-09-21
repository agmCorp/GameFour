package uy.com.agm.gamefour.game.tools;

import com.badlogic.gdx.math.Vector3;

import uy.com.agm.gamefour.screens.PlayScreen;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Box2DCreator { // TODO ESTO PARA MI DEBERIA LLAMARSE WORLD Y SE LO DEBERIA PASAR AL RENDERRE Y AL CONTROLLER
    private static final String TAG = Box2DCreator.class.getName();

    private PlayScreen playScreen;
    private Jumper jumper;

    public Box2DCreator(PlayScreen playScreen) {
        this.playScreen = playScreen;

        // Creates Jumper in the game world
        Vector3 pos = playScreen.getCamera().position;
       // todo jumper = new Jumper(playScreen, pos.x / 2, pos.y / 2);
    }

    public Jumper getJumper() {
        return jumper;
    }
}

