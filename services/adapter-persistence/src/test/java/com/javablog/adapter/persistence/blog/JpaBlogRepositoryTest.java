package com.javablog.adapter.persistence.blog;

import com.javablog.domain.*;
import com.javablog.domain.blog.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaBlogRepository.class)
@ActiveProfiles("test")
class JpaBlogRepositoryTest {

    private static final int SEED_POST_COUNT = 6;

    @Autowired
    private JpaBlogRepository repository;

    @Test
    void postRoundTrip() {
        int initialCount = repository.listPosts(Language.EN).values().size();
        assertThat(initialCount).isEqualTo(SEED_POST_COUNT);

        // Create
        Post post = Fixture.post();
        repository.create(post);

        // Read by ID
        Post found = repository.findPostById(post.id()).orElseThrow();
        assertThat(found.id()).isEqualTo(post.id());
        assertThat(found.slug()).isEqualTo(post.slug());
        assertThat(found.title()).isEqualTo(post.title());
        assertThat(found.summary()).isEqualTo(post.summary());
        assertThat(found.content()).isEqualTo(post.content());

        // Read by slug
        Post foundBySlug = repository.findPostBySlug(post.slug()).orElseThrow();
        assertThat(foundBySlug.id()).isEqualTo(post.id());

        // List posts includes new post
        Posts posts = repository.listPosts(Language.EN);
        assertThat(posts.values()).hasSize(SEED_POST_COUNT + 1);
        assertThat(posts.values()).anyMatch(p -> p.id().equals(post.id()));

        // Update
        Post updated = new Post(
                post.id(),
                new Slug("updated-slug"),
                new Title("Updated Title"),
                new Summary("Updated summary"),
                new Content("Updated content"),
                Language.DE,
                post.createdAt()
        );
        repository.update(updated);

        Post afterUpdate = repository.findPostById(post.id()).orElseThrow();
        assertThat(afterUpdate.slug().value()).isEqualTo("updated-slug");
        assertThat(afterUpdate.title().value()).isEqualTo("Updated Title");

        // Delete
        repository.delete(post.id());

        // Verify delete
        assertThat(repository.findPostById(post.id())).isEmpty();
        assertThat(repository.listPosts(Language.EN).values()).hasSize(SEED_POST_COUNT);
    }

    @Test
    void commentRoundTrip() {
        // Use an existing seeded post
        Post existingPost = repository.listPosts(Language.EN).values().iterator().next();

        // Create comment
        Comment comment = Fixture.comment(existingPost.id());
        repository.create(comment);

        // List comments
        Comments comments = repository.listComments(existingPost);
        assertThat(comments.values()).hasSize(1);
        Comment found = comments.values().iterator().next();
        assertThat(found.id()).isEqualTo(comment.id());
        assertThat(found.postId()).isEqualTo(existingPost.id());
        assertThat(found.author()).isEqualTo(comment.author());
        assertThat(found.content()).isEqualTo(comment.content());

        // Delete comment
        repository.delete(comment.id());

        // Verify delete
        Comments afterDelete = repository.listComments(existingPost);
        assertThat(afterDelete.values()).isEmpty();
    }
}
