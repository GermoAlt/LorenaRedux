package dev.altairac.lorenaredux.service;

import dev.altairac.lorenaredux.model.Lore;
import dev.altairac.lorenaredux.model.Server;
import dev.altairac.lorenaredux.repository.LoreRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static dev.altairac.lorenaredux.enums.ChannelType.LORE;

@Service
public class LoreService {
    private final LoreRepository loreRepository;
    private final UserService userService;

    @Autowired
    public LoreService(LoreRepository loreRepository, UserService userService) {
        this.loreRepository = loreRepository;
        this.userService = userService;
    }
    public void addLore(MessageReactionAddEvent event, Server server) {
        Optional<Lore> oLore = loreRepository.findById(event.getMessageIdLong());
        Message message = event.retrieveMessage().complete();
        if(oLore.isPresent()) return;

        loreRepository.save(new Lore(
                message.getIdLong(),
                message.getContentRaw(),
                userService.findUserById(message.getAuthor().getIdLong()).get(),
                message.getAttachments().stream().map(Message.Attachment::getUrl).toList(),
                message.getJumpUrl()
        ));
        MessageEmbed loreEmbed = new EmbedBuilder()
                .setAuthor(
                        message.getAuthor().getName(),
                        null,
                        message.getAuthor().getEffectiveAvatarUrl()
                )
                .setDescription(message.getContentRaw())
                .setImage(message.getAttachments().stream().findFirst().map(Message.Attachment::getUrl).orElse(null))
                .build();
        Objects.requireNonNull(message.getGuild().getTextChannelById(server.getChannelIds().get(LORE))).sendMessageEmbeds(loreEmbed).queue();
    }

    public void sendRandomLore(MessageReceivedEvent event) {
        Lore lore = loreRepository.findRandom();
        StringBuilder builder = new StringBuilder();
        builder.append(lore.getLore());
        for(String link : lore.getAttachmentLinks()) {
            builder.append("\n").append(link);
        }

        event.getMessage().getChannel().sendMessage(builder.toString()).queue();
    }
}
