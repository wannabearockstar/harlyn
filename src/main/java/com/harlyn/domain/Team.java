package com.harlyn.domain;

import com.harlyn.domain.competitions.RegisteredTeam;
import com.harlyn.domain.problems.Problem;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
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

	@OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<User> users = new HashSet<>();

	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "captain_id")
	private User captain;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "solverTeams")
	private Set<Problem> solvedProblems = new HashSet<>();

	@OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST)
	private Set<RegisteredTeam> registeredTeams = new HashSet<>();

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

	public Set<Problem> getSolvedProblems() {
		return solvedProblems;
	}

	public Team setSolvedProblems(Set<Problem> solvedProblems) {
		this.solvedProblems = solvedProblems;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof Team))
			return false;

		Team other = (Team) o;

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

	public Set<RegisteredTeam> getRegisteredTeams() {
		return registeredTeams;
	}

	public Team setRegisteredTeams(Set<RegisteredTeam> registeredTeams) {
		this.registeredTeams = registeredTeams;
		return this;
	}
}
