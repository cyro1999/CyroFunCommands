package com.Cyro1999.CyroFunCommands.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(
        description = "testing command",
        aliases = "cyroo",
        usage = "/<command>")
public class Command_cyro extends CFC_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        playerMsg("Cyros command system works!", ChatColor.RED);
        
        
        return true;
    }
    
}
