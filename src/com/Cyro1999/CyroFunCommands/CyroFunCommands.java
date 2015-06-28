package com.Cyro1999.CyroFunCommands;

import com.Cyro1999.CyroFunCommands.Commands.CFC_CommandHandler;
import com.Cyro1999.CyroFunCommands.Commands.CFC_CommandLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CyroFunCommands extends JavaPlugin {
   
   public static final String CFC_COMMAND_PATH = "com.Cyro1999.CyroFunCommands.Commands";
   public static final String CFC_COMMAND_PREFIX = "Command_";
   //
   public static FileConfiguration config;  
   public static CyroFunCommands plugin;
   public CFC_Util util;
   
   public void onLoad() {
        plugin = this;
    }
    
    public void onEnable() {
        CFC_Log.info("[CyroFunCommands] CyroFunCommands plugin by Cyro1999 enabled!");
        CFC_CommandLoader.getInstance().scan();
    }

    public void onDisable() {
        CFC_Log.info("[CyroFunCommands] CyroFunCommands plugin by Cyro1999 disabled!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return CFC_CommandHandler.handleCommand(sender, cmd, commandLabel, args);
    }
}
