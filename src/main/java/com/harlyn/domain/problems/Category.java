package com.harlyn.domain.problems;

import javax.persistence.*;

/**
 * Created by wannabe on 08.12.15.
 */
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @SequenceGenerator(name = "categories_id_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }
}
