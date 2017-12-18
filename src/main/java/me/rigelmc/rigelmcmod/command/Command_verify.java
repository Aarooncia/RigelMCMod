package me.rigelmc.rigelmcmod.command;

import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.player.FPlayer;
import me.rigelmc.rigelmcmod.admin.Admin;
import me.rigelmc.rigelmcmod.util.FUtil;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import java.util.Random;
import java.util.Date;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.pravian.aero.util.Ips;
import org.bukkit.scheduler.BukkitRunnable;

@CommandPermissions(level = Rank.IMPOSTOR, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Sends a verification code to the player, or the player can input the sent code.", usage = "/<command> [code]")
public class Command_verify extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!plugin.dc.enabled)
        {
            msg("The discord verification system is currently disabled", ChatColor.RED);
            return true;
        }

        if (!plugin.al.isAdminImpostor(playerSender))
        {
            msg("You are not an imposter, therefore you do not need to verify", ChatColor.RED);
            return true;
        }

        Admin admin = plugin.al.getEntryByName(playerSender.getName());

        if (admin.getDiscordID() == null)
        {
            msg("You do not have a discord account linked to your minecraft account, please verify the manual way.", ChatColor.RED);
            return true;
        }

        if (args.length < 1)
        {
            String code = "";
            Random random = new Random();
            for (int i = 0; i < 10; i++)
            {
                code += random.nextInt(10);
            }
            plugin.dc.VERIFY_CODES.add(code);
            final String c = code;

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (plugin.dc.VERIFY_CODES.contains(c))
                    {
                        plugin.dc.VERIFY_CODES.remove(c);
                    }
                }
            }.runTaskLater(plugin, 60L * 20L * 5L);

            PrivateChannel channel = plugin.dc.bot.getUserById(admin.getDiscordID()).openPrivateChannel().complete();
            channel.sendMessage("Please copy and paste the following into your chat: `/verify " + code + "`."
                    + "\nIf you did not request for verification, you can safely ignore it."
                    + "\nThe code will expire in 5 minutes.").complete();
            msg("A verification code has been sent to your DM! The code will expire in 5 minutes.", ChatColor.GREEN);
        }
        else
        {
            String code = args[0];
            if (!plugin.dc.VERIFY_CODES.contains(code))
            {
                msg("You have entered an invalid verification code", ChatColor.RED);
                return true;
            }
            else
            {
                plugin.dc.VERIFY_CODES.remove(code);
                FUtil.bcastMsg(playerSender.getName() + " has verified through the discord verification system!", ChatColor.GOLD);
                FUtil.adminAction(ConfigEntry.SERVER_NAME.getString(), "Readding " + admin.getName() + " to the admin list", true);
                admin.setName(playerSender.getName());
                admin.addIp(Ips.getIp(playerSender));
                admin.setActive(true);
                admin.setLastLogin(new Date());
                plugin.al.save();
                plugin.al.updateTables();
                final FPlayer fPlayer = plugin.pl.getPlayer(playerSender);
                if (fPlayer.getFreezeData().isFrozen())
                {
                    fPlayer.getFreezeData().setFrozen(false);
                    msg("You have been unfrozen.");
                }
            }
        }
        return true;
    }
}
