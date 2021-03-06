package me.rigelmc.rigelmcmod.command;

import java.util.ArrayList;
import java.util.List;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.rank.Displayable;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.IMPOSTOR, source = SourceType.BOTH)
@CommandParameters(description = "Lists the real names of all online players.", usage = "/<command> [-a | -i | -f | -v | -b]", aliases = "who")
public class Command_list extends FreedomCommand
{

    private static enum ListFilter
    {

        PLAYERS,
        ADMINS,
        VANISHED_ADMINS,
        FAMOUS_PLAYERS,
        IMPOSTORS;
    }

    @Override
    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length > 1)
        {
            return false;
        }

        if (FUtil.isFromHostConsole(sender.getName()))
        {
            final List<String> names = new ArrayList<>();
            for (Player player : server.getOnlinePlayers())
            {
                names.add(player.getName());
            }
            msg("There are " + names.size() + "/" + server.getMaxPlayers() + " players online:\n" + StringUtils.join(names, ", "), ChatColor.WHITE);
            return true;
        }

        final ListFilter listFilter;
        if (args.length == 1)
        {
            switch (args[0])
            {
                case "-a":
                    listFilter = ListFilter.ADMINS;
                    break;
                case "-v":
                    listFilter = ListFilter.VANISHED_ADMINS;
                    break;
                case "-i":
                    listFilter = ListFilter.IMPOSTORS;
                    break;
                case "-f":
                    listFilter = ListFilter.FAMOUS_PLAYERS;
                    break;
                default:
                    return false;
            }
        }
        else
        {
            listFilter = ListFilter.PLAYERS;
        }

        if (listFilter == ListFilter.VANISHED_ADMINS && !plugin.al.isAdmin(playerSender))
        {
            msg("/list [-a | -i | -f]", ChatColor.WHITE);
            return true;
        }

        final StringBuilder onlineStats = new StringBuilder();
        final StringBuilder onlineUsers = new StringBuilder();

        onlineStats.append(ChatColor.BLUE).append("There are ").append(ChatColor.RED).append(server.getOnlinePlayers().size() - Command_vanish.vanished.size());
        onlineStats.append(ChatColor.BLUE).append(" out of a maximum ").append(ChatColor.RED).append(server.getMaxPlayers());
        onlineStats.append(ChatColor.BLUE).append(" players online.");

        final List<String> names = new ArrayList<>();
        for (Player player : server.getOnlinePlayers())
        {
            if (listFilter == ListFilter.ADMINS && !plugin.al.isAdmin(player))
            {
                continue;
            }

            if (listFilter == ListFilter.ADMINS && Command_vanish.vanished.contains(player))
            {
                continue;
            }

            if (listFilter == ListFilter.VANISHED_ADMINS && !Command_vanish.vanished.contains(player))
            {
                continue;
            }

            if (listFilter == ListFilter.IMPOSTORS && !plugin.al.isAdminImpostor(player))
            {
                continue;
            }

            if (listFilter == ListFilter.FAMOUS_PLAYERS && !ConfigEntry.FAMOUS_PLAYERS.getList().contains(player.getName().toLowerCase()))
            {
                continue;
            }

            if (listFilter == ListFilter.PLAYERS && Command_vanish.vanished.contains(player))
            {
                continue;
            }
            Displayable display = plugin.rm.getDisplay(player);

            names.add(display.getColoredTag() + player.getName());
        }

        @SuppressWarnings("null")
        String playerType = listFilter == null ? "players" : listFilter.toString().toLowerCase().replace('_', ' ');

        onlineUsers.append("Connected ");
        onlineUsers.append(playerType + ": ");
        onlineUsers.append(StringUtils.join(names, ChatColor.WHITE + ", "));

        if (senderIsConsole)
        {
            sender.sendMessage(ChatColor.stripColor(onlineStats.toString()));
            sender.sendMessage(ChatColor.stripColor(onlineUsers.toString()));
        }
        else
        {
            sender.sendMessage(onlineStats.toString());
            sender.sendMessage(onlineUsers.toString());
        }
        names.clear();
        return true;
    }
}
