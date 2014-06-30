package com.github.marenwynn.fakewalls;

import org.bukkit.Location;

import com.github.marenwynn.fakewalls.data.FakeWall;

public class Selection {

    private Location a, b;
    private FakeWall fw;

    public FakeWall getFakeWall() {
        return fw;
    }

    public void setFakeWall(FakeWall fw) {
        this.fw = fw;
    }

    public boolean isSelection() {
        return (a != null && b != null);
    }

    public Location getMin() {
        return a;
    }

    public Location getMax() {
        return b;
    }

    public void setA(Location a) {
        this.a = a;
        minMaxCorners();
    }

    public void setB(Location b) {
        this.b = b;
        minMaxCorners();
    }

    public void minMaxCorners() {
        if (a != null && b != null) {
            int aX = Math.min(a.getBlockX(), b.getBlockX());
            int aY = Math.min(a.getBlockY(), b.getBlockY());
            int aZ = Math.min(a.getBlockZ(), b.getBlockZ());

            b.setX(a.getBlockX() + b.getBlockX() - aX);
            b.setY(a.getBlockY() + b.getBlockY() - aY);
            b.setZ(a.getBlockZ() + b.getBlockZ() - aZ);

            a.setX(aX);
            a.setY(aY);
            a.setZ(aZ);
        }
    }

}
