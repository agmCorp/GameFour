package uy.com.agm.gamefour.screens.util;

import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.AbstractScreen;
import uy.com.agm.gamefour.screens.CreditsScreen;
import uy.com.agm.gamefour.screens.MainMenuScreen;
import uy.com.agm.gamefour.screens.PlayScreen;
import uy.com.agm.gamefour.screens.SplashScreen;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public enum ScreenEnum {
    SPLASH {
        public AbstractScreen getScreen(Object... params) {
            return new SplashScreen((GameFour) params[0]);
        }
    },

    MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen((GameFour) params[0]);
        }
    },

    CREDITS {
        public AbstractScreen getScreen(Object... params) {
            return new CreditsScreen((GameFour) params[0]);
        }
    },

    PLAY_GAME {
        public AbstractScreen getScreen(Object... params) {
            return new PlayScreen((GameFour) params[0]);
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}