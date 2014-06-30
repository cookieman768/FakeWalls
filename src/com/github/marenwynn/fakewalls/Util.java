package com.github.marenwynn.fakewalls;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Util {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static boolean isWithinBounds(Location playerLoc, Location minCorner, Location maxCorner, int viewDistance) {
        int pX = playerLoc.getBlockX();
        int pY = playerLoc.getBlockY();
        int pZ = playerLoc.getBlockZ();

        int minX = minCorner.getBlockX() - viewDistance;
        int minY = minCorner.getBlockY() - viewDistance;
        int minZ = minCorner.getBlockZ() - viewDistance;

        int maxX = maxCorner.getBlockX() + viewDistance;
        int maxY = maxCorner.getBlockY() + viewDistance;
        int maxZ = maxCorner.getBlockZ() + viewDistance;

        return (pX >= minX && pX <= maxX && pY >= minY && pY <= maxY && pZ >= minZ && pZ <= maxZ);
    }

    public static String getKey(String string) {
        return ChatColor.stripColor(string.toLowerCase());
    }

}
