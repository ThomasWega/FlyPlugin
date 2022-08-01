package me.wega.fly;

import me.wega.fly.commands.FlyCommand;
import me.wega.fly.listeners.FlyListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // register commands and listeners
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand(this));
        getServer().getPluginManager().registerEvents(new FlyListener(this), this);

        File flyData = new File(getDataFolder() + File.separator + "player_data.yml");
        FileConfiguration flyDataConf = YamlConfiguration.loadConfiguration(flyData);
        flyDataConf.createSection("players");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
