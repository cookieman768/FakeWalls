package com.github.marenwynn.fakewalls.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.Selection;
import com.github.marenwynn.fakewalls.Util;
import com.github.marenwynn.fakewalls.data.FakeWall;
import com.github.marenwynn.fakewalls.data.Msg;

public class FWTypeCmd implements PluginCommand {

    private PluginMain pm;

    public FWTypeCmd(PluginMain pm) {
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

        if (args.length < 2) {
            Msg.USAGE_FW_TYPE.sendTo(sender);
            return true;
        }

        short data = (short) 0;
        String[] input = args[1].split(":");
        Material m = Material.matchMaterial(input[0]);

        if (input.length > 1) {
            try {
                data = Short.parseShort(input[1]);
            } catch (NumberFormatException e) {
                Msg.INVALID_DAMAGE_VALUE.sendTo(sender);
                return true;
            }
        }

        if (m == null) {
            Msg.INVALID_MATERIAL.sendTo(sender);
            return true;
        }

        fw.setBlockMaterial(m);
        fw.setBlockData(data);
        pm.getData().save();

        String key = Util.getKey(fw.getName());

        for (Player p : pm.getServer().getOnlinePlayers())
            if (p.hasMetadata(key))
                p.removeMetadata(key, pm);

        Msg.MATERIAL_SET.sendTo(sender, fw.getName(), fw.getBlockMaterial(), fw.getBlockData());
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
