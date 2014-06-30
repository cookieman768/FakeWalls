package com.github.marenwynn.fakewalls.data;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.Material;

public class FakeWall implements Serializable {

    private static final long serialVersionUID = -6457612892043353385L;

    private String            name;
    private GridLocation      minCorner, maxCorner;
    private Material          blockMaterial;
    private short             blockData;
    private boolean           enabled;

    public FakeWall(String name, Location minCorner, Location maxCorner) {
        this.name = name;
        this.minCorner = new GridLocation(minCorner);
        this.maxCorner = new GridLocation(maxCorner);

        setBlockMaterial(Material.SMOOTH_BRICK);
        setBlockData((short) 0);
        setEnabled(true);
    }

    public String getName() {
        return name;
    }

    public Location getMinCorner() {
        return minCorner.getLocation();
    }

    public Location getMaxCorner() {
        return maxCorner.getLocation();
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public void setBlockMaterial(Material blockMaterial) {
        this.blockMaterial = blockMaterial;
    }

    public short getBlockData() {
        return blockData;
    }

    public void setBlockData(short blockData) {
        this.blockData = blockData;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
