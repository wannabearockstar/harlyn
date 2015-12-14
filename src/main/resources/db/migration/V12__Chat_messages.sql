CREATE SEQUENCE team_messages_id_seq;
CREATE TABLE team_messages(
  id INTEGER PRIMARY KEY DEFAULT nextval('team_messages_id_seq'),
  content TEXT DEFAULT NULL ,
  posted_at TIMESTAMP WITH TIME ZONE,
  author_id INTEGER REFERENCES users (id),
  team_id INTEGER REFERENCES teams (id)
);

CREATE SEQUENCE competition_messages_id_seq;
CREATE TABLE competition_messages(
  id INTEGER PRIMARY KEY DEFAULT nextval('competition_messages_id_seq'),
  content TEXT DEFAULT NULL ,
  posted_at TIMESTAMP WITH TIME ZONE,
  author_id INTEGER REFERENCES users (id),
  competition_id INTEGER REFERENCES competitions (id)
);