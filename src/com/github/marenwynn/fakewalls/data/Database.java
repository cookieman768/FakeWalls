package com.github.marenwynn.fakewalls.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.github.marenwynn.fakewalls.PluginMain;
import com.github.marenwynn.fakewalls.Util;

public class Database {

    private PluginMain                 pm;

    private File                       wallData;
    private HashMap<String, FakeWall>  fakeWalls;

    public static HashMap<Msg, String> messages;
    public int                         VIEW_DISTANCE;

    public Database(PluginMain pm) {
        this.pm = pm;

        fakeWalls = new HashMap<String, FakeWall>();
        wallData = new File(pm.getDataFolder().getPath() + File.separator + "walls.db");
        messages = new HashMap<Msg, String>();

        loadConfig();

        if (wallData.exists())
            load();
    }

    public void loadConfig() {
        pm.saveDefaultConfig();
        messages.clear();

        for (Msg msg : Msg.values())
            messages.put(msg, pm.getConfig().getString("FakeWalls.Messages." + msg.name(), msg.getDefaultMsg()));

        VIEW_DISTANCE = pm.getConfig().getInt("FakeWalls.VIEW_DISTANCE");
    }

    public FakeWall getFakeWall(String wallName) {
        String key = Util.getKey(wallName);

        if (fakeWalls.containsKey(key))
            return fakeWalls.get(key);

        return null;
    }

    public void addFakeWall(FakeWall fw) {
        fakeWalls.put(Util.getKey(fw.getName()), fw);
        save();
    }

    public void removeFakeWall(FakeWall fw) {
        fakeWalls.remove(Util.getKey(fw.getName()));
        save();
    }

    public FakeWall[] getAllFakeWalls() {
        return fakeWalls.values().toArray(new FakeWall[0]);
    }

    public void load() {
        FakeWall[] fakeWalls = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(wallData));

            try {
                fakeWalls = (FakeWall[]) ois.readObject();
            } finally {
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (fakeWalls != null)
            for (FakeWall fw : fakeWalls)
                this.fakeWalls.put(Util.getKey(fw.getName()), fw);
    }

    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(wallData));

            try {
                oos.writeObject(getAllFakeWalls());
            } finally {
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMsg(Msg msg) {
        return Util.color(messages.get(msg));
    }

}
