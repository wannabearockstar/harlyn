ALTER TABLE problem_files ADD COLUMN content_type VARCHAR(255);
ALTER TABLE problem_files ADD COLUMN content_length BIGINT;

ALTER TABLE solution_files ADD COLUMN content_type VARCHAR(255);
ALTER TABLE solution_files ADD COLUMN content_length BIGINT;