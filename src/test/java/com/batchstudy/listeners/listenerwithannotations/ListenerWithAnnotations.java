package com.batchstudy.listeners.listenerwithannotations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListenerWithAnnotations {

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        log.info("Before chunk");
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        log.info("After chunk");
    }
}
