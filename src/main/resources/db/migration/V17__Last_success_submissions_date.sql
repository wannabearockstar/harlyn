ALTER TABLE registered_teams ALTER COLUMN register_date DROP NOT NULL;
ALTER TABLE registered_teams RENAME COLUMN register_date TO last_success_submission_date;
