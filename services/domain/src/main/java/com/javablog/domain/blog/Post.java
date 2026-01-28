package com.javablog.domain.blog;

import java.util.UUID;

public record Post(UUID id, String content) {
}