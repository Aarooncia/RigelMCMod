package me.rigelmc.rigelmcmod.shop;

import java.util.ArrayList;
import java.util.List;
import me.rigelmc.rigelmcmod.FreedomService;
import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopGUIListener extends FreedomService
{

    public final List<Player> SET_LOGIN = new ArrayList<>();

    public ShopGUIListener(RigelMCMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player))
        {
            return;
        }
        Inventory i = event.getInventory();
        if (!i.getTitle().equals(plugin.sh.GUIName) && !i.getTitle().equals(ChatColor.AQUA + "Login Messages"))
        {
            return;
        }
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        ShopData sd = plugin.sh.getData(p);
        ItemStack is = event.getCurrentItem();
        String prefix = plugin.sh.getShopPrefix();
        if (i.getTitle().equals(plugin.sh.GUIName))
        {
            int coins = sd.getCoins();
            int coloredChatPrice = ConfigEntry.SHOP_COLORED_CHAT_PRICE.getInteger();
            int customLoginMessagePrice = ConfigEntry.SHOP_LOGIN_MESSAGE_PRICE.getInteger();
            int setLoginMessagePrice = ConfigEntry.SHOP_SET_LOGIN_MESSAGE_PRICE.getInteger();
            int thorHammerPrice = ConfigEntry.SHOP_THOR_HAMMER_PRICE.getInteger();
            int magicWandPrice = ConfigEntry.SHOP_MAGIC_WAND_PRICE.getInteger();
            int minigunPrice = ConfigEntry.SHOP_MINIGUN_PRICE.getInteger();

            if (is.getType().equals(Material.BOOK_AND_QUILL) && !sd.isColoredchat() && canAfford(coloredChatPrice, coins))
            {
                sd.setCoins(coins - coloredChatPrice);
                sd.setColoredchat(true);
                plugin.sh.save(sd);
                p.sendMessage(prefix + ChatColor.GREEN + "You have successfully bought " + ChatColor.BLUE + "Colored Chat" + ChatColor.GREEN + "! You may now use colored codes in chat, do /einfo colors for more information.");
                event.setCancelled(true);
                p.closeInventory();
            }

            else if (is.getType().equals(Material.NAME_TAG) && !sd.isCustomLoginMessage() && canAfford(customLoginMessagePrice, coins))
            {
                sd.setCoins(coins - customLoginMessagePrice);
                sd.setCustomLoginMessage(true);
                plugin.sh.save(sd);
                p.sendMessage(prefix + ChatColor.GREEN + "You have successfully bought " + ChatColor.BLUE + "Custom Login Messages" + ChatColor.GREEN + "! Do /loginmessage to set one!");
                event.setCancelled(true);
                p.closeInventory();
            }

            else if (is.getType().equals(Material.BANNER) && !sd.isCanSetOwnLogin() && canAfford(setLoginMessagePrice, coins))
            {
                sd.setCoins(coins - setLoginMessagePrice);
                sd.setCanSetOwnLogin(true);
                plugin.sh.save(sd);
                p.sendMessage(prefix + ChatColor.GREEN + "You have successfully bought " + ChatColor.BLUE + "Set Custom Login Message" + ChatColor.GREEN + "! Do /loginmessage "
                        + "and select Set Custom Login Message to set one.");
                SET_LOGIN.add(p);
            }

            else if (is.getType().equals(Material.IRON_PICKAXE) && !sd.isThorHammer() && canAfford(thorHammerPrice, coins))
            {
                sd.setCoins(coins - thorHammerPrice);
                sd.setThorHammer(true);
                plugin.sh.save(sd);
                p.sendMessage(prefix + ChatColor.GREEN + "You have successfully bought " + ChatColor.BLUE + "Thor's Hammer" + ChatColor.GREEN + "! Do /thorhammer to get one!");
                event.setCancelled(true);
                p.closeInventory();
            }

            else if (is.getType().equals(Material.STICK) && !sd.isMagicWand() && canAfford(magicWandPrice, coins))
            {
                sd.setCoins(coins - magicWandPrice);
                sd.setMagicWand(true);
                plugin.sh.save(sd);
                p.sendMessage(prefix + ChatColor.GREEN + "You have successfully bought " + ChatColor.RED + "Magic Wand" + ChatColor.GREEN + "! Do /magicwand to get one!");
                event.setCancelled(true);
                p.closeInventory();
            }

            else if (is.getType().equals(Material.IRON_BARDING) && !sd.isMinigun() && canAfford(minigunPrice, coins))
            {
                sd.setCoins(coins - minigunPrice);
                sd.setMinigun(true);
                plugin.sh.save(sd);
                p.sendMessage(prefix + ChatColor.GREEN + "You have successfully bought the " + ChatColor.RED + "Minigun" + ChatColor.GREEN + "! Do /minigun to get one!");
                event.setCancelled(true);
                p.closeInventory();
            }
        }

        else if (i.getTitle().equals(ChatColor.AQUA + "Login Messages"))
        {
            if (is.getType().equals(Material.BARRIER))
            {
                sd.setLoginMessage(null);
                plugin.sh.save(sd);
                p.closeInventory();
                p.sendMessage(ChatColor.GREEN + "Successfully removed your current login message!");
            }
            else if (is.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Buddhism Hotline Fan"))
            {
                sd.setLoginMessage("&ba fan of the &eBuddhism Hotline");
                plugin.sh.save(sd);
                p.closeInventory();
                p.sendMessage(ChatColor.GREEN + "Your login message is now " + createLoginMessage(p, sd.getLoginMessage()));
            }
            else if (is.getItemMeta().getDisplayName().equals(ChatColor.RED + "Anti Jake Pauler"))
            {
                sd.setLoginMessage("&ban &cAnti Jake Pauler");
                plugin.sh.save(sd);
                p.closeInventory();
                p.sendMessage(ChatColor.GREEN + "Your login message is now " + createLoginMessage(p, sd.getLoginMessage()));
            }
            else if (is.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Depressed Memer"))
            {
                sd.setLoginMessage("&ba &6Depressed Memer");
                plugin.sh.save(sd);
                p.closeInventory();
                p.sendMessage(ChatColor.GREEN + "Your login message is now " + createLoginMessage(p, sd.getLoginMessage()));
            }
            else if (is.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Custom Login Message"))
            {
                if (sd.isCanSetOwnLogin())
                {
                    if (!SET_LOGIN.contains(p))
                    {
                        SET_LOGIN.add(p);
                    }
                }

                p.closeInventory();
                p.sendMessage((sd.isCanSetOwnLogin() ? ChatColor.GREEN + "You may now do /loginmessage <message>" : ChatColor.RED + "You haven't bought the custom login message from the shop!"));
            }
        }
    }

    public String createLoginMessage(Player player, String msg)
    {
        String loginMessage = ChatColor.AQUA + player.getName() + " is " + plugin.rm.getDisplay(player).getDeterminer() + " "
                + plugin.rm.getDisplay(player).getItalicColoredName() + ChatColor.AQUA + " and " + FUtil.colorize(msg);
        return loginMessage;
    }

    public boolean canAfford(int price, int coins)
    {
        return (coins >= price);
    }
}
