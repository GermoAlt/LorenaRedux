package dev.altairac.lorenaredux.model;

import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

@Entity
public class Lore {
    @Id
    private Long messageId;
    private String lore;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lore_attachments", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "link")
    private List<String> attachmentLinks;
    private String linkToMessage;

    public Lore() {}

    public Lore(Long messageId, String lore, User author, List<String> attachmentLinks, String linkToMessage) {
        this.messageId = messageId;
        this.lore = lore;
        this.author = author;
        this.attachmentLinks = attachmentLinks;
        this.linkToMessage = linkToMessage;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getLore() {
        return lore;
    }

    public User getAuthor() {
        return author;
    }

    public List<String> getAttachmentLinks() {
        return attachmentLinks;
    }

    public String getLinkToMessage() {
        return linkToMessage;
    }
}
