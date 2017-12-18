package me.rigelmc.rigelmcmod.command;

import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Check the stats of the server", usage = "/<command>", aliases = "ss")
public class Command_serverstats extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        msg("-==" + ConfigEntry.SERVER_NAME.getString() + " Server Stats ==-", ChatColor.GOLD);
        msg("Total OPs: " + server.getOperators().size(), ChatColor.RED);
        msg("Total Staff: " + plugin.al.getAllAdmins().size() + " (" + plugin.al.getActiveAdmins().size() + " active)", ChatColor.BLUE);
        int tpbips = plugin.pm.getPermbannedIps().size();
        int tpbns = plugin.pm.getPermbannedNames().size();
        int tpbs = tpbips + tpbns;
        msg("Total Permbans: " + tpbs + " (" + tpbips + " ips " + tpbns + " names)", ChatColor.GREEN);
        msg("Freedom Command Count: " + plugin.cl.totalCommands, ChatColor.AQUA);
        msg("Total Blocked Deathbot IPs: " + plugin.asb.SPAMBOT_IPS.size(), ChatColor.YELLOW);
        return true;
    }
}
