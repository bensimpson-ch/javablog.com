package com.javablog.domain.blog;

import java.util.List;

public interface TranslationRepository {

    void saveTranslationJob(TranslationJobId jobId, PostId postId, Language language);

    TranslatedPosts findTranslations(PostId originalPostId);
}
