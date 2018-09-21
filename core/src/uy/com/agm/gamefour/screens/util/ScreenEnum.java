package uy.com.agm.gamefour.screens.util;

import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.AbstractScreen;
import uy.com.agm.gamefour.screens.CreditsScreen;
import uy.com.agm.gamefour.screens.MainMenuScreen;
import uy.com.agm.gamefour.screens.PlayScreen2;
import uy.com.agm.gamefour.screens.SplashScreen;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public enum ScreenEnum {
    SPLASH {
        public AbstractScreen getScreen(GameFour game, Object... params) {
            return new SplashScreen(game);
        }
    },

    MAIN_MENU {
        public AbstractScreen getScreen(GameFour game, Object... params) {
            return new MainMenuScreen(game);
        }
    },

    CREDITS {
        public AbstractScreen getScreen(GameFour game, Object... params) {
            return new CreditsScreen(game);
        }
    },

    PLAY_GAME {
        public AbstractScreen getScreen(GameFour game, Object... params) {
            return new PlayScreen2(game);
        }
    };

    public abstract AbstractScreen getScreen(GameFour game, Object... params);
}