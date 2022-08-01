package me.wega.fly.commands;

import me.wega.fly.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FlyCommand implements CommandExecutor {

    private final Main mainClass;
    public FlyCommand(Main mainClass){
        this.mainClass = mainClass;
    }
    public void removePlayer(Player player){
        File flyData = new File(mainClass.getDataFolder() + File.separator + "player_data.yml");
        FileConfiguration flyDataConf = YamlConfiguration.loadConfiguration(flyData);
        flyDataConf.set("players." + player.getUniqueId(), null);
        try {
            flyDataConf.save(flyData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void flyMethod(Player player){
            if (player.getAllowFlight()){
                removePlayer(player);
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.RED + "Fly set OFF");
            } else if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                player.sendMessage(ChatColor.GREEN + "Fly set ON");
            }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0){
            if (sender.hasPermission("wega.fly")){
                if (sender instanceof Player player){
                    flyMethod(player);
                }
            }else {
                sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
            }
        }else if (args.length == 1) {
            if (sender.hasPermission("wega.fly")){
                Player target = Bukkit.getPlayer(args[0]);
                Player player = (Player) sender;
                float flySpeed;
                try {
                    flySpeed = Float.parseFloat(args[0]);
                    if (flySpeed >= -10 && flySpeed <= 10){
                        player.setFlySpeed(flySpeed / 10f);
                        player.sendMessage(ChatColor.GREEN + "Fly speed set to " + args[0]);
                    }else {
                        player.sendMessage(ChatColor.RED + "Max fly speed is 10!");
                    }
                } catch (NumberFormatException e) {
                    if (player.hasPermission("wega.admin")){
                        if (Objects.requireNonNull(target).isOnline()) {
                             if (Objects.requireNonNull(target).getAllowFlight()){
                                 target.sendMessage(ChatColor.YELLOW + "FLY set OFF by " + player.getName());
                            } else if (!target.getAllowFlight()){
                                 target.sendMessage(ChatColor.YELLOW + "FLY set ON by " + player.getName());
                             }
                        } else if (!target.isOnline()) {
                            player.sendMessage(ChatColor.RED + target.getName() + "is offline!");
                        }
                        flyMethod(Objects.requireNonNull(target));
                    } else if (!player.hasPermission("wega.admin")) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
                    }
                }
            }else if (sender.hasPermission("wega.fly")){
                sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
            }
        }else if (args.length == 2) {
            if (sender.hasPermission("wega.fly")){
                Player target = Bukkit.getPlayer(args[0]);
                Player player = (Player) sender;
                float flySpeed;
                try {
                    flySpeed = Float.parseFloat(args[1]);
                    if (flySpeed >= -10 && flySpeed <= 10){
                        Objects.requireNonNull(target).setFlySpeed(flySpeed / 10f);
                        player.sendMessage(ChatColor.GREEN + "Fly speed set to " + args[1] + " for " + target.getName());
                        target.sendMessage("Your fly speed has been set to " + args[1]);
                    }else {
                        player.sendMessage(ChatColor.RED + "Max fly speed is 10!");
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Not a valid number!");
                }
            }else if (sender.hasPermission("wega.fly")){
                sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
            }
        }
        return true;
    }
}
