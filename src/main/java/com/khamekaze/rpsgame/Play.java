package com.khamekaze.rpsgame;

import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author Khamekaze
 */
public class Play {
    
    private Image defenceAreaH, defenceAreaM, defenceAreaB, attackAreaH, attackAreaM, attackAreaB;
    
    private ArrayList<Image> defenceImages;
    private ArrayList<Image> attackImages;
    private Shape hbDH, hbDM, hbDB, hbAH, hbAM, hbAB;
    private ArrayList<Shape> hitboxes;
    private String outcome;
    
    private int aiHP, playerHP;
    private int aiAttack = 1, playerAttack = 1;
    
    private boolean gameOver = false;
    
    public Play() throws SlickException {
        defenceAreaH = new Image("src/main/resources/img/defenceArea.png");
        defenceAreaM = new Image("src/main/resources/img/defenceArea.png");
        defenceAreaB = new Image("src/main/resources/img/defenceArea.png");
        attackAreaH = new Image("src/main/resources/img/attackArea.png");
        attackAreaM = new Image("src/main/resources/img/attackArea.png");
        attackAreaB = new Image("src/main/resources/img/attackArea.png");
        defenceImages = new ArrayList<>();
        attackImages = new ArrayList<>();
        hitboxes = new ArrayList<>();
        defenceImages.add(defenceAreaH);
        defenceImages.add(defenceAreaM);
        defenceImages.add(defenceAreaB);
        attackImages.add(attackAreaH);
        attackImages.add(attackAreaM);
        attackImages.add(attackAreaB);
        
        hbDH = new Rectangle(100, 100, 75, 75);
        hbDM = new Rectangle(100, 200, 75, 75);
        hbDB = new Rectangle(100, 300, 75, 75);
        
        hbAH = new Rectangle(800, 100, 75, 75);
        hbAM = new Rectangle(800, 200, 75, 75);
        hbAB = new Rectangle(800, 300, 75, 75);
        
        hitboxes.add(hbDH);
        hitboxes.add(hbDM);
        hitboxes.add(hbDB);
        
        hitboxes.add(hbAH);
        hitboxes.add(hbAM);
        hitboxes.add(hbAB);
        
        playerHP = 10;
        aiHP = 10;
        
    }
    
    public void render(Graphics g) {
        int placeX = 100, placeY = 100;
        for(int i = 0; i < 3; i++) {
            attackImages.get(i).draw(placeX + 700, placeY * (i + 1));
        }
        
        for(int i = 0; i < 3; i++) {
            defenceImages.get(i).draw(placeX, placeY * (i + 1));
        }
        
        for(Shape s : hitboxes) {
            g.draw(s);
        }
        
        if(outcome != null) {
            g.drawString(outcome, 450, 100);
        }
        g.setColor(Color.white);
        
        g.drawString("Player HP: " + playerHP, 50, 50);
        g.drawString("Player Attack: " + playerAttack, 50, 75);
        
        g.drawString("AI HP: " + aiHP, 800, 50);
        g.drawString("AI Attack: " + aiAttack, 800, 75);
        
    }
    
    public void update(Input i) {
        checkAttackOrDefence(i);
        checkIfGameOver();
    }
    
