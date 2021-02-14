package com.skf.messaging.service;

import com.skf.messaging.model.Message;
import com.skf.messaging.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class MessageServiceTest {

    private static final String TEST_KEY = "name";
    private static final String TEST_VALUE = "John Doe";

    MessageRepository messageRepositoryMock;

    @BeforeEach
    public void setup() {
        messageRepositoryMock = Mockito.mock(MessageRepository.class);
    }

    @Test
    void testMessagesInRangeNotFound() {
        // given
        when(messageRepositoryMock.getRangeByScore(anyInt(), anyInt())).thenReturn(Collections.emptySet());
        MessageService messageService = new MessageService(messageRepositoryMock);

        // when
        MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
            () -> messageService.getBetween(LocalDateTime.now(), LocalDateTime.now())
            , "Exception not throw when no messages in range"
        );

        // then
        assertEquals("No messages found in specified range", exception.getMessage());
    }

    @Test
    public void testGetLastNotFound() {
        // given
        when(messageRepositoryMock.getRange(anyInt(), anyInt())).thenReturn(Collections.emptySet());
        MessageService messageService = new MessageService(messageRepositoryMock);

        // when
        MessageNotFoundException exception = assertThrows(MessageNotFoundException.class, messageService::getLast
            , "Exception not throw when no messages created");

        // then
        assertEquals("No messages have been created", exception.getMessage());
    }

    @Test
    public void messagesInRangeFound() {
        // given
        LocalDateTime created = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        HashMap<String, String> content = new HashMap<>();
        content.put(TEST_KEY, TEST_VALUE);

        Message message = Message.builder()
            .id(id)
            .content(content)
            .created(created)
            .build();

        when(messageRepositoryMock
            .getRangeByScore(
                created.toInstant(ZoneOffset.UTC).toEpochMilli(),
                created.toInstant(ZoneOffset.UTC).toEpochMilli()
            ))
            .thenReturn(Collections.singleton(message));
        MessageService messageService = new MessageService(messageRepositoryMock);

        // when
        List<Message> messagesInRange = messageService.getBetween(created, created);

        // then
        assertEquals(1, messagesInRange.size());
        Message result = messagesInRange.get(0);
        assertEquals(created, result.getCreated());
        assertEquals(id, message.getId());
        assertEquals(TEST_VALUE, message.getContent().get(TEST_KEY));
    }

    @Test
    public void lastMessageFound() {
        // given
        LocalDateTime created = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        HashMap<String, String> content = new HashMap<>();
        content.put(TEST_KEY, TEST_VALUE);

        Message message = Message.builder()
            .id(id)
            .content(content)
            .created(created)
            .build();

        when(messageRepositoryMock
            .getRange(-1, -1))
            .thenReturn(Collections.singleton(message));
        MessageService messageService = new MessageService(messageRepositoryMock);

        // when
        Message lastMessage = messageService.getLast();

        // then
        assertEquals(created, lastMessage.getCreated());
        assertEquals(id, lastMessage.getId());
        assertEquals(TEST_VALUE, lastMessage.getContent().get(TEST_KEY));
    }

}
