package com.github.groundbreakingmc.voidfall.utils;

import com.github.groundbreakingmc.voidfall.VoidFall;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public final class PapiUtil {

    private static boolean isPapiEnabled;

    private PapiUtil() {

    }

    public static String parse(final Player player, final String message) {
        return isPapiEnabled ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    public static void setPapiStatus(final VoidFall plugin) {
        isPapiEnabled = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}
