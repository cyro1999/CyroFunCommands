package com.Cyro1999.CyroFunCommands.Commands;

import com.Cyro1999.CyroFunCommands.CFC_Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.BOTH)
@CommandParameters(description = "Destroy some pussy!", aliases = "destroypussy", usage = "/<command> [player]")
public class Command_pussy extends CFC_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        if (!sender.getName().equals("cyro1999")) {
            sender.sendMessage(ChatColor.RED + CFC_Messages.MSG_NO_PERMS);
            return true;
        }
        
        if (args.length != 1) {
            return false;
        }
        
        final Player player = Bukkit.getPlayer(args[0]);
        
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }
        
        final Location targetPos = player.getLocation();
        
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                final Location strike_pos = new Location(targetPos.getWorld(), targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                targetPos.getWorld().strikeLightning(strike_pos);
            }
        }
        
        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Destroying " + player.getName() + "'s Pussy");
        player.setHealth(0);
        player.setOp(false);
        return true;
    }
    
}