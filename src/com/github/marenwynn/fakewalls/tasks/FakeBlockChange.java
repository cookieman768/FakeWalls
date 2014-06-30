package com.github.marenwynn.fakewalls.tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.marenwynn.fakewalls.Util;
import com.github.marenwynn.fakewalls.data.FakeWall;

public class FakeBlockChange extends BukkitRunnable {

    private Player   p;
    private FakeWall fw;
    private Location loc;

    public FakeBlockChange(Player p, FakeWall fw, Location loc) {
        this.p = p;
        this.fw = fw;
        this.loc = loc;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        if (p.hasPermission("fw.access." + Util.getKey(fw.getName())) || !fw.isEnabled())
            p.sendBlockChange(loc, Material.AIR, (byte) 0);
        else
            p.sendBlockChange(loc, fw.getBlockMaterial(), (byte) fw.getBlockData());
    }

}
