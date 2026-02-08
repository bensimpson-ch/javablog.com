package com.javablog.adapter.persistence.blog;

import com.javablog.domain.Fixture;
import com.javablog.domain.blog.Language;
import com.javablog.domain.blog.Post;
import com.javablog.domain.blog.TranslationJobId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaTranslationRepository.class, JpaBlogRepository.class})
@ActiveProfiles("test")
class JpaTranslationRepositoryTest {

    @Autowired
    private JpaTranslationRepository translationRepository;

    @Autowired
    private JpaBlogRepository blogRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void translationJobRoundTrip() {
        // Use an existing seeded post
        Post existingPost = blogRepository.listPosts(Language.EN).values().iterator().next();

        // Create translation job
        TranslationJobId jobId = Fixture.translationJobId();
        translationRepository.saveTranslationJob(jobId, existingPost.id(), Language.DE);

        // Flush and clear to force database read
        entityManager.flush();
        entityManager.clear();

        // Verify job was saved
        TranslationJobEntity found = entityManager.find(TranslationJobEntity.class, jobId.value());
        assertThat(found).isNotNull();
        assertThat(found.getTranslationJobId()).isEqualTo(jobId.value());
        assertThat(found.getOriginalPostId()).isEqualTo(existingPost.id().value());
        assertThat(found.getLanguageCode()).isEqualTo("de");
        assertThat(found.getCreatedAt()).isNotNull();
    }
}
