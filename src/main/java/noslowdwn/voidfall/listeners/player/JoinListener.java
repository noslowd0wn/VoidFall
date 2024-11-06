package noslowdwn.voidfall.listeners.player;

import noslowdwn.voidfall.VoidFall;
import noslowdwn.voidfall.actions.AbstractAction;
import noslowdwn.voidfall.utils.config.ConfigValues;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public final class JoinListener implements Listener {

    private final VoidFall plugin;
    private final ConfigValues configValues;

    private final String[] placeholders = {"%player%"};

    private boolean isRegistered;

    public JoinListener(final VoidFall plugin) {
        this.plugin = plugin;
        this.configValues = plugin.getConfigValues();
    }

    @EventHandler
    public void onQuit(final PlayerJoinEvent event) {
        this.processEvent(event);
    }

    public void registerEvent() {
        if (!this.isRegistered) {
            this.plugin.getServer().getPluginManager().registerEvent(
                    PlayerJoinEvent.class,
                    this,
                    EventPriority.MONITOR,
                    (listener, event) -> this.onQuit((PlayerJoinEvent) event),
                    this.plugin
            );

            this.isRegistered = true;
        }
    }

    public void unregisterEvent() {
        if (!this.isRegistered) {
            HandlerList.unregisterAll(this);
            this.isRegistered = false;
        }
    }

    private void processEvent(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String[] replacement = {player.getName()};
        final List<AbstractAction> actions = this.configValues.getPlayerServerJoinActions();
        for (int i = 0; i < actions.size(); i++) {
            final AbstractAction action = actions.get(i);
            action.run(player, placeholders, replacement);
        }
    }
}