package com.github.marenwynn.fakewalls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.data.FakeWall;
import com.github.marenwynn.fakewalls.data.Msg;

public class FWListCmd implements PluginCommand {

    private PluginMain pm;

    public FWListCmd(PluginMain pm) {
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FakeWall[] fakeWalls = pm.getData().getAllFakeWalls();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < fakeWalls.length; i++) {
            sb.append(fakeWalls[i].getName());

            if (i < fakeWalls.length - 1)
                sb.append("&6, ");
        }

        if (sb.length() == 0)
            sb.append(Msg.NO_WALLS.toString());

        Msg.LIST_WALLS.sendTo(sender, sb.toString());
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
