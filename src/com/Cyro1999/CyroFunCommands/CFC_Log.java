package com.Cyro1999.CyroFunCommands;

import java.util.logging.Level;
import java.util.logging.Logger;

// Credits to Madgeek and Darth

public class CFC_Log
{
    private static final Logger logger = Logger.getLogger("Minecraft-Server");

    private CFC_Log()
    {
        throw new AssertionError();
    }

    private static void log(Level level, String message, boolean raw)
    {
        logger.log(level, (raw ? "" : "[StormingMCCore]: ") + message);
    }

    public static void info(String message)
    {
        CFC_Log.info(message, false);
    }

    public static void info(String message, boolean raw)
    {
        CFC_Log.log(Level.INFO, message, raw);
    }

    public static void warning(String message)
    {
        CFC_Log.info(message, false);
    }

    public static void warning(String message, boolean raw)
    {
        CFC_Log.log(Level.WARNING, message, raw);
    }

    public static void severe(String message)
    {
        CFC_Log.info(message, false);
    }

    public static void severe(String message, boolean raw)
    {
        CFC_Log.log(Level.SEVERE, message, raw);
    }

    public static void severe(Throwable ex)
    {
        logger.log(Level.SEVERE, null, ex);
    }
}