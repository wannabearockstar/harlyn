package com.harlyn.domain.problems;

import com.harlyn.domain.Team;
import com.harlyn.domain.competitions.Competition;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by wannabe on 20.11.15.
 */
@Entity
@Table(name = "problems")
public class Problem {
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    @Id
    @SequenceGenerator(name = "problems_id_seq", sequenceName = "problems_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problems_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;
    private String name;
    private String info;
    private String answer;
    private Integer points;

    @Column(name = "start_date", nullable = true)
    private Date startDate;
    @Column(name = "end_date", nullable = true)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "problem_type")
    private ProblemType problemType;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teams_problems_solved",
            joinColumns = {@JoinColumn(name = "problem_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "id")}
    )
    private Set<Team> solverTeams = new HashSet<>();

    @OneToOne(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProblemFile file;

    public Problem() {
    }

    public Problem(String name, String answer, Integer points, ProblemType problemType, Competition competition) {
        this.name = name;
        this.answer = answer;
        this.points = points;
        this.problemType = problemType;
        this.competition = competition;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Problem setName(String name) {
        this.name = name;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public Problem setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public Problem setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public ProblemType getProblemType() {
        return problemType;
    }

    public Problem setProblemType(ProblemType problemType) {
        this.problemType = problemType;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public Problem setPoints(Integer points) {
        this.points = points;
        return this;
    }

    public Set<Team> getSolverTeams() {
        return solverTeams;
    }

    public Problem setSolverTeams(Set<Team> solverTeams) {
        this.solverTeams = solverTeams;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Problem))
            return false;

        Problem other = (Problem) o;

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

    public Date getStartDate() {
        return startDate;
    }

    public Problem setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Problem setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Problem setCompetition(Competition competition) {
        this.competition = competition;
        return this;
    }

    public ProblemFile getFile() {
        return file;
    }

    public Problem setFile(ProblemFile file) {
        this.file = file;
        return this;
    }

    public enum ProblemType {
        FLAG("Flag compare"),
        INFO_WEB("Manual checking of text info"),
        INFO_EMAIL("Manual checking of text info via email");

        private String localeName;

        ProblemType(String localeName) {
            this.localeName = localeName;
        }

        public String getLocaleName() {
            return localeName;
        }
    }
}
