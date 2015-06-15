package com.khamekaze.rpsgame.server;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author Khamekaze
 */
public class ConnectedPlayer {
    public String name;
    public Connection c;
    public int hp;
    public boolean ready;
    public int attackDamage;
    public int id;
    public int attackZone;
    public int defenceZone;

}
