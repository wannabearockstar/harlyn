package com.harlyn.domain.competitions;

import com.harlyn.domain.problems.Problem;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by wannabe on 01.12.15.
 */
@Entity
@Table(name = "competitions")
public class Competition {
    @Id
    @SequenceGenerator(name = "competitions_id_seq", sequenceName = "competitions_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "competitions_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    private String name;

    @Column(name = "start_date", nullable = true)
    private Date startDate;

    @Column(name = "end_date", nullable = true)
    private Date endDate;

    @OneToMany(mappedBy = "competition", fetch = FetchType.EAGER)
    @OrderBy(value = "points DESC")
    private Set<RegisteredTeam> registeredTeams = new HashSet<>();

    @OneToMany(mappedBy = "competition", fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    private Set<Problem> problems;

    public Competition() {
    }

    public Competition(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Competition setName(String name) {
        this.name = name;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Competition setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Competition setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Set<RegisteredTeam> getRegisteredTeams() {
        return registeredTeams;
    }

    public Competition setRegisteredTeams(Set<RegisteredTeam> registeredTeams) {
        this.registeredTeams = registeredTeams;
        return this;
    }

    public Set<Problem> getProblems() {
        return problems;
    }

    public Competition setProblems(Set<Problem> problems) {
        this.problems = problems;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Competition))
            return false;

        Competition other = (Competition) o;

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
