package com.javablog.domain;

import com.javablog.domain.blog.Author;
import com.javablog.domain.blog.Comment;
import com.javablog.domain.blog.CommentId;
import com.javablog.domain.blog.Content;
import com.javablog.domain.blog.CreatedAt;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.PostId;
import com.javablog.domain.blog.Slug;
import com.javablog.domain.blog.Summary;
import com.javablog.domain.blog.Title;

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

    public static Post post() {
        return new Post(postId(), slug(), title(), summary(), content(), createdAt());
    }

    public static Post post(String slugValue, String titleValue) {
        return new Post(postId(), new Slug(slugValue), new Title(titleValue), summary(), content(), createdAt());
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
}
