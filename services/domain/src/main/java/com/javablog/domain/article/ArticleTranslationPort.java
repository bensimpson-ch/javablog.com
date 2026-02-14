package com.javablog.domain.article;

import com.javablog.domain.Language;
import com.javablog.domain.blog.TranslationJobId;

public interface ArticleTranslationPort {

    TranslationJobId translate(Article article, Language targetLanguage);
}
