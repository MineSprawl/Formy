package net.minesprawl.formy.api.senders;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;

public interface FormyFormSender {
    void sendForm(Player player, Form form);
}
