package com.harlyn.domain;

import javax.persistence.*;

/**
 * Created by wannabe on 16.11.15.
 */
@Entity
@Table(name = "confirm_codes")
public class ConfirmCode {
    @Id
    @SequenceGenerator(name = "confirm_codes_id_seq", sequenceName = "confirm_codes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirm_codes_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public ConfirmCode() {
    }

    public ConfirmCode(String code, User user) {
        this.code = code;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public ConfirmCode setCode(String code) {
        this.code = code;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ConfirmCode setUser(User user) {
        this.user = user;
        return this;
    }
}
