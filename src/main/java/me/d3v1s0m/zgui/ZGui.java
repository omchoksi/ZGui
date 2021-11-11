package me.d3v1s0m.zgui;

import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.utility.SchedulerUtils;
import me.d3v1s0m.zgui.commands.AllCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZGui extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        boolean znpcs_present = Bukkit.getPluginManager().isPluginEnabled("ServersNPC");
        if (!znpcs_present){
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Couldn't find znpcs");
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "Found znpcs...");
        Plugin znpcs = Bukkit.getPluginManager().getPlugin("ServersNPC");
        ServersNPC.SCHEDULER = new SchedulerUtils(znpcs);
        new AllCommands();

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MyNPC enabled");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MyNPC disabled");
    }
}
