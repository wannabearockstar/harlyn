CREATE SEQUENCE problem_files_id_seq;
CREATE TABLE problem_files(
  id INTEGER PRIMARY KEY DEFAULT nextval('problem_files_id_seq'),
  name VARCHAR(255),
  path VARCHAR(255),
  problem_id INTEGER REFERENCES problems(id) UNIQUE
);
ALTER TABLE problems ADD COLUMN file_id INTEGER REFERENCES problem_files(id) UNIQUE DEFAULT NULL;

CREATE SEQUENCE solution_files_id_seq;
CREATE TABLE solution_files(
  id INTEGER PRIMARY KEY DEFAULT nextval('solution_files_id_seq'),
  name VARCHAR(255),
  path VARCHAR(255),
  solution_id INTEGER REFERENCES solutions(id) UNIQUE
);
ALTER TABLE solutions ADD COLUMN file_id INTEGER REFERENCES solution_files(id) UNIQUE DEFAULT NULL;