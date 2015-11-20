package com.harlyn.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wannabe on 11.11.15.
 */
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @SequenceGenerator(name = "teams_id_seq", sequenceName = "teams_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teams_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<User> users = new HashSet<>();

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "captain_id")
    private User captain;

    private Integer points;

    public Team(String name) {
        this.name = name;
    }

    public Team() {
    }

    public Team(String name, User captain) {
        this(name);
        this.captain = captain;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Team setUsers(Set<User> users) {
        this.users = users;
        return this;
    }

    public Team addUser(User user) {
        this.users.add(user);
        user.setTeam(this);
        return this;
    }

    public User getCaptain() {
        return captain;
    }

    public Team setCaptain(User captain) {
        this.captain = captain;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public Team setPoints(Integer points) {
        this.points = points;
        return this;
    }

    @PrePersist
    public void initValues() {
        this.points = 0;
    }
}
