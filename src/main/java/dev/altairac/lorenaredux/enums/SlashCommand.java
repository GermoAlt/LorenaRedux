package dev.altairac.lorenaredux.enums;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public enum SlashCommand {
    PING("ping", "ping the bot"),

    SET_LORE_THRESHOLD(
            "set-lore-threshold",
            "set the threshold for lore",
            builder -> builder.addOption(OptionType.INTEGER, "threshold", "the amount of reactions needed to register new lore", true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    ),

    SET_GULAG_THRESHOLD(
            "set-gulag-threshold",
            "set the threshold for gulag",
            builder -> builder.addOption(OptionType.INTEGER, "threshold", "the amount of reactions needed to gulag a user", true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    ),

    SET_GULAG_REMOVAL_THRESHOLD(
            "set-gulag-removal-threshold",
            "set the threshold for removing a user from gulag",
            builder -> builder.addOption(OptionType.INTEGER, "threshold", "the amount of reactions needed to remove a user from gulag", true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    ),

    SET_LORE_CHANNEL_ID(
            "set-lore-channel-id",
            "set the channel id for lore",
            builder -> builder.addOption(OptionType.CHANNEL, "channel", "the channel to send lore to", true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    ),

    SET_SUGGESTION_CHANNEL_ID(
            "set-suggestion-channel-id",
            "set the channel id for suggestions",
            builder -> builder.addOption(OptionType.CHANNEL, "channel", "the channel to send suggestions to", true)
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    ),

    CONVERT_UNITS(
            "convert",
            "convert units",
            builder -> builder.addOption(OptionType.INTEGER, "value", "the value to convert", true)
                    .addOption(OptionType.STRING, "unit", "the unit to convert", true)
                    .addOption(OptionType.STRING, "to", "the unit to convert to", true)
    ),

    SUGGEST(
            "suggest",
            "Suggest a change to the server",
            builder -> builder.addOption(OptionType.STRING, "suggestion", "the suggestion to suggest", true)
    );

    private final String name;
    private final String description;
    private final SlashCommandData commandData;

    /**
     * Constructor for commands without additional options or permissions.
     */
    SlashCommand(String name, String description) {
        this.name = name;
        this.description = description;
        this.commandData = Commands.slash(name, description);
    }

    /**
     * Constructor for commands with additional options or permissions.
     */
    SlashCommand(String name, String description, BuilderConfigurator configurator) {
        this.name = name;
        this.description = description;
        SlashCommandData builder = Commands.slash(name, description); // Explicitly use SlashCommandData
        configurator.configure(builder);
        this.commandData = builder;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SlashCommandData getCommandData() {
        return commandData;
    }

    /**
     * Functional interface for configuring SlashCommandData.
     */
    @FunctionalInterface
    private interface BuilderConfigurator {
        void configure(SlashCommandData builder);
    }
}