package me.rigelmc.rigelmcmod.command;

import java.util.ArrayList;
import me.rigelmc.rigelmcmod.admin.Admin;
import me.rigelmc.rigelmcmod.rank.Displayable;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.util.FLog;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_IN_GAME, blockHostConsole = true)
@CommandParameters(description = "Vanish/unvanish yourself.", usage = "/<command>", aliases = "v")
public class Command_vanish extends FreedomCommand
{

    public static ArrayList<Player> vanished = new ArrayList<Player>();

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        final Displayable display = plugin.rm.getDisplay(playerSender);
        String loginMsg = display.getColoredLoginMessage();
        String displayName = display.getColor() + playerSender.getName();
        Admin admin = plugin.al.getAdmin(playerSender);
        if (vanished.contains(playerSender))
        {
            msg(ChatColor.GOLD + "You have been unvanished.");
            if (admin.hasLoginMessage())
            {
                loginMsg = FUtil.colorize(admin.getLoginMessage());
            }
            FUtil.bcastMsg(ChatColor.AQUA + playerSender.getName() + " is " + loginMsg);
            FUtil.bcastMsg(ChatColor.YELLOW + playerSender.getName() + " joined the game");
            Admin target = plugin.al.getAdmin(playerSender);

            if (!target.getAtag().isEmpty())
            {
                plugin.pl.getPlayer(playerSender).setTag(target.getAtag());
            }
            else
            {
                plugin.pl.getPlayer(playerSender).setTag(display.getColoredTag());
            }
            
            FLog.info(playerSender.getName() + " is no longer vanished.");
            for (Player player : server.getOnlinePlayers())
            {
                player.showPlayer(playerSender);
            }
            plugin.esb.setVanished(playerSender.getName(), false);
            playerSender.removePotionEffect(PotionEffectType.INVISIBILITY);
            playerSender.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            vanished.remove(playerSender);

            return true;
        }
        else if (!(vanished.contains(playerSender)))
        {
            msg(ChatColor.GOLD + "You have been vanished.");
            FUtil.bcastMsg(ChatColor.YELLOW + playerSender.getName() + " left the game");
            FLog.info(playerSender.getName() + " is now vanished.");
            for (Player player : server.getOnlinePlayers())
            {
                player.hidePlayer(playerSender);
            }
            plugin.esb.setVanished(playerSender.getName(), true);
            playerSender.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1000000, true, false));
            vanished.add(playerSender);
            return true;
        }

        return true;
    }
}
