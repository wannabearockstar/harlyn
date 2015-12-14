package com.harlyn.domain.chat;

import com.harlyn.domain.User;
import com.harlyn.domain.competitions.Competition;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wannabe on 14.12.15.
 */
@Entity
@Table(name = "competition_messages")
public class CompetitionChatMessage extends ChatMessage {
    @Id
    @SequenceGenerator(name = "competition_messages_id_seq", sequenceName = "competition_messages_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "competition_messages_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    public CompetitionChatMessage() {
    }

    public CompetitionChatMessage(String content, Date postedAt, User author, Competition competition) {
        super(content, postedAt, author);
        this.competition = competition;
    }

    public Long getId() {
        return id;
    }

    public Competition getCompetition() {
        return competition;
    }

    public CompetitionChatMessage setCompetition(Competition competition) {
        this.competition = competition;
        return this;
    }
}
