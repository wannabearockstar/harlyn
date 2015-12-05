package com.harlyn.domain.problems;

import javax.persistence.*;

/**
 * Created by wannabe on 05.12.15.
 */
@Entity
@Table(name = "problem_files")
public class ProblemFile {
    @Id
    @SequenceGenerator(name = "problem_files_id_seq", sequenceName = "problem_files_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_files_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    private String path;

    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public ProblemFile() {
    }

    public ProblemFile(String path, Problem problem) {
        this.path = path;
        this.problem = problem;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public ProblemFile setPath(String path) {
        this.path = path;
        return this;
    }

    public Problem getProblem() {
        return problem;
    }

    public ProblemFile setProblem(Problem problem) {
        this.problem = problem;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProblemFile setName(String name) {
        this.name = name;
        return this;
    }
}
