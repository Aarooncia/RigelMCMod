package me.rigelmc.rigelmcmod.rank;

import me.rigelmc.rigelmcmod.FreedomService;
import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.admin.Admin;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.player.FPlayer;
import me.rigelmc.rigelmcmod.shop.ShopData;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankManager extends FreedomService
{

    public RankManager(RigelMCMod plugin)
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

    public Displayable getDisplay(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return getRank(sender); // Consoles don't have display ranks
        }

        final Player player = (Player) sender;

        // Display impostors
        if (plugin.al.isAdminImpostor(player))
        {
            return Rank.IMPOSTOR;
        }
        if (ConfigEntry.SERVER_BUDDHISTS.getList().contains(player.getName()) && !plugin.al.isAdmin(player))
        {
            return Title.BUDDHIST;
        }
        if (ConfigEntry.SERVER_GODS.getList().contains(player.getName()) && !plugin.al.isAdmin(player))
        {
            return Title.GOD;
        }
        if (ConfigEntry.SERVER_MASTER_BUILDERS.getList().contains(player.getName()) && !plugin.al.isAdmin(player))
        {
            return Title.MASTER_BUILDER;
        }
        // Developers always show up
        if (FUtil.TFDEVS.contains(player.getName()))
        {
            return Title.DEVELOPER;
        }

        if (FUtil.LEAD_DEV.contains(player.getName()))
        {
            return Title.LEAD_DEV;
        }

        final Rank rank = getRank(player);

        // Non-admins don't have titles, display actual rank
        if (!rank.isAdmin())
        {
            return rank;
        }

        // Staff Titles
        if (ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()))
        {
            return Title.OWNER;
        }
        if (FUtil.RMCDEVS.contains(player.getName()))
        {
            return Title.RMCDEV;
        }
        if (ConfigEntry.SERVER_EXECUTIVES.getList().contains(player.getName()))
        {
            return Title.EXECUTIVE;
        }
        return rank;
    }

    public Rank getRank(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getRank((Player) sender);
        }

        // CONSOLE?
        if (sender.getName().equals("CONSOLE"))
        {
            return ConfigEntry.ADMINLIST_CONSOLE_IS_SENIOR.getBoolean() ? Rank.SENIOR_CONSOLE : Rank.TELNET_CONSOLE;
        }

        // Console admin, get by name
        Admin admin = plugin.al.getEntryByName(sender.getName());

        // Unknown console: RCON?
        if (admin == null)
        {
            return Rank.SENIOR_CONSOLE;
        }

        Rank rank = admin.getRank();

        // Get console
        if (rank.hasConsoleVariant())
        {
            rank = rank.getConsoleVariant();
        }
        return rank;
    }

    public Rank getRank(Player player)
    {
        if (plugin.al.isAdminImpostor(player))
        {
            return Rank.IMPOSTOR;
        }

        final Admin entry = plugin.al.getAdmin(player);
        if (entry != null)
        {
            return entry.getRank();
        }

        return player.isOp() ? Rank.OP : Rank.NON_OP;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        final FPlayer fPlayer = plugin.pl.getPlayer(player);
        final ShopData sd = plugin.sh.getData(player);

        // Unban admins
        boolean isAdmin = plugin.al.isAdmin(player);
        if (isAdmin)
        {
            // Verify strict IP match
            if (!plugin.al.isIdentityMatched(player))
            {
                FUtil.bcastMsg("Warning: " + player.getName() + " is an admin, but is using an account not registered to one of their ip-list.", ChatColor.RED);
                fPlayer.setSuperadminIdVerified(false);
            }
            else
            {
                fPlayer.setSuperadminIdVerified(true);
                plugin.al.updateLastLogin(player);
            }
        }

        // Handle impostors
        if (plugin.al.isAdminImpostor(player))
        {
            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + Rank.IMPOSTOR.getColoredLoginMessage());
            FUtil.bcastMsg("Warning: " + player.getName() + " has been flagged as an impostor and has been frozen!", ChatColor.RED);
            player.getInventory().clear();
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);
            plugin.pl.getPlayer(player).getFreezeData().setFrozen(true);
            player.sendMessage(ChatColor.RED + "You are marked as an impostor, please verify yourself!");
            return;
        }

        // Set display
        if (isAdmin || FUtil.TFDEVS.contains(player.getName()) || FUtil.LEAD_DEV.contains(player.getName()) || FUtil.RMCDEVS.contains(player.getName()))
        {
            final Displayable display = getDisplay(player);
            String loginMsg = display.getColoredLoginMessage();

            if (isAdmin)
            {
                Admin admin = plugin.al.getAdmin(player);
                if (admin.hasLoginMessage())
                {
                    loginMsg = FUtil.colorize(admin.getLoginMessage());
                }
                else if (sd.isCustomLoginMessage() && sd.hasLoginMessage())
                {
                    loginMsg = loginMsg + FUtil.colorize(" &band " + sd.getLoginMessage());
                }
            }

            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
            Admin target = plugin.al.getAdmin(player);

            if (!target.getAtag().isEmpty())
            {
                plugin.pl.getPlayer(player).setTag(target.getAtag());
            }
            else
            {
                plugin.pl.getPlayer(player).setTag(display.getColoredTag());
            }

            String displayName = display.getColor() + player.getName();
            try
            {
                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            }
            catch (IllegalArgumentException ex)
            {
            }
        }
        else if (!isAdmin && ConfigEntry.SERVER_MASTER_BUILDERS.getList().contains(player.getName()))
        {
            final Displayable display = getDisplay(player);
            String loginMsg = display.getColoredLoginMessage();

            if ((sd.isCustomLoginMessage() || sd.isCanSetOwnLogin()) && sd.hasLoginMessage())
            {
                loginMsg = loginMsg + FUtil.colorize(" &band " + sd.getLoginMessage());
            }

            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
            String displayName = display.getColor() + player.getName();
            plugin.pl.getPlayer(player).setTag(display.getColoredTag());
            try
            {
                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            }
            catch (IllegalArgumentException ex)
            {
            }
        }
        else if (!isAdmin && ConfigEntry.SERVER_GODS.getList().contains(player.getName()))
        {
            final Displayable display = getDisplay(player);
            String loginMsg = display.getColoredLoginMessage();

            if ((sd.isCustomLoginMessage() || sd.isCanSetOwnLogin()) && sd.hasLoginMessage())
            {
                loginMsg = loginMsg + FUtil.colorize(" &band " + sd.getLoginMessage());
            }

            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
            String displayName = display.getColor() + player.getName();
            plugin.pl.getPlayer(player).setTag(display.getColoredTag());
            try
            {
                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            }
            catch (IllegalArgumentException ex)
            {
            }
        }
        else if (!isAdmin && ConfigEntry.SERVER_BUDDHISTS.getList().contains(player.getName()))
        {
            final Displayable display = getDisplay(player);
            String loginMsg = display.getColoredLoginMessage();

            if ((sd.isCustomLoginMessage() || sd.isCanSetOwnLogin()) && sd.hasLoginMessage() && !sd.getLoginMessage().equals("none"))
            {
                loginMsg = loginMsg + FUtil.colorize(" &band " + sd.getLoginMessage());
            }

            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
            String displayName = display.getColor() + player.getName();
            plugin.pl.getPlayer(player).setTag(display.getColoredTag());
            try
            {
                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            }
            catch (IllegalArgumentException ex)
            {
            }
        }
    }
}
