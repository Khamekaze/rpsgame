package com.khamekaze.rpsgame;

/**
 *
 * @author Khamekaze
 */
public class Player {
    
    private int playerId;
    
    public Player() {
        
    }
    
    public Player(int id) {
        this.playerId = id;
    }
    
    public void setId(int id) {
        this.playerId = id;
    }
    
    public int getId() {
        return playerId;
    }

}
