package com.github.marenwynn.fakewalls.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.Selection;
import com.github.marenwynn.fakewalls.Util;
import com.github.marenwynn.fakewalls.data.FakeWall;
import com.github.marenwynn.fakewalls.data.Msg;

public class FWCreateCmd implements PluginCommand {

    private PluginMain pm;

    public FWCreateCmd(PluginMain pm) {
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        if (args.length < 2) {
            Msg.USAGE_FW_CREATE.sendTo(p);
            return true;
        }

        Selection s = pm.getSelection(p.getName());

        if (s == null || !s.isSelection()) {
            Msg.NO_SELECTION.sendTo(sender);
            return true;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]);

            if (i < args.length - 1)
                sb.append(" ");
        }

        String wallName = Util.color(sb.toString());

        if (pm.getData().getFakeWall(wallName) != null) {
            Msg.WALL_EXISTS.sendTo(p, ChatColor.stripColor(wallName));
            return true;
        }

        ArrayList<Location> blockLocations = new ArrayList<Location>();

        for (int x = s.getMin().getBlockX(); x <= s.getMax().getBlockX(); x++) {
            for (int y = s.getMin().getBlockY(); y <= s.getMax().getBlockY(); y++) {
                for (int z = s.getMin().getBlockZ(); z <= s.getMax().getBlockZ(); z++) {
                    Location loc = new Location(p.getWorld(), x, y, z);

                    for (FakeWall fw : pm.getData().getAllFakeWalls()) {
                        if (Util.isWithinBounds(loc, fw.getMinCorner(), fw.getMaxCorner(), 0)) {
                            Msg.SELECTION_OVERLAP.sendTo(p, fw.getName());
                            return true;
                        }
                    }

                    blockLocations.add(loc);
                }
            }
        }

        for (Location loc : blockLocations)
            loc.getBlock().setType(Material.PISTON_MOVING_PIECE);

        FakeWall fw = new FakeWall(wallName, s.getMin(), s.getMax());

        s.setFakeWall(fw);
        pm.getData().addFakeWall(fw);
        Msg.WALL_CREATED.sendTo(p, wallName);
        return true;
    }

    @Override
    public boolean isConsoleExecutable() {
        return false;
    }

    @Override
    public boolean hasRequiredPerm(CommandSender sender) {
        return sender.hasPermission("fw.admin");
    }

}
