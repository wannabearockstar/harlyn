CREATE SEQUENCE hints_id_seq;
CREATE TABLE hints (
  id INTEGER PRIMARY KEY DEFAULT nextval('hints_id_seq'),
  problem_id INTEGER REFERENCES problems (id),
  content TEXT,
  posted_at TIMESTAMP WITH TIME ZONE
);