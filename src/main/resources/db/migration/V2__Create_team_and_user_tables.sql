CREATE SEQUENCE teams_id_seq;
CREATE TABLE teams (
  id INTEGER PRIMARY KEY DEFAULT nextval('teams_id_seq'),
  name VARCHAR(255) NOT NULL UNIQUE
);

CREATE SEQUENCE users_id_seq;
CREATE TABLE users(
  id INTEGER PRIMARY KEY DEFAULT nextval('users_id_seq'),
  email VARCHAR(255) NOT NULL UNIQUE,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(512) NOT NULL,
  team_id INTEGER REFERENCES teams (id) DEFAULT NULL,
  enabled BOOLEAN DEFAULT FALSE
);

ALTER TABLE teams ADD COLUMN captain_id INTEGER REFERENCES users(id) DEFAULT NULL;

CREATE SEQUENCE roles_id_seq;
CREATE TABLE roles(
  id INTEGER PRIMARY KEY DEFAULT nextval('roles_id_seq'),
  name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');

CREATE SEQUENCE user_roles_id_seq;
CREATE TABLE user_roles(
  id INTEGER PRIMARY KEY DEFAULT nextval('user_roles_id_seq'),
  user_id INTEGER REFERENCES users (id),
  role_id INTEGER REFERENCES roles (id)
);
