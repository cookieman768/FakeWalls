package com.github.marenwynn.fakewalls.data;

import org.bukkit.command.CommandSender;

import com.github.marenwynn.fakewalls.Util;

public enum Msg {

    BORDER("&2------------------------------"),
    CORNER_A_SELECTED("&eFirst Corner @ (X: %d Y: %d Z: %d)"),
    CORNER_B_SELECTED("&eSecond Corner @ (X: %d Y: %d Z: %d)"),
    FW_RELOADED("&aFakeWalls &freloaded."),
    INVALID_DAMAGE_VALUE("&cError: &fInvalid damage value."),
    INVALID_MATERIAL("&cError: &fInvalid material."),
    LIST_WALLS("&aWalls: &6%s"),
    MATERIAL_SET("&6%s: &fBlock type set to &a%s&6:&a%d&f."),
    NO_CONSOLE("&cError: &fCommand not available from CONSOLE."),
    NO_PERM("&cError: &fAccess denied."),
    NO_SELECTION("&cError: &fNo selection made."),
    NO_WALL_SELECTED("&cError: &fNo wall selected."),
    NO_WALLS("&f&oNone"),
    USAGE_FW_CREATE("&cUsage: &f/fw create <name ...>"),
    USAGE_FW_SELECT("&cUsage: &f/fw select <name ...>"),
    USAGE_FW_TYPE("&cUsage: &f/fw type <Material>"),
    SELECT_1(" &aX Range: &f%d &6to &f%d"),
    SELECT_2(" &aY Range: &f%d &6to &f%d"),
    SELECT_3(" &aZ Range: &f%d &6to &f%d"),
    SELECT_4(" &aBlock Type: &f%s&6:&f%d"),
    SELECT_TITLE(" &6%s"),
    SELECTION_CLEARED("&aSelection cleared."),
    SELECTION_OVERLAP("&cError: &fSelection overlaps with &6%s&f."),
    WALL_CREATED("&6%s &acreated successfully."),
    WALL_EXISTS("&cError: &fA wall with the name \"&6%s&f\" already exists."),
    WALL_NOT_FOUND("&cError: &fNo wall with the name \"%s\" found."),
    WALL_REMOVED("&aWall &6%s &aremoved."),
    WALL_SELECTED("&6%s &aselected.");

    private final String defaultMsg;

    Msg(String defaultMsg) {
        this.defaultMsg = defaultMsg;
    }

    public String getDefaultMsg() {
        return defaultMsg;
    }

    @Override
    public String toString() {
        return Database.getMsg(this);
    }

    public void sendTo(CommandSender sender) {
        sender.sendMessage(toString());
    }

    public void sendTo(CommandSender sender, Object... args) {
        sender.sendMessage(Util.color(String.format(toString(), args)));
    }

}