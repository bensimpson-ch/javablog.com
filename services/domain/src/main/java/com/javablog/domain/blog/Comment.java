package com.javablog.domain.blog;

import java.util.UUID;

public record Comment(UUID id, UUID postId, String content) {
}