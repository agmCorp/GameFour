package uy.com.agm.gamefour.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

import uy.com.agm.gamefour.admob.DummyAdsController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.playservices.DummyPlayServices;

public class HtmlLauncher extends GwtApplication {

    // FIXED SIZE APPLICATION
    //    @Override
    //    public GwtApplicationConfiguration getConfig () {
    //            return new GwtApplicationConfiguration(GameFour.APPLICATION_WIDTH, GameFour.APPLICATION_HEIGHT);
    //    }
    // END CODE FOR FIXED SIZE APPLICATION

    // LOG CONSOLE
    // Libgdx provides cfg.log to include a TextArea console, but I prefer to use the console (F12)
    @Override
    public void log(String tag, String message) {
        if (getLogLevel() >= LOG_INFO) {
            consoleLog(tag + ": " + message);
        }
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        if (getLogLevel() >= LOG_INFO) {
            consoleLog(tag + ": " + message + "\n" + exception.getMessage());
        }
    }

    @Override
    public void error(String tag, String message) {
        if (getLogLevel() >= LOG_ERROR) {
            consoleLog(tag + ": " + message);
        }
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        if (getLogLevel() >= LOG_ERROR) {
            consoleLog(tag + ": " + message + "\n" + exception.getMessage());
        }
    }

    @Override
    public void debug(String tag, String message) {
        if (getLogLevel() >= LOG_DEBUG) {
            consoleLog(tag + ": " + message);
        }
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        if (getLogLevel() >= LOG_DEBUG) {
            consoleLog(tag + ": " + message + "\n" + exception.getMessage());
        }
    }
    // END CODE FOR LOG CONSOLE

    // RESIZABLE APPLICATION
    // PADDING is to avoid scrolling in iframes, set to 20 if you have problems
    private static final int PADDING = 0;
    private GwtApplicationConfiguration cfg;

    @Override
    public GwtApplicationConfiguration getConfig() {
        int w = Window.getClientWidth() - PADDING;
        int h = Window.getClientHeight() - PADDING;
        cfg = new GwtApplicationConfiguration(w, h);
        Window.enableScrolling(false);
        Window.setMargin("0");
        Window.addResizeHandler(new ResizeListener());
        cfg.preferFlash = false;
        return cfg;
    }

    class ResizeListener implements ResizeHandler {
        @Override
        public void onResize(ResizeEvent event) {
            int width = event.getWidth() - PADDING;
            int height = event.getHeight() - PADDING;
            getRootPanel().setWidth("" + width + "px");
            getRootPanel().setHeight("" + height + "px");
            getApplicationListener().resize(width, height);
            Gdx.graphics.setWindowedMode(width, height);
        }
    }
    // END OF CODE FOR RESIZABLE APPLICATION

    @Override
    public ApplicationListener createApplicationListener() {
        return new GameFour(new DummyAdsController(), new DummyPlayServices());
    }
}