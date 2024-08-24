package net.minesprawl.formy.api;

public final class FormyInstanceHolder {
    private static FormyAPI instance;

    public static void setInstance(FormyAPI formyAPI) {
        instance = formyAPI;
    }

    public static FormyAPI getInstance() {
        return instance;
    }
}
