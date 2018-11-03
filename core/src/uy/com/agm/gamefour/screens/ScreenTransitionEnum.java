package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

import uy.com.agm.gamefour.screens.transitions.ColorFadeTransition;
import uy.com.agm.gamefour.screens.transitions.FadeTransition;
import uy.com.agm.gamefour.screens.transitions.IScreenTransition;
import uy.com.agm.gamefour.screens.transitions.RotatingTransition;
import uy.com.agm.gamefour.screens.transitions.SliceTransition;
import uy.com.agm.gamefour.screens.transitions.SlideTransition;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public enum ScreenTransitionEnum {
    COLOR_FADE_WHITE {
        public IScreenTransition getScreenTransition() {
            return ColorFadeTransition.init(2.0f, Color.WHITE, Interpolation.sine);
        }
    },

    COLOR_FADE_BLACK {
        public IScreenTransition getScreenTransition() {
            return ColorFadeTransition.init(1.0f, Color.BLACK, Interpolation.exp10);
        }
    },

    ROTATING {
        public IScreenTransition getScreenTransition() {
            return RotatingTransition.init(2.0f, Interpolation.pow2Out, 720.0f, RotatingTransition.TransitionScaling.IN);
        }
    },

    ROTATING_BOUNCE {
        public IScreenTransition getScreenTransition() {
            return RotatingTransition.init(2.0f, Interpolation.bounce, 360.0f, RotatingTransition.TransitionScaling.IN);
        }
    },

    FADE {
        public IScreenTransition getScreenTransition() {
            return FadeTransition.init(0.75f);
        }
    },

    SLICE_UP_DOWN_80 {
        public IScreenTransition getScreenTransition() {
            return SliceTransition.init(2.0f, SliceTransition.UP_DOWN, 80, Interpolation.pow4);
        }
    },

    SLICE_UP_DOWN_10 {
        public IScreenTransition getScreenTransition() {
            return SliceTransition.init(2.0f, SliceTransition.UP_DOWN, 10, Interpolation.pow5Out);
        }
    },

    SLICE_DOWN_8 {
        public IScreenTransition getScreenTransition() {
            return SliceTransition.init(1.5f, SliceTransition.DOWN, 8, Interpolation.bounce);
        }
    },

    SLIDE_LEFT_LINEAR {
        public IScreenTransition getScreenTransition() {
            return SlideTransition.init(0.75f, SlideTransition.LEFT, true, Interpolation.linear);
        }
    },

    SLIDE_LEFT_EXP {
        public IScreenTransition getScreenTransition() {
            return SlideTransition.init(0.5f, SlideTransition.LEFT, false, Interpolation.exp10Out);
        }
    },

    SLIDE_RIGHT_EXP {
        public IScreenTransition getScreenTransition() {
            return SlideTransition.init(0.5f, SlideTransition.RIGHT, true, Interpolation.exp10Out);
        }
    },

    SLIDE_UP {
        public IScreenTransition getScreenTransition() {
            return SlideTransition.init(1.0f, SlideTransition.UP, false, Interpolation.bounce);
        }
    },

    SLIDE_DOWN {
        public IScreenTransition getScreenTransition() {
            return SlideTransition.init(1.0f, SlideTransition.DOWN, false, Interpolation.bounceOut);
        }
    };

    public abstract IScreenTransition getScreenTransition();
}
