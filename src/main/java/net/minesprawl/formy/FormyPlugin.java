package net.minesprawl.formy;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import net.minesprawl.formy.api.*;
import net.minesprawl.formy.api.exceptions.InvalidFormyConfigurationException;
import net.minesprawl.formy.api.senders.FloodgateFormSender;
import net.minesprawl.formy.api.senders.FormyFormSender;
import net.minesprawl.formy.api.senders.RawFormSender;
import net.minesprawl.formy.listener.RawFormResponseListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class FormyPlugin extends JavaPlugin implements FormyAPI {
    private FormyFormSender formSender;
    private FileConfiguration config;
    private SkriptAddon addon;

    @Override
    public void onEnable() {
        FormyInstanceHolder.setInstance(this);

        saveDefaultConfig();
        config = getConfig();

        // Setup Form Sender
        if (usingRawForms()) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "floodgate:form");
            getServer().getMessenger().registerIncomingPluginChannel(this, "floodgate:form", new RawFormResponseListener());
            formSender = new RawFormSender();
        } else if (isFloodgateInstalled()) {
            formSender = new FloodgateFormSender();
        } else {
            throw new InvalidFormyConfigurationException("The server is not using raw forms while Floodgate is also not installed");
        }

        // Enable Skript API if installed
        if (isSkriptInstalled()) {
            addon = Skript.registerAddon(this);
            try {
                addon.loadClasses("net.minesprawl.formy.elements.effects");
                addon.loadClasses("net.minesprawl.formy.elements.expressions");
                addon.loadClasses("net.minesprawl.formy.elements.types");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDisable() {
        if (usingRawForms()) {
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, "floodgate:form");
            getServer().getMessenger().unregisterIncomingPluginChannel(this, "floodgate:form");
        }
    }

    public FileConfiguration config() {
        return config;
    }

    @Override
    public FormyFormSender getFormSender() {
        return formSender;
    }

    @Override
    public boolean isFloodgateInstalled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("Floodgate");
    }

    @Override
    public boolean isSkriptInstalled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("Skript");
    }

    @Override
    public boolean usingRawForms() {
        return config().getBoolean("send-raw-forms");
    }
}
