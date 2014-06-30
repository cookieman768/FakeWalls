package com.github.marenwynn.fakewalls;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.marenwynn.fakewalls.commands.FWCreateCmd;
import com.github.marenwynn.fakewalls.commands.FWListCmd;
import com.github.marenwynn.fakewalls.commands.FWReloadCmd;
import com.github.marenwynn.fakewalls.commands.FWRemoveCmd;
import com.github.marenwynn.fakewalls.commands.FWSelectCmd;
import com.github.marenwynn.fakewalls.commands.FWTypeCmd;
import com.github.marenwynn.fakewalls.commands.PluginCommand;
import com.github.marenwynn.fakewalls.data.Database;
import com.github.marenwynn.fakewalls.data.FakeWall;
import com.github.marenwynn.fakewalls.data.Msg;
import com.github.marenwynn.fakewalls.listeners.PlayerListener;

public class PluginMain extends JavaPlugin {

    private Database                       data;
    private HashMap<String, Selection>     playerSelections;
    private HashMap<String, PluginCommand> commands;

    private ProtocolManager                protocolLib;

    @Override
    public void onEnable() {
        saveResource("CHANGELOG.txt", true);

        data = new Database(this);
        playerSelections = new HashMap<String, Selection>();

        commands = new HashMap<String, PluginCommand>();
        commands.put("create", new FWCreateCmd(this));
        commands.put("list", new FWListCmd(this));
        commands.put("reload", new FWReloadCmd(this));
        commands.put("remove", new FWRemoveCmd(this));
        commands.put("select", new FWSelectCmd(this));
        commands.put("type", new FWTypeCmd(this));
        getCommand("fw").setExecutor(this);

        protocolLib = ProtocolLibrary.getProtocolManager();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        playerSelections = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fw")) {
            if (args.length > 0) {
                String key = args[0].toLowerCase();

                if (key.equals("c"))
                    key = "create";
                else if (key.equals("l"))
                    key = "list";
                else if (key.equals("r"))
                    key = "remove";
                else if (key.equals("s"))
                    key = "select";
                else if (key.equals("t"))
                    key = "type";

                if (commands.containsKey(key)) {
                    PluginCommand pc = commands.get(key);

                    if (!pc.isConsoleExecutable() && !(sender instanceof Player)) {
                        Msg.NO_CONSOLE.sendTo(sender);
                        return true;
                    }

                    if (!pc.hasRequiredPerm(sender)) {
                        Msg.NO_PERM.sendTo(sender);
                        return true;
                    }

                    return pc.onCommand(sender, command, label, args);
                }
            }
        }

        FakeWall fw = getSelection(sender.getName()).getFakeWall();

        if (fw != null)
            Msg.WALL_SELECTED.sendTo(sender, fw.getName());

        sender.sendMessage(getDescription().getName() + " v" + getDescription().getVersion() + " by Marenwynn.");
        return false;
    }

    public void selectWall(CommandSender sender, FakeWall fw) {
        Selection s = getSelection(sender.getName());
        Location min = fw.getMinCorner();
        Location max = fw.getMaxCorner();

        s.setFakeWall(fw);
        s.setA(min);
        s.setB(max);

        Msg.BORDER.sendTo(sender);
        Msg.SELECT_TITLE.sendTo(sender, fw.getName());
        Msg.BORDER.sendTo(sender);
        Msg.SELECT_1.sendTo(sender, min.getBlockX(), max.getBlockX());
        Msg.SELECT_2.sendTo(sender, min.getBlockY(), max.getBlockY());
        Msg.SELECT_3.sendTo(sender, min.getBlockZ(), max.getBlockZ());
        Msg.SELECT_4.sendTo(sender, fw.getBlockMaterial().toString(), fw.getBlockData());
        Msg.BORDER.sendTo(sender);
    }

    public Selection getSelection(String playerName) {
        if (!playerSelections.containsKey(playerName))
            playerSelections.put(playerName, new Selection());

        return playerSelections.get(playerName);
    }

    public void clearSelection(String playerName) {
        if (playerSelections.containsKey(playerName))
            playerSelections.remove(playerName);
    }

    public void clearSelections(FakeWall fw) {
        for (Selection s : playerSelections.values()) {
            if (s.getFakeWall() == fw) {
                s.setFakeWall(null);
                s.setA(null);
                s.setB(null);
            }
        }
    }

    public Database getData() {
        return data;
    }

    public ProtocolManager getProtocolManager() {
        return protocolLib;
    }

}
