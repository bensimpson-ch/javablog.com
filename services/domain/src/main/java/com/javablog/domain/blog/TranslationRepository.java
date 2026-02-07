package com.javablog.domain.blog;

public interface TranslationRepository {

    void saveTranslationJob(TranslationJobId jobId, PostId postId, Language language);
}
