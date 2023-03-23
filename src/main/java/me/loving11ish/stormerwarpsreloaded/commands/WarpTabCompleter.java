package me.loving11ish.stormerwarpsreloaded.commands;

import me.loving11ish.stormerwarpsreloaded.models.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WarpTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList();
        String s = "";
        int depth = 0;
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("warp") || cmd.getName().equalsIgnoreCase("delwarp") && p.isOp()) {
                Iterator var10 = Warp.getWarps(p.isOp()).iterator();

                while(true) {
                    do {
                        if (!var10.hasNext()) {
                            return list;
                        }

                        Warp warp = (Warp)var10.next();
                        s = warp.getName();
                    } while(args.length != depth && (args.length != depth + 1 || !s.toLowerCase().startsWith(args[depth].toLowerCase())));

                    list.add(s);
                }
            }
        }
        return list;
    }
}
