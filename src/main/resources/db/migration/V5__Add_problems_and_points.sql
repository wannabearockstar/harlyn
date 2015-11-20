ALTER TABLE teams ADD COLUMN points INTEGER DEFAULT 0;

CREATE SEQUENCE problems_id_seq;
CREATE TABLE problems(
  id INTEGER PRIMARY KEY DEFAULT nextval('problems_id_seq'),
  name VARCHAR(255) NOT NULL,
  info TEXT DEFAULT NULL,
  answer VARCHAR(1024) DEFAULT NULL,
  points INTEGER NOT NULL,
  problem_type VARCHAR(100) NOT NULL
);

CREATE SEQUENCE solutions_id_seq;
CREATE TABLE solutions(
  id INTEGER PRIMARY KEY DEFAULT nextval('solutions_id_seq'),
  problem_id INTEGER REFERENCES problems (id),
  solver_id INTEGER REFERENCES users (id),
  correct BOOLEAN DEFAULT FALSE,
  checked BOOLEAN DEFAULT FALSE,
  answer VARCHAR(1024) DEFAULT NULL
);