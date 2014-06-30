package com.github.marenwynn.fakewalls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.Selection;
import com.github.marenwynn.fakewalls.Util;
import com.github.marenwynn.fakewalls.data.FakeWall;
import com.github.marenwynn.fakewalls.data.Msg;

public class FWSelectCmd implements PluginCommand {

    private PluginMain pm;

    public FWSelectCmd(PluginMain pm) {
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Selection s = pm.getSelection(sender.getName());

        if (args.length < 2) {
            if (s.getFakeWall() != null || s.getMin() != null || s.getMax() != null) {
                pm.clearSelection(sender.getName());
                Msg.SELECTION_CLEARED.sendTo(sender);
                return true;
            } else {
                Msg.USAGE_FW_SELECT.sendTo(sender);
                return true;
            }
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]);

            if (i < args.length - 1)
                sb.append(" ");
        }

        String wallName = Util.color(sb.toString());
        FakeWall fw = pm.getData().getFakeWall(wallName);

        if (fw == null) {
            Msg.WALL_NOT_FOUND.sendTo(sender, wallName);
            return true;
        }

        pm.selectWall(sender, fw);
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
