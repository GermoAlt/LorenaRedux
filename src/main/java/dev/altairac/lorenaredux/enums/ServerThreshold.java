package dev.altairac.lorenaredux.enums;

public enum ServerThreshold {
    LORE_THRESHOLD("📜"),
    GULAG_THRESHOLD("⛓️"),
    GULAG_REMOVAL_THRESHOLD("⛓️‍💥");

    private final String emoji;

    ServerThreshold(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }
}
