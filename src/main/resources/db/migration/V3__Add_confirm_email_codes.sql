CREATE SEQUENCE confirm_codes_id_seq;
CREATE TABLE confirm_codes(
  id INTEGER PRIMARY KEY DEFAULT nextval('confirm_codes_id_seq'),
  user_id INTEGER REFERENCES users (id) UNIQUE ,
  code VARCHAR(255) NOT NULL UNIQUE
);