package com.harlyn.domain;

import javax.persistence.*;

/**
 * Created by wannabe on 15.11.15.
 */
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @Column(name = "id", updatable = false)
    private Integer id;

    private String name;

    public Role() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }
}
