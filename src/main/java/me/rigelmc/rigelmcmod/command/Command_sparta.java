package me.rigelmc.rigelmcmod.command;

import me.rigelmc.rigelmcmod.admin.Admin;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@CommandPermissions(level = Rank.OP, source = SourceType.IN_GAME, blockHostConsole = true)
@CommandParameters(description = "Sparta the heck out of you.", usage = "/<command> <playername>")
public class Command_sparta extends FreedomCommand
{

    @Override
    public boolean run(final CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        playerSender.chat("THIS IS AGE OF RIGEL MC!");
        playerSender.setVelocity(new Vector(0, 1000, 0));
        playerSender.getWorld().createExplosion(playerSender.getLocation(), 0F, false);
        FUtil.bcastMsg(playerSender.getName() + " is now in SPARTA!", ChatColor.RED);
        playerSender.kickPlayer("THIS IS AGE OF RIGEL MC!");

        return true;
    }
}
