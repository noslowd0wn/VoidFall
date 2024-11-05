package noslowdwn.voidfall;

import lombok.Getter;
import noslowdwn.voidfall.listeners.*;
import noslowdwn.voidfall.utils.UpdateChecker;
import noslowdwn.voidfall.utils.colorizer.IColorizer;
import noslowdwn.voidfall.utils.colorizer.LegacyColorizer;
import noslowdwn.voidfall.utils.colorizer.VanillaColorizer;
import noslowdwn.voidfall.utils.config.ConfigValues;
import noslowdwn.voidfall.utils.logging.BukkitLogger;
import noslowdwn.voidfall.utils.logging.ILogger;
import noslowdwn.voidfall.utils.logging.PaperLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class VoidFall extends JavaPlugin {

    private ConfigValues configValues;

    private IColorizer colorizer;

    private ILogger myLogger;

    private JoinListener joinListener;
    private QuitListener quitListener;
    private DeathListener deathListener;

    @Override
    public void onEnable() {
        this.configValues = new ConfigValues(this);

        this.registerListenerClasses();
        this.configValues.setupValues();

        final int subVersion = this.getSubVersion();
        this.colorizer = this.getColorizerByVersion(subVersion);
        this.myLogger = this.getLoggerByVersion(subVersion);

        this.registerCommand();

        this.getServer().getPluginManager().registerEvents(new YCords(this), this);



        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> new UpdateChecker(this).checkVersion(), 60L);
    }

    private void registerCommand() {
        final VoidFallCommand command = new VoidFallCommand(this);
        super.getCommand("voidfall").setExecutor(command);
        super.getCommand("voidfall").setTabCompleter(command);
    }

    private void registerListenerClasses() {
        joinListener = new JoinListener(this);
        quitListener = new QuitListener(this);
        deathListener = new DeathListener(this);
    }

    public void registerRegionsListener() {
        final boolean isWgEventsEnabled = Bukkit.getPluginManager().isPluginEnabled("WorldGuardEvents");
        if (isWgEventsEnabled) {
            this.getServer().getPluginManager().registerEvents(new Region(this), this);
        } else {
            this.myLogger.info("Actions on region enter/leave will be disabled!");
            this.myLogger.info("Please download WorldGuardEvents to enable them.");
            this.myLogger.info("https://www.spigotmc.org/resources/worldguard-events.65176/");
        }
    }

    public IColorizer getColorizerByVersion(final int subVersion) {
        final boolean is16OrAbove = subVersion >= 16;
        return is16OrAbove ? new LegacyColorizer() : new VanillaColorizer();
    }

    public ILogger getLoggerByVersion(final int subVersion) {
        final boolean is19OrAbove = subVersion >= 19;
        return is19OrAbove ? new PaperLogger(this) : new BukkitLogger(this);
    }

    public int getSubVersion() {
        try {
            return Integer.parseInt(super.getServer().getVersion().split("\\.")[1]);
        } catch (final NumberFormatException ex) {
            super.getLogger().warning("Failed to extract server version. Plugin may not work correctly!");
            return 0;
        }
    }
}