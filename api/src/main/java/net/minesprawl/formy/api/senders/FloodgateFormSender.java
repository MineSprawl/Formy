package net.minesprawl.formy.api.senders;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;

public final class FloodgateFormSender implements FormyFormSender {
    private final FloodgateApi floodgateApi = FloodgateApi.getInstance();

    @Override
    public void sendForm(Player player, Form form) {
        floodgateApi.sendForm(player.getUniqueId(), form);
    }
}
