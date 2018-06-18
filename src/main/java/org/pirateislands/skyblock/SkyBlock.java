package org.pirateislands.skyblock;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.IslandAPI;
import com.islesmc.islandapi.dao.RedisIslandDAO;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.module.PluginModule;
import com.keenant.tabbed.Tabbed;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.command.*;
import org.pirateislands.skyblock.command.island.*;
import org.pirateislands.skyblock.command.mission.LevelCommand;
import org.pirateislands.skyblock.configuration.OreGenerationConfig;
import org.pirateislands.skyblock.configuration.ServerConfig;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseCommandHandler;
import org.pirateislands.skyblock.goose.GooseHandler;
import org.pirateislands.skyblock.goose.GooseTicker;
import org.pirateislands.skyblock.handler.CombatLogHandler;
import org.pirateislands.skyblock.island.IslandGUIHandler;
import org.pirateislands.skyblock.island.IslandOreGens;
import org.pirateislands.skyblock.island.IslandRegistry;
import org.pirateislands.skyblock.island.listeners.GeneralListener;
import org.pirateislands.skyblock.island.listeners.IslandListener;
import org.pirateislands.skyblock.island.listeners.isles.CoordinateBookListener;
import org.pirateislands.skyblock.island.listeners.isles.IslesRaidListener;
import org.pirateislands.skyblock.player.listener.*;
import org.pirateislands.skyblock.region.RegionHandler;
import org.pirateislands.skyblock.schematic.SchematicLoader;
import org.pirateislands.skyblock.task.BackupTask;
import org.pirateislands.skyblock.task.FlyCheckTask;
import org.pirateislands.skyblock.task.PlayerWorldSwitchFixer;
import org.pirateislands.skyblock.timers.TimerHandler;
import org.pirateislands.skyblock.util.GridUtil;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matt on 2017-02-10.
 */
public class SkyBlock extends PluginModule {

    private static SkyBlock plugin;
    public SchematicLoader schematicLoader;
    private RegionHandler regionHandler;
    private IslandRegistry islandRegistry;
    private WorldEditPlugin worldEditPlugin;
    private OreGenerationConfig oreGenerationConfig;
    private ServerConfig serverConfig;
    private GooseHandler gooseHandler;
    private Economy economy;
    private Tabbed tabbed;
    private World islandWorld;
    private GridUtil gridUtil;
    private CombatLogHandler combatLogHandler;
    private TimerHandler timerHandler;

    public static synchronized SkyBlock getPlugin() {
        return plugin;
    }

    private static synchronized void setPlugin(final SkyBlock skyBlock) {
        plugin = skyBlock;
    }

    @Override
    public void onEnable() {
        IslandAPI.setRedisIslandDAO(new RedisIslandDAO("172.17.0.1"));
        setPlugin(this);

        getModuleDir().toFile().mkdir();
        this.regionHandler = new RegionHandler();
        this.oreGenerationConfig = new OreGenerationConfig();
        this.oreGenerationConfig.loadValues();

        serverConfig = new ServerConfig();
        serverConfig.load();

        this.gooseHandler = new GooseHandler();
        this.tabbed = new Tabbed(API.getPlugin());
        this.gridUtil = new GridUtil(100000);
        this.combatLogHandler = new CombatLogHandler();
        this.timerHandler = new TimerHandler();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getServerConfig().getServerType() == ServerType.SKY) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv create Skyblock normal -g VoidWorld");
                }
                islandWorld = Bukkit.getWorld("Skyblock");

                setupEconomy();
                islandRegistry = new IslandRegistry();

                if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null && !Bukkit.getPluginManager().getPlugin("WorldEdit").isEnabled()) {
                    disable();
                }

                worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            }
        }.runTaskLater(API.getPlugin(), 20L);

        schematicLoader = new SchematicLoader();
        setupShit();

        new GooseTicker().runTaskTimerAsynchronously(API.getPlugin(), 1L, 1L);
        new BackupTask().runTaskTimer(API.getPlugin(), TimeUnit.MINUTES.toSeconds(25L) * 20L, TimeUnit.MINUTES.toSeconds(25L) * 20L);
