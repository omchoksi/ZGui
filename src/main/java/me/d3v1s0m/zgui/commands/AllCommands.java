package me.d3v1s0m.zgui.commands;

import io.github.znetworkw.znpcservers.commands.Command;
import io.github.znetworkw.znpcservers.commands.CommandInformation;
import io.github.znetworkw.znpcservers.commands.CommandSender;
import me.d3v1s0m.zgui.commands.inventory.MainGUI;
import org.bukkit.entity.Player;

import java.util.Map;

public class AllCommands extends Command {

    public AllCommands() {
        super("zgui");
    }

    @CommandInformation(arguments = {}, name = "", permission = "zgui.open")
    public void guiCommand(CommandSender sender, Map<String, String> args){
        Player player = sender.getPlayer();
        player.openInventory(new MainGUI(player).build());
    }
}
