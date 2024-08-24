package net.minesprawl.formy.api;

import net.minesprawl.formy.api.senders.FormyFormSender;
import org.geysermc.cumulus.form.Form;

/**
 * The main interface for the Formy API containing the most useful methods.
 *
 * @since 0.1.0
 * @author CDX
 */
public interface FormyAPI  {
    static FormyAPI getInstance() {
        return FormyInstanceHolder.getInstance();
    }

    /**
     * Returns a {@link FormyFormSender} to send {@link Form}s
     *
     * @since 0.1.0
     * @author CDX
     */
    FormyFormSender getFormSender();

    /**
     * Used to check if the Floodgate plugin is installed on the server.
     *
     * @return if the Floodgate plugin was found
     *
     * @since 0.1.0
     * @author CDX
     */
    boolean isFloodgateInstalled();

    /**
     * Used to check if Skript is installed on the server.
     *
     * @return if the Skript plugin was found
     *
     * @since 0.1.0
     * @author CDX
     */
    boolean isSkriptInstalled();

    /**
     * Used to check if the config setting 'send-raw-forms' is enabled
     *
     * @return the value of the 'send-raw-forms' config option
     */
    boolean usingRawForms();
}
