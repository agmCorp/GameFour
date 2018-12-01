package uy.com.agm.gamefour.screens;

import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.gui.CreditsScreen;
import uy.com.agm.gamefour.screens.gui.MainMenuScreen;
import uy.com.agm.gamefour.screens.gui.SplashScreen;
import uy.com.agm.gamefour.screens.play.PlayScreen;

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
            return new PlayScreen(game);
        }
    };

    public abstract AbstractScreen getScreen(GameFour game, Object... params);
}