CREATE SEQUENCE team_invites_id_seq;
CREATE TABLE team_invites(
  id INTEGER PRIMARY KEY DEFAULT nextval('team_invites_id_seq'),
  team_id INTEGER REFERENCES teams (id),
  recipent_id INTEGER REFERENCES users (id)
);