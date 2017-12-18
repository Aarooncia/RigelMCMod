package me.rigelmc.rigelmcmod.command;

import java.util.List;
import java.util.ArrayList;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.shop.ShopData;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Open the GUI to set your login message", usage = "/<command>", aliases = "lm")
public class Command_loginmessage extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!ConfigEntry.SHOP_ENABLED.getBoolean())
        {
            msg("The shop is currently disabled and this command is a shop item", ChatColor.RED);
            return true;
        }
        ShopData sd = plugin.sh.getData(playerSender);
        if (!sd.isCustomLoginMessage())
        {
            msg(plugin.sh.getShopPrefix() + ChatColor.RED + "You have not purchased " + ChatColor.BLUE + "Custom Login Messages" + ChatColor.RED + " yet!");
            return true;
        }
        Inventory i = server.createInventory(null, 9, ChatColor.AQUA + "Login Messages");
        ItemStack removeLoginMessage = new ItemStack(Material.BARRIER);
        ItemMeta meta = removeLoginMessage.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Remove current login message");
        removeLoginMessage.setItemMeta(meta);
        i.setItem(0, removeLoginMessage);
        ItemStack buddhismHotlineFan = newLoginMessage(new ItemStack(Material.NAME_TAG), "Buddhism Hotline Fan", ChatColor.YELLOW, "a fan of the &eBuddhism Hotline", playerSender);
        i.setItem(1, buddhismHotlineFan);
        ItemStack autist = newLoginMessage(new ItemStack(Material.NAME_TAG), "Autist", ChatColor.GREEN, "an &aAutist", playerSender);
        i.setItem(2, autist);
        ItemStack AntiJakePauler = newLoginMessage(new ItemStack(Material.NAME_TAG), "Anti Jake Pauler", ChatColor.RED, "an &cAnti Jake Pauler", playerSender);
        i.setItem(3, AntiJakePauler);
        ItemStack DepressedMemer = newLoginMessage(new ItemStack(Material.NAME_TAG), "Depressed Memer", ChatColor.GOLD, "a &6Depressed Memer", playerSender);
        i.setItem(4, DepressedMemer);
        playerSender.openInventory(i);
        return true;
    }

    public ItemStack newLoginMessage(ItemStack is, String name, ChatColor color, String message, Player player)
    {
        ItemMeta m = is.getItemMeta();
        m.setDisplayName(color + name);
        List<String> l = new ArrayList();
        l.add(ChatColor.AQUA + player.getName() + " is " + plugin.rm.getDisplay(player).getDeterminer() + " " + plugin.rm.getDisplay(player).getItalicColoredName());
        l.add(ChatColor.AQUA + "and " + FUtil.colorize(message));
        m.setLore(l);
        is.setItemMeta(m);
        return is;
    }

    public ItemStack newLoginMessage(Material mat, String name, ChatColor color, String message, Player player)
    {
        return newLoginMessage(new ItemStack(mat), name, color, message, player);
    }
}
