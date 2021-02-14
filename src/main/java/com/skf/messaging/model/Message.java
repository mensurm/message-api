package com.skf.messaging.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 6663625581593300518L;

    @Id
    private final UUID id;
    private final LocalDateTime created;
    private final Map<String, String> content;
}
