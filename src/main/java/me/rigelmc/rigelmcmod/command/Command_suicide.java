package me.rigelmc.rigelmcmod.command;

import me.rigelmc.rigelmcmod.admin.Admin;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Kill yourself", usage = "/<command>", aliases = "kms")
public class Command_suicide extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        Admin target = getAdmin(playerSender);

        if (FUtil.isBuddhist(target.getName()))
        {
            playerSender.setHealth(0);
            FUtil.bcastMsg(playerSender.getName() + " has killed themself, and is a curtain now!", ChatColor.RED);
            return true;
        }
        else if (FUtil.isGod(target.getName()))
        {
            playerSender.setHealth(0);
            FUtil.bcastMsg(playerSender.getName() + " has killed themself, but cannot die!", ChatColor.RED);
            return true;
        }
        else
        {
            playerSender.setHealth(0);
            FUtil.bcastMsg(playerSender.getName() + " has killed themself, RIP!", ChatColor.RED);
            return true;
        }

    }
}
