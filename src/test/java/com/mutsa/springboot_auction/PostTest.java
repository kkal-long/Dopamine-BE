package com.mutsa.springboot_auction;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void setContent() {
        Post post = new Post(1L, "CI제발돼라", LocalDateTime.now());
        post.setContent("과연될까?");
        assertEquals("과연될까?", post.getContent());
    }
}