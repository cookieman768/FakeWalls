package com.github.marenwynn.fakewalls.listeners;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.Selection;
import com.github.marenwynn.fakewalls.Util;
import com.github.marenwynn.fakewalls.data.FakeWall;
import com.github.marenwynn.fakewalls.data.Msg;
import com.github.marenwynn.fakewalls.lib.BlockChangeArray;
import com.github.marenwynn.fakewalls.lib.WrapperPlayServerMultiBlockChange;
import com.github.marenwynn.fakewalls.tasks.FakeBlockChange;

public class PlayerListener implements Listener {

    private PluginMain pm;

    public PlayerListener(PluginMain pm) {
        this.pm = pm;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent clickEvent) {
        Action a = clickEvent.getAction();

        if (a != Action.LEFT_CLICK_BLOCK && a != Action.RIGHT_CLICK_BLOCK)
            return;

        Player p = clickEvent.getPlayer();
        Location loc = clickEvent.getClickedBlock().getLocation();

        if (clickEvent.getClickedBlock().getType() == Material.PISTON_MOVING_PIECE) {
            for (FakeWall fw : pm.getData().getAllFakeWalls()) {
                if (!p.getWorld().getName().equals(fw.getMinCorner().getWorld().getName()))
                    continue;

                if (Util.isWithinBounds(loc, fw.getMinCorner(), fw.getMaxCorner(), 0)) {
                    if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.WOOD_SPADE) {
                        pm.selectWall(p, fw);
                    }

                    new FakeBlockChange(p, fw, loc).runTask(pm);
                    clickEvent.setCancelled(true);
                    return;
                }
            }
        }

        if (p.getItemInHand() == null || p.getItemInHand().getType() != Material.WOOD_SPADE
                || !p.hasPermission("fw.admin"))
            return;

        Selection s = pm.getSelection(p.getName());

        if (a == Action.RIGHT_CLICK_BLOCK) {
            s.setA(loc);
            Msg.CORNER_A_SELECTED.sendTo(p, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        } else {
            s.setB(loc);
            Msg.CORNER_B_SELECTED.sendTo(p, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }

        clickEvent.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent moveEvent) {
        Player p = moveEvent.getPlayer();

        for (FakeWall fw : pm.getData().getAllFakeWalls()) {
            String key = Util.getKey(fw.getName());
            boolean isLoaded = p.hasMetadata(key);

            if (!p.getWorld().getName().equals(fw.getMinCorner().getWorld().getName())) {
                if (isLoaded)
                    p.removeMetadata(key, pm);

                continue;
            }

            if (!Util.isWithinBounds(p.getLocation(), fw.getMinCorner(), fw.getMaxCorner(), pm.getData().VIEW_DISTANCE)) {
                if (isLoaded)
                    p.removeMetadata(key, pm);
            } else if (!isLoaded) {
                if (p.hasPermission("fw.access." + key) || !fw.isEnabled())
                    sendFakeWall(p, fw, false);
                else
                    sendFakeWall(p, fw, true);

                p.setMetadata(key, new FixedMetadataValue(pm, true));
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent worldEvent) {
        pm.clearSelection(worldEvent.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        pm.clearSelection(quitEvent.getPlayer().getName());
    }

    @SuppressWarnings("deprecation")
    public void sendFakeWall(Player p, FakeWall fw, boolean visible) {
        Location min = fw.getMinCorner();
        Location max = fw.getMaxCorner();
        int typeID = Material.AIR.getId();
        int meta = 0;

        if (visible) {
            typeID = fw.getBlockMaterial().getId();
            meta = (int) fw.getBlockData();
        }

        HashMap<Chunk, ArrayList<Location>> updates = new HashMap<Chunk, ArrayList<Location>>();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Location blockLocation = new Location(min.getWorld(), x, y, z);
                    Chunk c = blockLocation.getChunk();

                    if (!updates.containsKey(c))
                        updates.put(c, new ArrayList<Location>());

                    updates.get(c).add(blockLocation);
                }
            }
        }

        for (Chunk c : updates.keySet()) {
            ArrayList<Location> blockLocations = updates.get(c);
            BlockChangeArray blockChanges = new BlockChangeArray(blockLocations.size());

            for (int i = 0; i < blockLocations.size(); i++)
                blockChanges.getBlockChange(i).setLocation(blockLocations.get(i)).setBlockID(typeID).setMetadata(meta);

            WrapperPlayServerMultiBlockChange packet = new WrapperPlayServerMultiBlockChange();
            packet.setChunkX(c.getX());
            packet.setChunkZ(c.getZ());
            packet.setRecordData(blockChanges);

            try {
                pm.getProtocolManager().sendServerPacket(p, packet.getHandle());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
