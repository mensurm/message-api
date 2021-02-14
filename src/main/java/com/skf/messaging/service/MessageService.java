package com.skf.messaging.service;

import com.skf.messaging.controller.request.MessageRequest;
import com.skf.messaging.model.Message;
import com.skf.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void save(MessageRequest messageRequest) {
        LocalDateTime created = LocalDateTime.now();
        Message message = Message.builder()
            .created(created)
            .content(messageRequest.getContent())
            .id(UUID.randomUUID())
            .build();
        messageRepository.save(message, created.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    public Message getLast() {
        Set<Message> messages = messageRepository.getRange(-1, -1);
        if (messages.isEmpty()) {
            throw new MessageNotFoundException("No messages have been created");
        }
        return messages.iterator().next();
    }

    public List<Message> getBetween(LocalDateTime start, LocalDateTime end) {
        Set<Message> messagesInRange = messageRepository
            .getRangeByScore(
                start.toInstant(ZoneOffset.UTC).toEpochMilli(),
                end.toInstant(ZoneOffset.UTC).toEpochMilli()
            );
        if (messagesInRange.isEmpty()) {
            throw new MessageNotFoundException("No messages found in specified range");
        }
        return new ArrayList<>(messagesInRange);
    }
}
