ALTER TABLE translation_jobs RENAME COLUMN post_id TO original_post_id;
ALTER TABLE translation_jobs ADD COLUMN slug VARCHAR(255);
