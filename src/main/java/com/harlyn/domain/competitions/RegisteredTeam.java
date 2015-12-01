package com.harlyn.domain.competitions;

import com.harlyn.domain.Team;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by wannabe on 01.12.15.
 */
@Entity
@Table(name = "registered_teams")
public class RegisteredTeam {
    @Id
    @SequenceGenerator(name = "registered_teams_id_seq", sequenceName = "registered_teams_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registered_teams_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private Integer points = 0;

    @Column(name = "register_date")
    private Date registerDate;

    public RegisteredTeam(Competition competition, Team team) {
        this.competition = competition;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public Competition getCompetition() {
        return competition;
    }

    public RegisteredTeam setCompetition(Competition competition) {
        this.competition = competition;
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public RegisteredTeam setTeam(Team team) {
        this.team = team;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public RegisteredTeam setPoints(Integer points) {
        this.points = points;
        return this;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public RegisteredTeam setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    @PrePersist
    public void initValues() {
        registerDate = new Date();
        points = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof RegisteredTeam))
            return false;

        RegisteredTeam other = (RegisteredTeam) o;

        if (id == null) return false;
        if (Objects.equals(id, other.getId())) return true;

        return id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }
}
