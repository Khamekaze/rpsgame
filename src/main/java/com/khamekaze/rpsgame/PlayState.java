package com.khamekaze.rpsgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Khamekaze
 */
public class PlayState extends BasicGameState {
    
    private String name;
    private Play play;
    
    private Shape gameOverMenu;
    private boolean gameOver = false;
    
    public PlayState(String name) {
        this.name = name;
        gameOverMenu = new Rectangle(0, 0, Main.WIDTH, Main.HEIGHT);
        
    }

    @Override
    public int getID() {
        return Main.PLAYSTATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        play = new Play();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        play.render(gc.getGraphics());
        if(play.getGameOver()) {
            g.setColor(Color.gray);
            g.fill(gameOverMenu);
            g.setColor(Color.white);
            g.drawString("Game Over!", Main.WIDTH / 2 - 50, Main.HEIGHT / 2);
            if(play.getAiHp() <= 0) {
                g.drawString("Player wins!", Main.WIDTH / 2 - 50, Main.HEIGHT / 2 + 50);
            } else if(play.getPlayerHp() <= 0) {
                g.drawString("AI wins!", Main.WIDTH / 2 - 50, Main.HEIGHT / 2 + 50);
            }
            g.drawString("Press RETURN to play again!", Main.WIDTH / 2 - 50, Main.HEIGHT / 2 + 100);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        if(!play.getGameOver())
            play.update(gc.getInput(), delta);
        else if(play.getGameOver())
            gameOver(gc.getInput());
    }
    
    public void gameOver(Input i) {
        if(i.isKeyPressed(Input.KEY_ENTER)) {
            play.setAiHp(10);
            play.setPlayerHp(10);
            play.setPlayerAttack(1);
            play.setAiAttack(1);
            play.setOutcomeString("");
            play.setGameOver(false);
        }
    }

}
