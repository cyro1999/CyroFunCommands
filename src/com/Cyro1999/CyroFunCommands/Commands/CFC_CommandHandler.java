// Credit to the Superior Development team :)

package com.Cyro1999.CyroFunCommands.Commands;


import com.Cyro1999.CyroFunCommands.CFC_Log;
import com.Cyro1999.CyroFunCommands.CFC_Messages;
import com.Cyro1999.CyroFunCommands.CyroFunCommands;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CFC_CommandHandler
{
    public static final String SMC_COMMAND_PATH = CFC_Command.class.getPackage().getName(); // "me.StevenLawson.TotalFreedomMod.Commands";
    public static final String SMC_COMMAND_PREFIX = "Command_";

    public static boolean handleCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        final Player playerSender;
        final boolean senderIsConsole;

        if (sender instanceof Player)
        {
            senderIsConsole = false;
            playerSender = (Player) sender;

            CFC_Log.info(String.format("[PLAYER_COMMAND] %s (%s): /%s %s",
                    playerSender.getName(),
                    ChatColor.stripColor(playerSender.getDisplayName()),
                    commandLabel,
                    StringUtils.join(args, " ")), true);
        }
        else
        {
            senderIsConsole = true;
            playerSender = null;

            CFC_Log.info(String.format("[CONSOLE_COMMAND] %s: /%s %s",
                    sender.getName(),
                    commandLabel,
                    StringUtils.join(args, " ")), true);
        }

        final CFC_Command dispatcher;
        try
        {
            final ClassLoader classLoader = CyroFunCommands.class.getClassLoader();
            dispatcher = (CFC_Command) classLoader.loadClass(String.format("%s.%s%s",
                    SMC_COMMAND_PATH,
                    SMC_COMMAND_PREFIX,
                    cmd.getName().toLowerCase())).newInstance();
            dispatcher.setup(CyroFunCommands.plugin ,sender, dispatcher.getClass());
        }
        catch (Exception ex)
        {
            return true;
        }

        if (!dispatcher.senderHasPermission())
        {
            sender.sendMessage(CFC_Messages.MSG_NO_PERMS);
            return true;
        }

        try
        {
            return dispatcher.run(sender, playerSender, cmd, commandLabel, args, senderIsConsole);
        }
        catch (Exception ex)
        {

        }

        return true;
    }
}