//        new PlayerWorldSwitchFixer().runTaskTimer(API.getPlugin(), 10L, 10L);
//        new TabUpdateTask().runTaskTimerAsynchronously(API.getPlugin(), 1L, 20L);
    }

    private boolean setupEconomy() {
        if (API.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            System.out.println("no vault");
            return false;
        }
        RegisteredServiceProvider<Economy> economyProvider = API.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider == null) {
            System.out.println("rsp is null");
            return false;
        }
        economy = economyProvider.getProvider();
        return economy != null;
    }

    public RegionHandler getRegionHandler() {
        return regionHandler;
    }

    public World getIslandWorld() {
        return islandWorld;
    }

    @Override
    public void onDisable() {
        islandRegistry.disable();
        getServerConfig().save();
    }

    public OreGenerationConfig getOreGenerationConfig() {
        return oreGenerationConfig;
    }

    public IslandRegistry getIslandRegistry() {
        return islandRegistry;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public SchematicLoader getSchematicLoader() {
        return schematicLoader;
    }

    private void setupShit() {
        registerEvent(new IslandListener());
        registerEvent(new IslandGUIHandler());
        registerEvent(new GeneralListener());
        registerEvent(new IslandOreGens());
        registerEvent(this.gooseHandler);
        registerEvent(new PlayerJoinListener());
        registerEvent(new PlayerDamageListener());
        registerEvent(new PlayerDeathListener());
        registerEvent(new PlayerInteractListener());

        if (getServerConfig().getServerType().equals(ServerType.ISLES)) {
            registerEvent(new IslesRaidListener());
            registerEvent(new CoordinateBookListener());
            registerEvent(new PlayerBoatListener());
            registerCommand("givecoordinatebook", new GiveCoordinateBookCommand());
        }

        new FlyCheckTask().runTaskTimerAsynchronously(API.getPlugin(), 1L, 5L);


        GooseCommandHandler commandHandler = new GooseCommandHandler("island", new IslandBaseCommand());
        commandHandler.setAliases(Lists.newArrayList("is", "islands"));
        commandHandler.addSubCommand("accept", new IslandAcceptCommand());
        commandHandler.addSubCommand("help", new IslandBaseCommand());
        commandHandler.addSubCommand("create", new IslandCreateCommand());
        commandHandler.addSubCommand("decline", new IslandDeclineCommand());
        commandHandler.addSubCommand("delete", new IslandDeleteCommand());
        commandHandler.addSubCommand("home", new IslandHomeCommand());
        commandHandler.addSubCommand("kick", new IslandKickCommand());
        commandHandler.addSubCommand("leave", new IslandLeaveCommand());
        commandHandler.addSubCommand("level", new IslandLevelCommand());
        commandHandler.addSubCommand("invite", new IslandInviteCommand());
        commandHandler.addSubCommand("top", new IslandTopCommand());
        commandHandler.addSubCommand("lock", new IslandLockCommand());
        commandHandler.addSubCommand("warp", new IslandWarpCommand());
        commandHandler.addSubCommand("sethome", new IslandSetHomeCommand());
        commandHandler.addSubCommand("force", new IslandForceJoinCommand());
        commandHandler.addSubCommand("expel", new IslandExpelCommand());
        commandHandler.addSubCommand("coop", new IslandCoopCommand());
        commandHandler.addSubCommand("members", new IslandMembersCommand());
        commandHandler.addSubCommand("chat", new IslandChatCommand());
        commandHandler.addSubCommand("reload", new IslandReloadCommand());

        registerCommand("island", commandHandler);

        registerCommand("region", new RegionCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("level", new LevelCommand());
        registerCommand("balance", new BalanceCommand());
        registerCommand("pay", new PayCommand());
        registerCommand("help", new HelpCommand());
        registerCommand("setislandsize", new SetIslandSizeCommand());
        registerCommand("setmaxmembers", new SetIslandMaxMembersCommand());
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public String format(double number) {
        String[] suffix = new String[]{"", "K", "M", "B", "T", "Q"};
        int MAX_LENGTH = 4;
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public GooseHandler getGooseHandler() {
        return gooseHandler;
    }

    public Tabbed getTabbed() {
        return tabbed;
    }

    public GridUtil getGridUtil() {
        return gridUtil;
    }

    public CombatLogHandler getCombatLogHandler() {
        return combatLogHandler;
    }

    public TimerHandler getTimerHandler() {
        return timerHandler;
    }
}
