package com.khamekaze.rpsgame;

import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Khamekaze
 */
public class Main extends StateBasedGame{
    
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 576;
    
    public static final int PLAYSTATE = 1;
    public static final int GAMEOVERSTATE = 2;

    public Main(String name) {
        super(name);
    }
    
    @Override
    public void initStatesList(GameContainer gc) {
        addState(new PlayState("Game"));
        enterState(PLAYSTATE);
    }
    
    public static void main(String[] args) {
        try {
                    //The gamecontainer used by Slick, this is where everything will be contained
                    AppGameContainer appgc = new AppGameContainer(new Main("RPSGame"));
                    appgc.setDisplayMode(WIDTH, HEIGHT, false);
                    appgc.getFPS();
                    appgc.setShowFPS(false);
                    appgc.setTargetFrameRate(100);
                    appgc.setAlwaysRender(true);
                    appgc.start();
            } catch (SlickException ex) {
                    Logger.getLogger(ex.toString());
            }
    }

}
