package com.harlyn.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by wannabe on 11.11.15.
 */
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @Column(name = "id", updatable = false)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "team")
    private Set<User> users;

    @OneToOne(targetEntity = User.class)
    private User captain;

    public Team(String name) {
        this.name = name;
    }

    public Team() {
    }

    public Integer getId() {
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
}
