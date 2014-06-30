package com.github.marenwynn.fakewalls.data;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class GridLocation implements Serializable {

    private static final long serialVersionUID = 5672567575166440591L;

    private String            worldName;
    private int               x, y, z;

    public GridLocation(Location loc) {
        setLocation(loc);
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    public void setLocation(Location loc) {
        worldName = loc.getWorld().getName();
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();
    }

}
