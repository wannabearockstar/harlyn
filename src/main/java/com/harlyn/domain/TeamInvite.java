package com.harlyn.domain;

import javax.persistence.*;

/**
 * Created by wannabe on 18.11.15.
 */
@Entity
@Table(name = "team_invites")
public class TeamInvite {
    @Id
    @SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "recipent_id", nullable = false)
    private User recipent;

    public TeamInvite() {
    }

    public TeamInvite(Team team, User recipent) {
        this.team = team;
        this.recipent = recipent;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public TeamInvite setTeam(Team team) {
        this.team = team;
        return this;
    }

    public User getRecipent() {
        return recipent;
    }

    public TeamInvite setRecipent(User recipent) {
        this.recipent = recipent;
        return this;
    }
}
