package uy.com.agm.gamefour.game.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class WorldContactListener implements ContactListener {
    private static final String TAG = WorldContactListener.class.getName();

    // Box2D Collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short PLATFORM_BIT = 1;
    public static final short JUMPER_BIT = 2;
    public static final short OBSTACLE_BIT = 4;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        Fixture fixC;

        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (collisionDef) {
            case PLATFORM_BIT | JUMPER_BIT:
                // todo aca lo que tengo que hacer es ver que indice tiene la plataforma con la que choco en el arreglo de
                // plataformas. Si es el indice 1 o posterior quiere decir que salte bien (y que no salte en el lugar por ejemplo).
                // Si salte bien solo ahi debo aumentar el score, si salte en el lugar por ejempo igual debo setvelocity en cero y todo eso.
                fixC = fixA.getFilterData().categoryBits == JUMPER_BIT ? fixA : fixB;
                if (fixC.isSensor()) {
                    ((Jumper) fixC.getUserData()).onSuccessfulJump();
                }
                break;

            case OBSTACLE_BIT | JUMPER_BIT:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
