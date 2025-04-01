package dev.altairac.lorenaredux.enums;

public enum ChannelType {
    LORE("lore"),
    SUGGESTIONS("suggestions");

    public final String label;

    ChannelType(String label) {
        this.label = label;
    }
}
