ALTER TABLE teams DROP COLUMN points;

CREATE SEQUENCE competitions_id_seq;
CREATE TABLE competitions(
  id INTEGER PRIMARY KEY DEFAULT nextval('competitions_id_seq'),
  name VARCHAR(255),
  start_date TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  end_date TIMESTAMP WITH TIME ZONE DEFAULT NULL
);

CREATE SEQUENCE registered_teams_id_seq;
CREATE TABLE registered_teams(
  id INTEGER PRIMARY KEY DEFAULT nextval('registered_teams_id_seq'),
  competition_id INTEGER REFERENCES competitions (id),
  team_id INTEGER REFERENCES teams (id),
  points INTEGER DEFAULT 0,
  register_date TIMESTAMP WITH TIME ZONE DEFAULT NULL
);

ALTER TABLE problems ADD COLUMN competition_id INTEGER REFERENCES competitions (id);
