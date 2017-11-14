/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.rigelmc.rigelmcmod.command;

import me.rigelmc.rigelmcmod.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.rigelmc.rigelmcmod.shop.ShopData;
import org.bukkit.ChatColor;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Gives you the Magic Wand", usage = "/<command>", aliases = "mw")
public class Command_magicwand extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        ShopData sd = plugin.sh.getData(playerSender);
        if (!sd.isMagicWand())
        {
            msg("You have not yet purchased the Magic Wand from the shop!", ChatColor.RED);
            return true;
        }
        playerSender.getInventory().addItem(plugin.mw.getMagicWand());
        msg("You have been given the Magic Wand!", ChatColor.GREEN);
        return true;
    }
}