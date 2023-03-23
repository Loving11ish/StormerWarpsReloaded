package me.loving11ish.stormerwarpsreloaded.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class Message {

    private static final String PLUGIN_NAME = "Stormer's Warps";
    private static final ChatColor BRACKETS_COLOR;
    private static final ChatColor NAME_COLOR;
    private static final ChatColor MESSAGE_COLOR;
    private static final ChatColor MESSAGE_ERROR_COLOR;

    static {
        BRACKETS_COLOR = ChatColor.LIGHT_PURPLE;
        NAME_COLOR = ChatColor.RED;
        MESSAGE_COLOR = ChatColor.GREEN;
        MESSAGE_ERROR_COLOR = ChatColor.RED;
    }

    public Message() {
    }

    public static void normal(CommandSender p, String strg) {
        String m = BRACKETS_COLOR + "[" + NAME_COLOR + "Stormer's Warps" + BRACKETS_COLOR + "] " + MESSAGE_COLOR + strg;
        p.sendMessage(ColorUtils.translateColorCodes(m));
    }

    public static void normal(String strg) {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while(var2.hasNext()) {
            Player p = (Player)var2.next();
            normal((CommandSender)p, ColorUtils.translateColorCodes(strg));
        }

    }

    public static void normal(String strg, List<String> p) {
        Iterator var3 = Bukkit.getOnlinePlayers().iterator();

        while(var3.hasNext()) {
            Player pls = (Player)var3.next();
            if (p.contains(pls.getName())) {
                normal((CommandSender)pls, ColorUtils.translateColorCodes(strg));
            }
        }

    }

    public static void error(CommandSender p, String strg) {
        String m = BRACKETS_COLOR + "[" + NAME_COLOR + "Error" + BRACKETS_COLOR + "] " + MESSAGE_ERROR_COLOR + strg;
        p.sendMessage(ColorUtils.translateColorCodes(m));
    }

    public static void error(String strg) {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while(var2.hasNext()) {
            Player p = (Player)var2.next();
            error((CommandSender)p, ColorUtils.translateColorCodes(strg));
        }

    }

    public static void error(String strg, List<String> p) {
        Iterator var3 = Bukkit.getOnlinePlayers().iterator();

        while(var3.hasNext()) {
            Player pls = (Player)var3.next();
            if (p.contains(pls.getName())) {
                error((CommandSender)pls, ColorUtils.translateColorCodes(strg));
            }
        }

    }

    public static void systemNormal(String strg) {
        String m = BRACKETS_COLOR + "[" + NAME_COLOR + "Stormer's Warps" + BRACKETS_COLOR + "] " + MESSAGE_COLOR + strg;
        Bukkit.getConsoleSender().sendMessage(ColorUtils.translateColorCodes(m));
    }

    public static void systemError(String strg) {
        String m = BRACKETS_COLOR + "[" + NAME_COLOR + "Stormer's Warps" + BRACKETS_COLOR + "] " + MESSAGE_ERROR_COLOR + strg;
        Bukkit.getConsoleSender().sendMessage(ColorUtils.translateColorCodes(m));
    }
}
