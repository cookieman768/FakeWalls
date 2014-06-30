package com.github.marenwynn.fakewalls.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface PluginCommand extends CommandExecutor {

    public boolean isConsoleExecutable();

    public boolean hasRequiredPerm(CommandSender sender);

}
