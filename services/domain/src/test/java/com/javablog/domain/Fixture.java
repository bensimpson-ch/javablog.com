package com.javablog.domain;

import com.javablog.domain.article.Article;
import com.javablog.domain.article.ArticleId;
import com.javablog.domain.article.ArticleTranslationRequest;
import com.javablog.domain.article.ArticleUpdate;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.TranslationJobId;
import com.javablog.domain.blog.TranslationRequest;

import java.util.Set;

public final class Fixture {

    private Fixture() {
    }

    public static PostId postId() {
        return PostId.generate();
    }

    public static CommentId commentId() {
        return CommentId.generate();
    }

    public static Title title() {
        return new Title("Test Title");
    }

    public static Slug slug() {
        return new Slug("test-slug");
    }

    public static Summary summary() {
        return new Summary("Test summary for the post");
    }

    public static Content content() {
        return new Content("Test content");
    }

    public static Author author() {
        return new Author("Ben");
    }

    public static CreatedAt createdAt() {
        return CreatedAt.now();
    }

    public static Languages languages() {
        return new Languages(Set.of(Language.EN));
    }

    public static TranslationJobId translationJobId() {
        return TranslationJobId.generate();
    }

    public static TranslationRequest translationRequest() {
        return new TranslationRequest(postId(), author(), languages());
    }

    public static TranslationRequest translationRequest(PostId postId) {
        return new TranslationRequest(postId, author(), languages());
    }

    public static TranslationRequest translationRequest(PostId postId, Set<Language> languages) {
        return new TranslationRequest(postId, author(), new Languages(languages));
    }

    public static Post post() {
        return new Post(postId(), slug(), title(), summary(), content(), Language.EN, createdAt());
    }

    public static Post post(String slugValue, String titleValue) {
        return new Post(postId(), new Slug(slugValue), new Title(titleValue), summary(), content(), Language.EN, createdAt());
    }

    public static Comment comment() {
        return new Comment(commentId(), postId(), author(), content(), createdAt());
    }

    public static Comment comment(PostId postId) {
        return new Comment(commentId(), postId, author(), content(), createdAt());
    }

    public static Comment comment(PostId postId, String authorName) {
        return new Comment(commentId(), postId, new Author(authorName), content(), createdAt());
    }

    public static ArticleId articleId() {
        return ArticleId.generate();
    }

    public static Article article() {
        return new Article(articleId(), slug(), title(), summary(), content(), Language.EN, createdAt());
    }

    public static Article article(String slugValue, String titleValue) {
        return new Article(articleId(), new Slug(slugValue), new Title(titleValue), summary(), content(), Language.EN, createdAt());
    }

    public static ArticleUpdate articleUpdate() {
        return new ArticleUpdate(articleId(), slug(), title(), summary(), content(), Language.EN);
    }

    public static ArticleTranslationRequest articleTranslationRequest() {
        return new ArticleTranslationRequest(articleId(), author(), languages());
    }

    public static ArticleTranslationRequest articleTranslationRequest(ArticleId articleId) {
        return new ArticleTranslationRequest(articleId, author(), languages());
    }

    public static ArticleTranslationRequest articleTranslationRequest(ArticleId articleId, Set<Language> languages) {
        return new ArticleTranslationRequest(articleId, author(), new Languages(languages));
    }
}
