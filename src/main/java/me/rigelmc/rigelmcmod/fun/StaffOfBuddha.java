/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.rigelmc.rigelmcmod.fun;

import me.rigelmc.rigelmcmod.FreedomService;
import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.shop.ShopData;
import me.rigelmc.rigelmcmod.util.FUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.FireworkEffect.Type;

public class StaffOfBuddha extends FreedomService
{

    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
    public List<Integer> bullets = new ArrayList<>();
    public final long cooldownTime = 20;

    public StaffOfBuddha(RigelMCMod plugin)
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onBulletImpact(ProjectileHitEvent event)
    {
        if (event.getEntity() instanceof Arrow && bullets.contains(event.getEntity().getEntityId()))
        {
            Arrow bullet = (Arrow) event.getEntity();
            bullets.remove((Integer) bullet.getEntityId());

            if (event.getHitEntity() != null && event.getHitEntity() instanceof LivingEntity)
            {
                if (bullet.getShooter() != null && bullet.getShooter() instanceof Player)
                {
                    Player shooter = (Player) bullet.getShooter();
                    LivingEntity hitEntity = (LivingEntity) event.getHitEntity();
                    if (event.getHitEntity() instanceof Player)
                    {
                        Player target = (Player) event.getHitEntity();
                        if (plugin.al.isAdmin(target) && !FUtil.isExecutive(shooter.getName()))
                        {
                            FUtil.playerMsg(shooter, "Sorry, but you can't attack staff members with Staff of Buddha!", ChatColor.RED);
                            return;
                        }
                        if (target.getGameMode().equals(GameMode.CREATIVE) && !FUtil.isExecutive(shooter.getName()))
                        {
                            return;
                        }
                    }

                    hitEntity.setHealth(0);
                    if (ConfigEntry.ALLOW_FIREWORK_EXPLOSIONS.getBoolean())
                    {
                        Location l = hitEntity.getLocation();
                        final Firework fw = (Firework) l.getWorld().spawn(l, Firework.class);
                        FireworkMeta fm = fw.getFireworkMeta();
                        fm.addEffect(FireworkEffect.builder().trail(true).with(Type.BALL_LARGE).withColor(Color.RED).build());
                        fm.setPower(0);
                        fw.setFireworkMeta(fm);
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                fw.detonate();
                            }
                        }.runTaskLater(plugin, 2L);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRightClick(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            Player p = event.getPlayer();
            if (p.getInventory().getItemInMainHand().equals(getStaffOfBuddha()))
            {
                if (cooldowns.containsKey(p.getName()))
                {
                    long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0)
                    {
                        FUtil.playerMsg(p, "You can't use the Stick of Buddha for another " + secondsLeft + " seconds!", ChatColor.RED);
                        return;
                    }
                }
                Arrow bullet = p.launchProjectile(Arrow.class, p.getLocation().getDirection());
                bullets.add(bullet.getEntityId());
                bullet.setVelocity(bullet.getVelocity().normalize().multiply(50));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 50, 2.5f);

                // Executives don't need a cool down :^)
                if (!FUtil.isExecutive(p.getName()))
                {
                    cooldowns.put(p.getName(), System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAttack(EntityDamageByEntityEvent event)
    {
        Entity attacker = event.getDamager();
        Entity target = event.getEntity();
        if (attacker instanceof Player && target instanceof LivingEntity)
        {
            Player p = (Player) attacker;
            ItemStack i = p.getInventory().getItemInMainHand();
            if (i != null && i.equals(getStaffOfBuddha()))
            {
                ShopData sd = plugin.sh.getData(p);
                if (sd.isMagicWand())
                {
                    if (cooldowns.containsKey(p.getName()))
                    {
                        long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                        if (secondsLeft > 0)
                        {
                            FUtil.playerMsg(p, "You can't use the Staff Of Buddha for another " + secondsLeft + " seconds!", ChatColor.RED);
                            return;
                        }
                    }
                    if (target instanceof Player && !plugin.al.isAdmin(p) && plugin.al.isAdmin((Player) target))
                    {
                        FUtil.playerMsg(p, "Sorry, but you can't attack staff members with the Staff Of Buddha!", ChatColor.RED);
                        return;
                    }
                    // Executives don't need a cool down :^)
                    if (!FUtil.isExecutive(p.getName()))
                    {
                        cooldowns.put(p.getName(), System.currentTimeMillis());
                    }

                    // Play attack sound
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 100F, 0.1F);

                    // Deliver the final blow
                    LivingEntity t = (LivingEntity) target;
                    t.setHealth(0);
                }
            }
        }
    }

    public ItemStack getStaffOfBuddha()
    {
        ItemStack MAGIC_WAND = new ItemStack(Material.STICK);
        ItemMeta datMeta = MAGIC_WAND.getItemMeta();
        datMeta.setDisplayName(ChatColor.GOLD + "Staff Of Buddha");
        List<String> lore = new ArrayList();
        lore.add(ChatColor.RED + "Buddha Bless Me Buddha Bless you!");
        lore.add(ChatColor.RED + "Totally didn't take this refference from the buddhism hotline ;)");
        datMeta.setLore(lore);
        datMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, 420, true);
        datMeta.addEnchant(Enchantment.DAMAGE_ALL, 420, true);
        datMeta.setUnbreakable(true);
        MAGIC_WAND.setItemMeta(datMeta);
        return MAGIC_WAND;
    }
}
