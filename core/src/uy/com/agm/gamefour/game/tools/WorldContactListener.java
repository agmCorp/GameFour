package uy.com.agm.gamefour.game.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import uy.com.agm.gamefour.sprites.Jumper;
import uy.com.agm.gamefour.sprites.Platform;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class WorldContactListener implements ContactListener {
    private static final String TAG = WorldContactListener.class.getName();

    // Box2D Collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short PLATFORM_BIT = 1;
    public static final short JUMPER_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short WEAPON_BIT = 8;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (collisionDef) {
            case PLATFORM_BIT | JUMPER_BIT:
                if (fixA.getFilterData().categoryBits == JUMPER_BIT) {
                    if (fixA.isSensor()) {
                        resolveContact(((Jumper) fixA.getUserData()), ((Platform) fixB.getUserData()));
                    } else {
                        Jumper jumper = ((Jumper) fixA.getUserData());
                        if (!jumper.isIdle()) {
                            jumper.onHit();
                        }
                    }
                } else {
                    if (fixB.isSensor()) {
                        resolveContact(((Jumper) fixB.getUserData()), ((Platform) fixA.getUserData()));
                    } else {
                        Jumper jumper = ((Jumper) fixB.getUserData());
                        if (!jumper.isIdle()) {
                            jumper.onHit();
                        }
                    }
                }
                break;
        }
    }

    private void resolveContact(Jumper jumper, Platform platform) {
        if (jumper.getCurrentPlatform() != platform) {
            jumper.onSuccessfulJump(platform);
        } else {
            jumper.onLanding();
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
