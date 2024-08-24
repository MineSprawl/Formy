package net.minesprawl.formy.api.senders;

import com.google.common.base.Charsets;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMaps;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import net.minesprawl.formy.api.FormyAPI;
import net.minesprawl.formy.api.exceptions.FailedFormProcessingException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.impl.FormDefinition;
import org.geysermc.cumulus.form.impl.FormDefinitions;

import java.util.concurrent.atomic.AtomicInteger;

/*
This code is mostly a port of Floodgate's form channel handler
https://github.com/GeyserMC/Floodgate/blob/49bd56446fa10931b1c4abf6609055571a4ae105/core/src/main/java/org/geysermc/floodgate/pluginmessage/channel/FormChannel.java#L55
*/
public final class RawFormSender implements FormyFormSender {
    private final FormDefinitions definitions = FormDefinitions.instance();
    private final Short2ObjectMap<Form> storedForms = Short2ObjectMaps.synchronize(new Short2ObjectOpenHashMap<>());
    private final AtomicInteger nextFormId = new AtomicInteger(0);

    @Override
    public void sendForm(Player player, Form form) {
        player.sendPluginMessage(
                (JavaPlugin) FormyAPI.getInstance(),
                "floodgate:form",
                createFormData(form)
        );
    }

    private byte[] createFormData(Form form) {
        short formId = getNextFormId();
        storedForms.put(formId, form);
        FormDefinition<Form, ?, ?> definition = definitions.definitionFor(form);

        byte[] json = definition.codec().jsonData(form).getBytes(Charsets.UTF_8);
        byte[] data = new byte[json.length + 3];
        data[0] = (byte) definition.formType().ordinal();
        data[1] = (byte) (formId >> 8 & 0xFF);
        data[2] = (byte) (formId & 0xFF);
        System.arraycopy(json, 0, data, 3, json.length);

        return data;
    }

    public void callResponseConsumer(byte[] data) {
        Form storedForm = storedForms.remove(getFormId(data));
        if (storedForm != null) {
            String responseData = new String(data, 2, data.length - 2, Charsets.UTF_8);
            try {
                definitions.definitionFor(storedForm).handleFormResponse(storedForm, responseData);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FailedFormProcessingException("An error occurred while processing a form response");
            }
        }
    }

    private short getFormId(byte[] data) {
        return (short) ((data[0] & 0xFF) << 8 | data[1] & 0xFF);
    }

    private short getNextFormId() {
        return (short) nextFormId.getAndUpdate((number) -> number == Short.MAX_VALUE ? 0 : number + 1);
    }
}
