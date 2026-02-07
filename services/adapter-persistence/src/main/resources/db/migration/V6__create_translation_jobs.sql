CREATE TABLE translation_jobs (
    translation_job_id UUID PRIMARY KEY,
    post_id UUID NOT NULL REFERENCES posts(post_id) ON DELETE CASCADE,
    language_code VARCHAR(2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (post_id, language_code)
);
