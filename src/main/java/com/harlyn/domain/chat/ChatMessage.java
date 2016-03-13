package com.harlyn.domain.chat;

import com.harlyn.domain.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by wannabe on 14.12.15.
 */
@MappedSuperclass
public class ChatMessage {

	protected String content;

	@Column(name = "posted_at")
	protected Date postedAt;

	@ManyToOne
	@JoinColumn(name = "author_id")
	protected User author;

	public ChatMessage(String content, Date postedAt, User author) {
		this.content = content;
		this.postedAt = postedAt;
		this.author = author;
	}

	public ChatMessage(String content) {
		this.content = content;
		this.postedAt = null;
		this.author = null;
	}

	public ChatMessage() {
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
