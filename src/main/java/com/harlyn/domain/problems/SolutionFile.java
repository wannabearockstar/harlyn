package com.harlyn.domain.problems;

import javax.persistence.*;

/**
 * Created by wannabe on 05.12.15.
 */
@Entity
@Table(name = "solution_files")
public class SolutionFile {

	@Id
	@SequenceGenerator(name = "solution_files_id_seq", sequenceName = "solution_files_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solution_files_id_seq")
	@Column(name = "id", updatable = false)
	private Long id;

	private String path;

	private String name;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "content_length")
	private Long contentLength;

	@ManyToOne(optional = false)
	@JoinColumn(name = "solution_id")
	private Solution solution;

	public SolutionFile() {
	}

	public SolutionFile(String path, Solution solution, String name, String contentType, Long contentLength) {
		this.path = path;
		this.solution = solution;
		this.name = name;
		this.contentType = contentType;
		this.contentLength = contentLength;
	}

	public Long getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public SolutionFile setPath(String path) {
		this.path = path;
		return this;
	}

	public Solution getSolution() {
		return solution;
	}

	public SolutionFile setSolution(Solution solution) {
		this.solution = solution;
		return this;
	}

	public String getName() {
		return name;
	}

	public SolutionFile setName(String name) {
		this.name = name;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public SolutionFile setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public SolutionFile setContentLength(Long contentLength) {
		this.contentLength = contentLength;
		return this;
	}
}
