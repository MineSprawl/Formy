package net.minesprawl.formy.listener;

import net.minesprawl.formy.api.FormyAPI;
import net.minesprawl.formy.api.senders.RawFormSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class RawFormResponseListener implements PluginMessageListener {
    private final FormyAPI formyAPI = FormyAPI.getInstance();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if (!channel.equals("floodgate:form")) return;
        if (!formyAPI.usingRawForms()) return;
        ((RawFormSender) formyAPI.getFormSender()).callResponseConsumer(data);
    }
}
