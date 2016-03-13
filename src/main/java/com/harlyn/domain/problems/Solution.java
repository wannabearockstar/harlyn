package com.harlyn.domain.problems;

import com.harlyn.domain.User;

import javax.persistence.*;

/**
 * Created by wannabe on 20.11.15.
 */
@Entity
@Table(name = "solutions")
public class Solution {

	@Id
	@SequenceGenerator(name = "solutions_id_seq", sequenceName = "solutions_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solutions_id_seq")
	@Column(name = "id", updatable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "problem_id")
	private Problem problem;

	@ManyToOne
	@JoinColumn(name = "solver_id")
	private User solver;

	private boolean correct;

	private boolean checked;

	private String answer;

	@OneToOne(mappedBy = "solution", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private SolutionFile file;

	public Solution() {
	}

	public Solution(Problem problem, User solver) {
		this.problem = problem;
		this.solver = solver;
		this.correct = false;
		this.checked = false;
	}


	public Long getId() {
		return id;
	}

	public Problem getProblem() {
		return problem;
	}

	public Solution setProblem(Problem problem) {
		this.problem = problem;
		return this;
	}

	public User getSolver() {
		return solver;
	}

	public Solution setSolver(User solver) {
		this.solver = solver;
		return this;
	}

	public boolean isChecked() {
		return checked;
	}

	public Solution setChecked(boolean checked) {
		this.checked = checked;
		return this;
	}

	public boolean isCorrect() {
		return correct;
	}

	public Solution setCorrect(boolean correct) {
		this.correct = correct;
		return this;
	}

	public String getAnswer() {
		return answer;
	}

	public Solution setAnswer(String answer) {
		this.answer = answer;
		return this;
	}

	public SolutionFile getFile() {
		return file;
	}

	public Solution setFile(SolutionFile file) {
		this.file = file;
		return this;
	}
}
