package com.github.marenwynn.fakewalls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.data.Msg;

public class FWReloadCmd implements PluginCommand {

    private PluginMain pm;

    public FWReloadCmd(PluginMain pm) {
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        pm.reloadConfig();
        pm.getData().loadConfig();

        for (Player p : pm.getServer().getOnlinePlayers()) {
            pm.clearSelection(p.getName());
        }

        pm.clearSelection("CONSOLE");
        Msg.FW_RELOADED.sendTo(sender);
        return true;
    }

    @Override
    public boolean isConsoleExecutable() {
        return true;
    }

    @Override
    public boolean hasRequiredPerm(CommandSender sender) {
        return sender.hasPermission("fw.admin");
    }

}
