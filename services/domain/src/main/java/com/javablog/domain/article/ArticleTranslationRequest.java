package com.javablog.domain.article;

import com.javablog.domain.Guard;
import com.javablog.domain.Author;
import com.javablog.domain.Languages;

public record ArticleTranslationRequest(ArticleId articleId, Author author, Languages languages) {

    public ArticleTranslationRequest {
        Guard.againstNull(articleId, "ArticleTranslationRequest.articleId");
        Guard.againstNull(author, "ArticleTranslationRequest.author");
        Guard.againstNull(languages, "ArticleTranslationRequest.languages");
    }
}
