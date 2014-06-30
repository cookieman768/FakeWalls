package com.github.marenwynn.fakewalls.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.Selection;
import com.github.marenwynn.fakewalls.data.FakeWall;
import com.github.marenwynn.fakewalls.data.Msg;

public class FWRemoveCmd implements PluginCommand {

    private PluginMain pm;

    public FWRemoveCmd(PluginMain pm) {
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Selection s = pm.getSelection(sender.getName());
        FakeWall fw = s.getFakeWall();

        if (fw == null) {
            Msg.NO_WALL_SELECTED.sendTo(sender);
            return true;
        }

        Location min = fw.getMinCorner();
        Location max = fw.getMaxCorner();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    min.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }

        pm.clearSelections(fw);
        pm.getData().removeFakeWall(fw);
        Msg.WALL_REMOVED.sendTo(sender, fw.getName());
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
