package com.harlyn.domain;

import java.util.Date;

/**
 * Created by wannabe on 14.12.15.
 */
public class ChatMessage {
    private final String content;
    private final Date postedAt;
    private final User author;

    public ChatMessage(String content, Date postedAt, User author) {
        this.content = content;
        this.postedAt = postedAt;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public User getAuthor() {
        return author;
    }
}
