package me.rigelmc.rigelmcmod;

import me.rigelmc.rigelmcmod.fun.Trailer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import me.rigelmc.rigelmcmod.admin.AdminList;
import me.rigelmc.rigelmcmod.banning.BanManager;
import me.rigelmc.rigelmcmod.banning.PermbanList;
import me.rigelmc.rigelmcmod.blocking.BlockBlocker;
import me.rigelmc.rigelmcmod.blocking.EventBlocker;
import me.rigelmc.rigelmcmod.blocking.InteractBlocker;
import me.rigelmc.rigelmcmod.blocking.MobBlocker;
import me.rigelmc.rigelmcmod.blocking.PotionBlocker;
import me.rigelmc.rigelmcmod.blocking.command.CommandBlocker;
import me.rigelmc.rigelmcmod.bridge.BukkitTelnetBridge;
import me.rigelmc.rigelmcmod.bridge.EssentialsBridge;
import me.rigelmc.rigelmcmod.bridge.LibsDisguisesBridge;
import me.rigelmc.rigelmcmod.bridge.WorldEditBridge;
import me.rigelmc.rigelmcmod.discord.Discord;
import me.rigelmc.rigelmcmod.caging.Cager;
import me.rigelmc.rigelmcmod.command.CommandLoader;
import me.rigelmc.rigelmcmod.config.MainConfig;
import me.rigelmc.rigelmcmod.freeze.Freezer;
import me.rigelmc.rigelmcmod.fun.ItemFun;
import me.rigelmc.rigelmcmod.fun.Jumppads;
import me.rigelmc.rigelmcmod.fun.Landminer;
import me.rigelmc.rigelmcmod.fun.MP44;
import me.rigelmc.rigelmcmod.fun.MagicWand;
import me.rigelmc.rigelmcmod.fun.ThorHammer;
import me.rigelmc.rigelmcmod.httpd.HTTPDaemon;
import me.rigelmc.rigelmcmod.player.PlayerList;
import me.rigelmc.rigelmcmod.shop.Shop;
import me.rigelmc.rigelmcmod.shop.ShopGUIListener;
import me.rigelmc.rigelmcmod.rank.RankManager;
import me.rigelmc.rigelmcmod.rollback.RollbackManager;
import me.rigelmc.rigelmcmod.util.FLog;
import me.rigelmc.rigelmcmod.util.FUtil;
import me.rigelmc.rigelmcmod.util.MethodTimer;
import me.rigelmc.rigelmcmod.world.WorldManager;
import net.pravian.aero.component.service.ServiceManager;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

public class RigelMCMod extends AeroPlugin<RigelMCMod>
{

    public static final String CONFIG_FILENAME = "config.yml";
    //
    public static final BuildProperties build = new BuildProperties();
    //
    public static String pluginName;
    public static String pluginVersion;
    //
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public MainConfig config;
    //
    // Services
    public ServiceManager<RigelMCMod> services;
    public ServerInterface si;
    public SavedFlags sf;
    public WorldManager wm;
    public LogViewer lv;
    public AdminList al;
    public RankManager rm;
    public CommandLoader cl;
    public CommandBlocker cb;
    public EventBlocker eb;
    public BlockBlocker bb;
    public MobBlocker mb;
    public InteractBlocker ib;
    public PotionBlocker pb;
    public LoginProcess lp;
    public AntiNuke nu;
    public AntiSpam as;
    public PlayerList pl;
    public Shop sh;
    public ShopGUIListener sl;
    public Announcer an;
    public ChatManager cm;
    public BanManager bm;
    public PermbanList pm;
    public ProtectArea pa;
    public GameRuleHandler gr;
    public RollbackManager rb;
    public CommandSpy cs;
    public Cager ca;
    public Freezer fm;
    public Orbiter or;
    public Discord dc;
    public Muter mu;
    public Fuckoff fo;
    public AutoKick ak;
    public AutoEject ae;
    public MovementValidator mv;
    public EntityWiper ew;
    public ServerPing sp;
    public ItemFun it;
    public Landminer lm;
    public MP44 mp;
    public Jumppads jp;
    public Trailer tr;
    public HTTPDaemon hd;
    public EditBlocker ebl;
    public PvPBlocker pbl;
    public MagicWand mw;
    public ThorHammer th;
    //
    // Bridges
    public ServiceManager<RigelMCMod> bridges;
    public BukkitTelnetBridge btb;
    public EssentialsBridge esb;
    public LibsDisguisesBridge ldb;
    public WorldEditBridge web;
    public CoreProtectDatabase cpd;

    @Override
    public void load()
    {
        RigelMCMod.pluginName = plugin.getDescription().getName();
        RigelMCMod.pluginVersion = plugin.getDescription().getVersion();

        FLog.setPluginLogger(plugin.getLogger());
        FLog.setServerLogger(server.getLogger());

        build.load(plugin);
    }

