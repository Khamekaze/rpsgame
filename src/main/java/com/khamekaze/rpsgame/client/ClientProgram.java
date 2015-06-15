package com.khamekaze.rpsgame.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khamekaze
 */
public class ClientProgram extends Listener {

    public Client client;
    public String serverIp, playerName;
    public int tcpPort = 27960, udpPort = 27960;
    public Map<Integer, MPPlayer> players = new HashMap<>();
    public boolean clientStarted = false, serverIsPlaying = false;
    
    public ClientProgram(String serverIp, String playerName) {
        this.serverIp = serverIp;
        this.playerName = playerName;
    }
    
    public ClientProgram() {
        
    }
    
    public void network(String ip) {
        System.out.println("Connecting to server");
        
        client = new Client();
        
        client.addListener(this);
        client.start();
        clientStarted = true;
        
        try {
            client.connect(5000, ip, tcpPort, udpPort);
        } catch (IOException ex) {
            Logger.getLogger(ClientProgram.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Connected! Waiting for packet");
        
    }
    
    public void recieved(Connection c, Object o) {
        if(o instanceof PacketAddPlayer) {
            PacketAddPlayer packet = (PacketAddPlayer) o;
            MPPlayer player = new MPPlayer();
            players.put(packet.id, player);
            players.get(packet.id).name = packet.name;
            players.get(packet.id).connected = true;
            players.get(packet.id).ready = false;
        } else if(o instanceof PacketRemovePlayer) {
            PacketRemovePlayer packet = (PacketRemovePlayer) o;
            players.get(packet.id).connected = false;
        } else if(o instanceof PacketAttack) {
            PacketAttack packet = (PacketAttack) o;
            players.get(packet.id).attackDamage = packet.attackDamage;
            players.get(packet.id).attackZone = packet.attackZone;
        } else if(o instanceof PacketDefend) {
            PacketDefend packet = (PacketDefend) o;
            players.get(packet.id).defenceZone = packet.defenceZone;
        } else if(o instanceof PacketHp) {
            PacketHp packet = (PacketHp) o;
            players.get(packet.id).hp = packet.hp;
        } else if(o instanceof PacketReady) {
            PacketReady packet = (PacketReady) o;
            players.get(packet.id).ready = packet.ready;
        } else if(o instanceof PacketServerIsPlaying) {
            PacketServerIsPlaying packet = (PacketServerIsPlaying) o;
            serverIsPlaying = packet.serverIsPlaying;
        }
    }
    
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Map<Integer, MPPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, MPPlayer> players) {
        this.players = players;
    }
    
    public boolean serverIsPlaying() {
        return serverIsPlaying;
    }
    
}
