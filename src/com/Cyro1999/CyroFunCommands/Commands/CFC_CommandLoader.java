// Credit to the Superior Development team :)

package com.Cyro1999.CyroFunCommands.Commands;

import com.Cyro1999.CyroFunCommands.CFC_Log;
import com.Cyro1999.CyroFunCommands.CFC_Util;
import com.Cyro1999.CyroFunCommands.CyroFunCommands;
import java.io.IOException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

public class CFC_CommandLoader
{
    public static final Pattern CFC_COMMAND_CLASS_PATTERN = Pattern.compile(CyroFunCommands.CFC_COMMAND_PATH.replace('.', '/') + "/(" + CyroFunCommands.CFC_COMMAND_PREFIX + "[^\\$]+)\\.class");
    private List<CFC_CommandInfo> commandList = null;

    private CFC_CommandLoader()
    {
    }

    public void scan()
    {
        CommandMap commandMap = getCommandMap();
        if (commandMap == null)
        {
            CFC_Log.severe("Error loading commandMap.");
            return;
        }

        if (commandList == null)
        {
            commandList = getCommands();
        }

        for (CFC_CommandInfo commandInfo : commandList)
        {
            CFC_DynamicCommand dynamicCommand = new CFC_DynamicCommand(commandInfo);

            Command existing = commandMap.getCommand(dynamicCommand.getName());
            if (existing != null)
            {
                unregisterCommand(existing, commandMap);
            }

            commandMap.register(CyroFunCommands.plugin.getDescription().getName(), dynamicCommand);
        }

        CFC_Log.info("CyroFunCommands commands loaded.");
    }

    public void unregisterCommand(String commandName)
    {
        CommandMap commandMap = getCommandMap();
        if (commandMap != null)
        {
            Command command = commandMap.getCommand(commandName.toLowerCase());
            if (command != null)
            {
                unregisterCommand(command, commandMap);
            }
        }
    }

    public void unregisterCommand(Command command, CommandMap commandMap)
    {
        try
        {
            command.unregister(commandMap);
            HashMap<String, Command> knownCommands = getKnownCommands(commandMap);
            if (knownCommands != null)
            {
                knownCommands.remove(command.getName());
                for (String alias : command.getAliases())
                {
                    knownCommands.remove(alias);
                }
            }
        }
        catch (Exception ex)
        {
            CFC_Log.severe(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public CommandMap getCommandMap()
    {
        Object commandMap = CFC_Util.getField(Bukkit.getServer().getPluginManager(), "commandMap");
        if (commandMap != null)
        {
            if (commandMap instanceof CommandMap)
            {
                return (CommandMap) commandMap;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Command> getKnownCommands(CommandMap commandMap)
    {
        Object knownCommands = CFC_Util.getField(commandMap, "knownCommands");
        if (knownCommands != null)
        {
            if (knownCommands instanceof HashMap)
            {
                return (HashMap<String, Command>) knownCommands;
            }
        }
        return null;
    }

    
    private static List<CFC_CommandInfo> getCommands()
    {
        List<CFC_CommandInfo> commandList = new ArrayList<CFC_CommandInfo>();

        try
        {
            CodeSource codeSource = CyroFunCommands.class.getProtectionDomain().getCodeSource();
            if (codeSource != null)
            {
                ZipInputStream zip = new ZipInputStream(codeSource.getLocation().openStream());
                ZipEntry zipEntry;
                while ((zipEntry = zip.getNextEntry()) != null)
                {
                    String entryName = zipEntry.getName();
                    Matcher matcher = CFC_COMMAND_CLASS_PATTERN.matcher(entryName);
                    if (matcher.find())
                    {
                        try
                        {
                            Class<?> commandClass = Class.forName(CyroFunCommands.CFC_COMMAND_PATH + "." + matcher.group(1));

                            CommandPermissions commandPermissions = (CommandPermissions) commandClass.getAnnotation(CommandPermissions.class);
                            CommandParameters commandParameters = (CommandParameters) commandClass.getAnnotation(CommandParameters.class);

                            if (commandPermissions != null && commandParameters != null)
                            {
                                CFC_CommandInfo commandInfo = new CFC_CommandInfo(
                                        commandClass,
                                        matcher.group(1).split("_")[1],
                                        commandPermissions.source(),
                                        commandPermissions.block_Host_Console(),
                                        commandParameters.description(),
                                        commandParameters.usage(),
                                        commandParameters.aliases());

                                commandList.add(commandInfo);
                            }
                        }
                        catch (ClassNotFoundException ex)
                        {
                            CFC_Log.severe(ex);
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            CFC_Log.severe(ex);
        }

        return commandList;
    }

    public static class CFC_CommandInfo
    {
        private final String commandName;
        private final Class<?> commandClass;
        private final SourceType source;
        private final boolean blockHostConsole;
        private final String description;
        private final String usage;
        private final List<String> aliases;

        public CFC_CommandInfo(Class<?> commandClass, String commandName, SourceType source, boolean blockHostConsole, String description, String usage, String aliases)
        {
            this.commandName = commandName;
            this.commandClass = commandClass;
            this.source = source;
            this.blockHostConsole = blockHostConsole;
            this.description = description;
            this.usage = usage;
            this.aliases = ("".equals(aliases) ? new ArrayList<String>() : Arrays.asList(aliases.split(",")));
        }



        public List<String> getAliases()
        {
            return aliases;
        }

        public Class<?> getCommandClass()
        {
            return commandClass;
        }

        public String getCommandName()
        {
            return commandName;
        }

        public String getDescription()
        {
            return description;
        }

        public String getDescriptionPermissioned()
        {
            String _description = description;

            return _description;
        }


        public SourceType getSource()
        {
            return source;
        }

        public String getUsage()
        {
            return usage;
        }

        public boolean getBlockHostConsole()
        {
            return blockHostConsole;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("commandName: ").append(commandName);
            sb.append("\ncommandClass: ").append(commandClass.getName());
            sb.append("\nsource: ").append(source);
            sb.append("\nblock_host_console: ").append(blockHostConsole);
            sb.append("\ndescription: ").append(description);
            sb.append("\nusage: ").append(usage);
            sb.append("\naliases: ").append(aliases);
            return sb.toString();
        }
    }

    public class CFC_DynamicCommand extends Command implements PluginIdentifiableCommand
    {
        private final CFC_CommandInfo commandInfo;

        private CFC_DynamicCommand(CFC_CommandInfo commandInfo)
        {
            super(commandInfo.getCommandName(), commandInfo.getDescriptionPermissioned(), commandInfo.getUsage(), commandInfo.getAliases());

            this.commandInfo = commandInfo;
        }

        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args)
        {
            boolean success = false;

            if (!getPlugin().isEnabled())
            {
                return false;
            }

            try
            {
                success = getPlugin().onCommand(sender, this, commandLabel, args);
            }
            catch (Throwable ex)
            {
                
            }

            if (!success && getUsage().length() > 0)
            {
                for (String line : getUsage().replace("<command>", commandLabel).split("\n"))
                {
                    sender.sendMessage(line);
                }
            }

            return success;
        }

        @Override
        public Plugin getPlugin()
        {
            return CyroFunCommands.plugin;
        }

        public CFC_CommandInfo getCommandInfo()
        {
            return commandInfo;
        }
    }

    public static CFC_CommandLoader getInstance()
    {
        return TFM_CommandScannerHolder.INSTANCE;
    }

    private static class TFM_CommandScannerHolder
    {
        private static final CFC_CommandLoader INSTANCE = new CFC_CommandLoader();
    }
}
