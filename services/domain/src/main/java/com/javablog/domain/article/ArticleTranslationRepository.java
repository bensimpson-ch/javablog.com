package com.javablog.domain.article;

import com.javablog.domain.*;
import com.javablog.domain.blog.*;

import java.util.Optional;

public interface ArticleTranslationRepository {

    void saveTranslationJob(TranslationJobId jobId, ArticleId articleId, Language language);

    Optional<ArticleTranslationJob> findTranslationJob(TranslationJobId jobId);

    void saveTranslatedArticle(ArticleId originalArticleId, Language language, Title title, Summary summary, Slug slug, Content content);

    void deleteTranslationJob(TranslationJobId jobId);

    TranslatedArticles findTranslations(ArticleId originalArticleId);
}
