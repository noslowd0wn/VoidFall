package noslowdwn.voidfall.actions;

import noslowdwn.voidfall.VoidFall;
import org.bukkit.entity.Player;

public class ShowActionbar extends AbstractAction {

    public ShowActionbar(final VoidFall plugin, final String string) {
        super(plugin, string);
    }

    @Override
    public void process(final Player player, final String string) {
        player.sendActionBar(string);
    }
}