package me.loving11ish.stormerwarpsreloaded.utils;

import me.loving11ish.stormerwarpsreloaded.StormerWarpsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class Message {

    private static FileConfiguration messagesConfig = StormerWarpsReloaded.i.messagesFileManager.getMessagesConfig();

    public static void normal(CommandSender p, String strg) {
        p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-prefix") + ColorUtils.translateColorCodes(strg)));
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
        p.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-prefix" + " [ERROR] ") + ColorUtils.translateColorCodes(strg)));
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
        Bukkit.getConsoleSender().sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-prefix") + ColorUtils.translateColorCodes(strg)));
    }

    public static void systemError(String strg) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("plugin-prefix" + " [ERROR] ") + ColorUtils.translateColorCodes(strg)));
    }
}
