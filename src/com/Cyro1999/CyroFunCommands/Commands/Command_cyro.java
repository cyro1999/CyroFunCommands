package com.Cyro1999.CyroFunCommands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Display info about CyroFunCommands", aliases = "cyroo", usage = "/<command>")
public class Command_cyro extends CFC_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        sender.sendMessage(ChatColor.RED + "CyroFunCommands - Made by Cyro1999");
        sender.sendMessage(ChatColor.RED + "Made to add some additional fun commands to the server");
        sender.sendMessage(ChatColor.RED + "Version " + plugin.getDescription().getVersion());
        return true;
    }
    
}