    @Override
    public void enable()
    {
        FLog.info("Created by Madgeek1450, Prozza, Savnith and LightWarp");
        FLog.info("Version " + build.formattedVersion());
        FLog.info("Compiled " + build.date + " by " + build.author);

        final MethodTimer timer = new MethodTimer();
        timer.start();

        // Warn if we're running on a wrong version
        ServerInterface.warnVersion();

        // Delete unused files
        FUtil.deleteCoreDumps();
        FUtil.deleteFolder(new File("./_deleteme"));

        // Convert old config files
        new ConfigConverter(plugin).convert();

        BackupManager backups = new BackupManager(this);
        backups.createBackups(RigelMCMod.CONFIG_FILENAME, true);
        backups.createBackups(AdminList.CONFIG_FILENAME);
        backups.createBackups(PermbanList.CONFIG_FILENAME);

        config = new MainConfig(this);
        config.load();

        // Start services
        services = new ServiceManager<>(plugin);
        si = services.registerService(ServerInterface.class);
        sf = services.registerService(SavedFlags.class);
        wm = services.registerService(WorldManager.class);
        lv = services.registerService(LogViewer.class);
        al = services.registerService(AdminList.class);
        rm = services.registerService(RankManager.class);
        cl = services.registerService(CommandLoader.class);
        cb = services.registerService(CommandBlocker.class);
        eb = services.registerService(EventBlocker.class);
        bb = services.registerService(BlockBlocker.class);
        mb = services.registerService(MobBlocker.class);
        ib = services.registerService(InteractBlocker.class);
        pb = services.registerService(PotionBlocker.class);
        lp = services.registerService(LoginProcess.class);
        nu = services.registerService(AntiNuke.class);
        as = services.registerService(AntiSpam.class);
        dc = services.registerService(Discord.class);

        pl = services.registerService(PlayerList.class);
        sh = services.registerService(Shop.class);
        sl = services.registerService(ShopGUIListener.class);
        an = services.registerService(Announcer.class);
        cm = services.registerService(ChatManager.class);
        bm = services.registerService(BanManager.class);
        pm = services.registerService(PermbanList.class);
        pa = services.registerService(ProtectArea.class);
        gr = services.registerService(GameRuleHandler.class);

        // Single admin utils
        rb = services.registerService(RollbackManager.class);
        cs = services.registerService(CommandSpy.class);
        ca = services.registerService(Cager.class);
        fm = services.registerService(Freezer.class);
        or = services.registerService(Orbiter.class);
        mu = services.registerService(Muter.class);
        fo = services.registerService(Fuckoff.class);
        ak = services.registerService(AutoKick.class);
        ae = services.registerService(AutoEject.class);

        mv = services.registerService(MovementValidator.class);
        ew = services.registerService(EntityWiper.class);
        sp = services.registerService(ServerPing.class);

        // Fun
        it = services.registerService(ItemFun.class);
        lm = services.registerService(Landminer.class);
        mp = services.registerService(MP44.class);
        jp = services.registerService(Jumppads.class);
        tr = services.registerService(Trailer.class);
        mw = services.registerService(MagicWand.class);
        th = services.registerService(ThorHammer.class);
        
        ebl = services.registerService(EditBlocker.class);
        pbl = services.registerService(PvPBlocker.class);

        // HTTPD
        hd = services.registerService(HTTPDaemon.class);
        services.start();

        // Start bridges
        bridges = new ServiceManager<>(plugin);
        btb = bridges.registerService(BukkitTelnetBridge.class);
        esb = bridges.registerService(EssentialsBridge.class);
        ldb = bridges.registerService(LibsDisguisesBridge.class);
        web = bridges.registerService(WorldEditBridge.class);
        cpd = bridges.registerService(CoreProtectDatabase.class);
        bridges.start();

        timer.update();
        FLog.info("Version " + pluginVersion + " for " + ServerInterface.COMPILE_NMS_VERSION + " enabled in " + timer.getTotal() + "ms");

        // Metrics @ http://mcstats.org/plugin/TotalFreedomMod
        try
        {
            final Metrics metrics = new Metrics(plugin);
            metrics.start();
        }
        catch (IOException ex)
        {
            FLog.warning("Failed to submit metrics data: " + ex.getMessage());
        }

        // Add spawnpoints later - https://github.com/TotalFreedom/TotalFreedomMod/issues/438
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                plugin.pa.autoAddSpawnpoints();
            }
        }.runTaskLater(plugin, 60L);
    }

    @Override
    public void disable()
    {
        // Stop services and bridges
        bridges.stop();
        services.stop();

        server.getScheduler().cancelTasks(plugin);

        FLog.info("Plugin disabled");
    }

    public static class BuildProperties
    {

        public String author;
        public String codename;
        public String version;
        public String number;
        public String date;
        public String head;

        public void load(RigelMCMod plugin)
        {
            try
            {
                final Properties props;
                try (InputStream in = plugin.getResource("build.properties"))
                {
                    props = new Properties();
                    props.load(in);
                }

                author = props.getProperty("program.build.author", "unknown");
                codename = props.getProperty("program.build.codename", "unknown");
                version = props.getProperty("program.build.version", "unknown");
                number = props.getProperty("program.build.number", "1");
                date = props.getProperty("program.build.date", "unknown");
                head = props.getProperty("program.build.head", "unknown");
            }
            catch (Exception ex)
            {
                FLog.severe("Could not load build properties! Did you compile with Netbeans/Maven?");
                FLog.severe(ex);
            }
        }

        public String formattedVersion()
        {
            return pluginVersion + "." + number + " (" + head + ")";
        }
    }

    public static RigelMCMod plugin()
    {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
        {
            if (plugin.getName().equalsIgnoreCase(pluginName))
            {
                return (RigelMCMod) plugin;
            }
        }
        return null;
    }

}
