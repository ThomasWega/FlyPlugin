package me.wega.fly.listeners;

import me.wega.fly.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FlyListener implements Listener {

    private final Main mainClass;

    public FlyListener(Main mainClass){
        this.mainClass = mainClass;
    }
    public void readData(Player player){
        File flyData = new File(mainClass.getDataFolder() + File.separator + "player_data.yml");
        FileConfiguration flyDataConf = YamlConfiguration.loadConfiguration(flyData);
        if (flyDataConf.contains("players." + player.getUniqueId())){
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(ChatColor.WHITE + "Fly has been restored to ON");
            if (!Objects.requireNonNull(flyDataConf.get("players." + player.getUniqueId() + ".fly-speed")).equals(1.0D)){
                player.sendMessage(ChatColor.WHITE + "Fly speed has been restored to " + flyDataConf.get("players." + player.getUniqueId() + ".fly-speed"));
                }
            }
    }
    public void addPlayer(Player player){
            File flyData = new File(mainClass.getDataFolder() + File.separator + "player_data.yml");
            FileConfiguration flyDataConf = YamlConfiguration.loadConfiguration(flyData);
            flyDataConf.createSection("players." + player.getUniqueId());
            flyDataConf.set("players." + player.getUniqueId() + ".fly-speed", player.getFlySpeed() * 10);
        try {
            flyDataConf.save(flyData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        readData(event.getPlayer());
       }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){

        if (event.getPlayer().getAllowFlight()){
            addPlayer(event.getPlayer());
        }
    }
}
