package com.harlyn.domain.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harlyn.domain.Team;
import com.harlyn.domain.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wannabe on 14.12.15.
 */
@Entity
@Table(name = "team_messages")
public class TeamChatMessage extends ChatMessage {

	@Id
	@SequenceGenerator(name = "team_messages_id_seq", sequenceName = "team_messages_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_messages_id_seq")
	@Column(name = "id", updatable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "team_id")
	@JsonIgnore
	private Team team;

	public TeamChatMessage() {
	}

	public TeamChatMessage(String content, Date postedAt, User author, Team team) {
		super(content, postedAt, author);
		this.team = team;
	}

	public Long getId() {
		return id;
	}

	public Team getTeam() {
		return team;
	}

	public TeamChatMessage setTeam(Team team) {
		this.team = team;
		return this;
	}
}
