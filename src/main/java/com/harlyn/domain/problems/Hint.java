package com.harlyn.domain.problems;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wannabe on 12.03.16.
 */
@Entity
@Table(name = "hints")
public class Hint {
	@Id
	@SequenceGenerator(name = "hints_id_seq", sequenceName = "hints_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hints_id_seq")
	@Column(name = "id", updatable = false)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "problem_id")
	private Problem problem;
	private String content;
	@Column(name = "posted_at")
	private Date postedAt;

	Hint() {}

	public Hint(String content, Problem problem) {
		this.content = content;
		this.problem = problem;
	}

	public String getContent() {
		return content;
	}

	public Hint setContent(String content) {
		this.content = content;
		return this;
	}

	public Long getId() {
		return id;
	}

	public Problem getProblem() {
		return problem;
	}

	public Hint setProblem(Problem problem) {
		this.problem = problem;
		return this;
	}

	@PrePersist
	public void initValues() {
		postedAt = new Date();
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public Hint setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
		return this;
	}
}
