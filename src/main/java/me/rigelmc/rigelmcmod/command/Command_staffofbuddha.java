package me.rigelmc.rigelmcmod.command;

import me.rigelmc.rigelmcmod.admin.Admin;
import me.rigelmc.rigelmcmod.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.rigelmc.rigelmcmod.shop.ShopData;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.ChatColor;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Gives you Thor's hammer", usage = "/<command>", aliases = "sob")
public class Command_staffofbuddha extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        Player init = null;
        Player targetPlayer = playerSender;
        Admin target = getAdmin(playerSender);
        if (FUtil.isBuddhist(target.getName()))
        {
            playerSender.getInventory().addItem(plugin.sob.getStaffOfBuddha());
            msg("You have been given The Staff Of Buddha!", ChatColor.GREEN);
            return true;
        }
        else 
        {
            msg("You are not a Buddhist!", ChatColor.RED);
        }
        return true;
    }
}