ALTER TABLE translation_jobs ADD COLUMN completed_at TIMESTAMP;

ALTER TABLE article_translation_jobs ADD COLUMN completed_at TIMESTAMP;
