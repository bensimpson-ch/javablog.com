package com.javablog.domain.blog;

import com.javablog.domain.Language;

public interface TranslationPort {

    TranslationJobId translate(Post post, Language targetLanguage);
}
