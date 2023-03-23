package me.loving11ish.stormerwarpsreloaded.commands;

import me.loving11ish.stormerwarpsreloaded.StormerWarpsReloaded;
import me.loving11ish.stormerwarpsreloaded.models.Warp;
import me.loving11ish.stormerwarpsreloaded.utils.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class WarpCommand implements CommandExecutor {

    FileConfiguration messagesConfig = StormerWarpsReloaded.i.messagesFileManager.getMessagesConfig();

    public WarpCommand() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("warps")) {
                if (!p.isOp()||!p.hasPermission("stormerwarpsreloaded.use")||!p.hasPermission("stormerwarpsreloaded.admin")||!p.hasPermission("stormerwarpsreloaded.*")){
                    Message.error(p, messagesConfig.getString("command-error-no-permission"));
                    return true;
                }
                listWarps(p);
                return true;
            }

            Warp warp;
            if (cmd.getName().equalsIgnoreCase("warp")) {
                if (!p.isOp()||!p.hasPermission("stormerwarpsreloaded.use")||!p.hasPermission("stormerwarpsreloaded.admin")||!p.hasPermission("stormerwarpsreloaded.*")){
                    Message.error(p, messagesConfig.getString("command-error-no-permission"));
                    return true;
                }
                if (args.length == 0) {
                    Message.error(p, messagesConfig.getString("command-error-no-arg"));
                    return false;
                }

                warp = Warp.findWarp(args[0]);
                if (warp != null) {
                    if (warp.isOpOnly() && !p.isOp()) {
                        Message.error(p, messagesConfig.getString("command-error-op-only"));
                        return false;
                    }

                    warp.warp(p);
                    return true;
                }

                Message.error(p, messagesConfig.getString("command-error-no-warp") + args[0]);
                return false;
            }

            if (cmd.getName().equalsIgnoreCase("setwarp")) {
                if (!p.isOp()||!p.hasPermission("stormerwarpsreloaded.admin")||!p.hasPermission("stormerwarpsreloaded.*")) {
                    Message.error(p, messagesConfig.getString("command-error-op-only"));
                    return false;
                }

                if (args.length == 0) {
                    Message.error(p, messagesConfig.getString("command-error-no-setwarp-name"));
                    return true;
                }

                warp = Warp.findWarp(args[0]);
                if (warp != null) {
                    Message.error(p, messagesConfig.getString("command-error-warp-existing"));
                    return false;
                }

                boolean opOnly = false;
                if (args.length != 1) {
                    try {
                        opOnly = Boolean.parseBoolean(args[1]);
                    } catch (Exception var9) {
                        Message.error(messagesConfig.getString("command-error-opOnly-boolean") + args[1]);
                        return false;
                    }
                }

                new Warp(p.getLocation(), args[0], opOnly);
                Message.normal(p, messagesConfig.getString("command-set-successful").replace("%WARP%", args[0]));
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("delwarp")) {
                if (!p.isOp()||!p.hasPermission("stormerwarpsreloaded.admin")||!p.hasPermission("stormerwarpsreloaded.*")) {
                    Message.error(p, messagesConfig.getString("command-error-op-only"));
                    return false;
                }

                if (args.length == 0) {
                    Message.error(p, messagesConfig.getString("command-error-delwarp.line-1"));
                    Message.error(p, messagesConfig.getString("command-error-delwarp.line-2"));
                    return false;
                }

                warp = Warp.findWarp(args[0]);
                if (warp == null) {
                    Message.error(p, messagesConfig.getString("command-error-no-warp") + args[0]);
                    return false;
                }

                warp.delete();
                Message.normal(p, messagesConfig.getString("command-error-no-warp").replace("%WARP%", warp.getName()));
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("swreload")) {
                if (p.isOp()||p.hasPermission("stormerwarpsreloaded.admin")||p.hasPermission("stormerwarpsreloaded.*")) {
                    StormerWarpsReloaded.i.reload();
                    Message.normal(p, messagesConfig.getString("command-reload-successful"));
                    return true;
                }

                Message.error(p, messagesConfig.getString("command-error-no-permission"));
                return false;
            }
        }
        return false;
    }

    private static void listWarps(Player p) {
        Message.normal(p, StormerWarpsReloaded.i.messagesFileManager.getMessagesConfig().getString("command-list-header"));
        Iterator var2 = Warp.getWarps(p.isOp()).iterator();

        while(var2.hasNext()) {
            Warp warp = (Warp)var2.next();
            Message.normal(p, (warp.isOpOnly() ? ChatColor.GOLD : "") + " - " + warp.getName() + " - ");
        }

        Message.normal(p, StormerWarpsReloaded.i.messagesFileManager.getMessagesConfig().getString("command-list-footer"));
    }
}
