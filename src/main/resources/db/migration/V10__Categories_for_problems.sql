CREATE SEQUENCE categories_id_seq;
CREATE TABLE categories(
  id INTEGER PRIMARY KEY DEFAULT nextval('categories_id_seq'),
  name VARCHAR(255)
);

ALTER TABLE problems ADD COLUMN category_id INTEGER REFERENCES categories (id) DEFAULT NULL;