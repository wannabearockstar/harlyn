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
    @ManyToOne
    @JoinColumn(name = "competition_id")
    private final Competition competition;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private final Team team;
    @Id
    @SequenceGenerator(name = "registered_teams_id_seq", sequenceName = "registered_teams_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registered_teams_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;
    private Integer points;

    @Column(name = "register_date")
    private Date registerDate;

    RegisteredTeam() {
        competition = null;
        team = null;
    }

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

    public Team getTeam() {
        return team;
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
