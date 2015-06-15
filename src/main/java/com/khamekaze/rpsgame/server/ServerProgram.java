package com.khamekaze.rpsgame.server;

import com.khamekaze.rpsgame.server.ConnectedPlayer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.khamekaze.rpsgame.Player;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khamekaze
 */
public class ServerProgram extends Listener {

    public static Server server;
    final int udpPort = 27960, tcpPort = 27960;
    public Map<Integer, ConnectedPlayer> players = new HashMap<>();
    
    public boolean serverReady = false;
    public boolean serverIsPlaying = false;
    
    public void startServer() {
        server = new Server();
        
        server.getKryo().register(PacketAddPlayer.class);
        server.getKryo().register(PacketAttack.class);
        server.getKryo().register(PacketDefend.class);
        server.getKryo().register(PacketRemovePlayer.class);
        server.getKryo().register(PacketServerIsPlaying.class);
        
        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException ex) {
            Logger.getLogger(ServerProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        server.start();
        server.addListener(this);
        System.out.println("Server is ready");
        serverReady = true;
    }
    
    @Override
    public void connected(Connection c) {
        
        System.out.println(serverIsPlaying);
        if(!serverIsPlaying) {
            ConnectedPlayer player = new ConnectedPlayer();
            player.name = "" + c.getID();
            player.c = c;
            player.hp = 10;
            player.attackDamage = 1;
            player.ready = false;
            player.id = c.getID();
            
            PacketAddPlayer packet = new PacketAddPlayer();
            packet.id = c.getID();
            packet.name = player.name;
            server.sendToAllExceptTCP(c.getID(), packet);
            
            for(ConnectedPlayer p : players.values()) {
                PacketAddPlayer packet2 = new PacketAddPlayer();
                packet2.id = p.c.getID();
                packet2.name = p.name;
                c.sendTCP(packet2);
            }
            
            players.put(c.getID(), player);
            System.out.println("Connection recieved");
        } else {
            PacketServerIsPlaying packet = new PacketServerIsPlaying();
            packet.serverIsPlaying = true;
            c.sendTCP(packet);
        }
        
    }
    
    @Override
    public void received(Connection c, Object o) {
        if(o instanceof PacketAttack) {
            PacketAttack packet = (PacketAttack) o;
            players.get(c.getID()).attackDamage = packet.attackDamage;
            players.get(c.getID()).attackZone = packet.attackZone;
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
        } else if(o instanceof PacketDefend) {
            PacketDefend packet = (PacketDefend) o;
            players.get(c.getID()).defenceZone = packet.defenceZone;
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
        } else if(o instanceof PacketHp) {
            PacketHp packet = (PacketHp) o;
            players.get(packet.id).hp = packet.hp;
            packet.id = c.getID();
            server.sendToAllExceptTCP(c.getID(), packet);
        } else if(o instanceof PacketServerIsPlaying) {
            PacketServerIsPlaying packet = (PacketServerIsPlaying) o;
            serverIsPlaying = packet.serverIsPlaying;
        } else if(o instanceof PacketReady) {
            PacketReady packet = (PacketReady) o;
            players.get(packet.id).ready = packet.ready;
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
        }
    }
    
    @Override
    public void disconnected(Connection c) {
        if(!serverIsPlaying) {
            players.remove(c.getID());
            PacketRemovePlayer packet = new PacketRemovePlayer();
            packet.id = c.getID();
            server.sendToAllExceptTCP(c.getID(), packet);
            System.out.println("Connection dropped");
        }
    }
    
    public boolean isServerReady() {
        return serverReady;
    }
    
    public static Server getServer() {
        return server;
    }
    
}
