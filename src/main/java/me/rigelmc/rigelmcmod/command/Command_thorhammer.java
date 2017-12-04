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
@CommandParameters(description = "Gives you Thor's hammer", usage = "/<command>")
public class Command_thorhammer extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        Player init = null;
        Player targetPlayer = playerSender;
        Admin target = getAdmin(playerSender);
        ShopData sd = plugin.sh.getData(playerSender);
        if (!sd.isThorHammer())
        {
            msg("You have not yet purchased Thor's hammer from the shop!", ChatColor.RED);
            return true;
        }
        if (!FUtil.isGod(target.getName()))
        {
            playerSender.getInventory().addItem(plugin.th.getThorHammer());
            msg("You have been given Thor's hammer!", ChatColor.GREEN);
            return true;
        }
        playerSender.getInventory().addItem(plugin.th.getThorHammer());
        msg("You have been given Thor's hammer!", ChatColor.GREEN);
        return true;
    }
}