    public void checkAttackOrDefence(Input i) {
        Shape selected = null;
        for(Shape s : hitboxes) {
            if(i.getMouseX() > s.getX() && i.getMouseX() < s.getX() + 75 &&
                    i.getMouseY() > s.getY() && i.getMouseY() < s.getY() + 75) {
                if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    selected = s;
                    checkOutcome(selected);
                }
            }
        }
    }
    
    public void checkOutcome(Shape s) {
        if(s == null)
            return;
        
        Random r = new Random();
        
        int ai = r.nextInt(6) + 1;
        System.out.println("Random: " + ai);
        
        if(s.getX() == 100) {
            switch ((int) s.getY()) {
                case 100:
                    if(ai == 1) {
                        outcome = "Player defended againts AI attack Head";
                        playerAttack = playerAttack * 2;
                    }
                    else if(ai == 2) {
                        outcome = "AI attacked Player Middle";
                        playerHP -= aiAttack;
                    }
                    else if(ai == 3) {
                        outcome = "AI attacked Player Bottom";
                        playerHP -= aiAttack;
                    }
                    else
                        outcome = "Double deff!";
                    break;
                    
                case 200:
                    if(ai == 1) {
                        outcome = "AI attacked Player Head";
                        playerHP -= aiAttack;
                    }
                    else if(ai == 2) {
                        outcome = "Player defended against AI attack Middle";
                        playerAttack = playerAttack * 2;
                    }
                    else if(ai == 3) {
                        outcome = "AI attacked Player Bottom";
                        playerHP -= aiAttack;
                    }
                    else
                        outcome = "Double deff!";
                    break;
                    
                case 300:
                    if(ai == 1) {
                        outcome = "AI attacked Player Head";
                        playerHP -= aiAttack;
                    }
                    else if(ai == 2) {
                        outcome = "AI attacked Player Middle";
                        playerHP -= aiAttack;
                    }
                    else if(ai == 3) {
                        outcome = "Player defended against AI attack Bottom";
                        playerAttack = playerAttack * 2;
                    }
                    else
                        outcome = "Double deff!";
                    break;
                    
                default:
                    outcome = "No action";
                    break;
            }
        } else if(s.getX() == 800) {
            switch ((int) s.getY()) {
                case 100:
                    if(ai == 1)
                        outcome = "Parry!";
                    else if(ai == 2) {
                        outcome = "Player hit AI Head";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    else if(ai == 3) {
                        outcome = "AI hit Player Bottom";
                        playerHP -= aiAttack;
                        aiAttack = 1;
                    }
                    else if(ai == 4) {
                        outcome = "AI defended against Player attack Head";
                        playerAttack = 1;
                        aiAttack = aiAttack * 2;
                    }
                    else if(ai == 5) {
                        outcome = "Player hit AI Head";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    else if(ai == 6) {
                        outcome = "Player hit AI Head";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    break;
                    
                case 200:
                    if(ai == 1) {
                        outcome = "AI hit Player Head";
                        playerHP -= aiAttack;
                        aiAttack = 1;
                    }
                    else if(ai == 2)
                        outcome = "Parry!";
                    else if(ai == 3) {
                        outcome = "Player hit AI Middle";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    else if(ai == 4) {
                        outcome = "Player hit AI Middle";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    else if(ai == 5) {
                        outcome = "AI defended against Player attack Middle";
                        playerAttack = 1;
                        aiAttack = aiAttack * 2;
                    }
                    else if(ai == 6) {
                        outcome = "Player hit AI Middle";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    break;
                    
                case 300:
                    if(ai == 1) {
                        outcome = "Player hit AI Bottom";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    else if(ai == 2) {
                        outcome = "AI hit Player Middle";
                        playerHP -= aiAttack;
                        aiAttack = 1;
                    }
                    else if(ai == 3) 
                        outcome = "Parry!";
                    else if(ai == 4) {
                        outcome = "Player hit AI Bottom";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    else if(ai == 5) {
                        outcome = "Player hit AI Bottom";
                        aiHP -= playerAttack;
                        playerAttack = 1;
                    }
                    else if(ai == 6) {
                        outcome = "AI defended against Player attack Bottom";
                        playerAttack = 1;
                        aiAttack = aiAttack * 2;
                    }
                    break;
                    
                default:
                    outcome = "No action!";
                    break;
            }
        }
        
    }
    
    public void checkIfGameOver() {
        if(playerHP <= 0 || aiHP <= 0)
            gameOver = true;
    }
    
    public boolean getGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public void setPlayerHp(int hp) {
        this.playerHP = hp;
    }
    
    public int getPlayerHp() {
        return playerHP;
    }
    
    public void setAiHp(int hp) {
        this.aiHP = hp;
    }
    
    public int getAiHp() {
        return aiHP;
    }
    
    public void setPlayerAttack(int attack) {
        this.playerAttack = attack;
    }
    
    public int getPlayerAttack() {
        return playerAttack;
    }
    
    public void setAiAttack(int attack) {
        this.aiAttack = attack;
    }
    
    public int getAiAttack() {
        return aiAttack;
    }

}
