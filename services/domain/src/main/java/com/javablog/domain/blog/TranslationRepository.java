package com.javablog.domain.blog;

import com.javablog.domain.*;

import java.util.Optional;

public interface TranslationRepository {

    void saveTranslationJob(TranslationJobId jobId, PostId postId, Language language);

    Optional<TranslationJob> findTranslationJob(TranslationJobId jobId);

    void saveTranslatedPost(PostId originalPostId, Language language, Title title, Summary summary, Slug slug, Content content);

    void completeTranslationJob(TranslationJobId jobId);

    TranslatedPosts findTranslations(PostId originalPostId);
}
