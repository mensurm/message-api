package com.skf.messaging.controller;

import com.skf.messaging.controller.request.MessageRequest;
import com.skf.messaging.model.Message;
import com.skf.messaging.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class MessageController {

    private final MessageService messageService;

    public MessageController(@Autowired MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/publish")
    @ResponseBody
    public ResponseEntity publishMessage(@RequestBody MessageRequest message) {
        log.info("Publishing message");
        messageService.save(message);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/getLast")
    @ResponseBody
    public Message getLastMessage() {
        log.info("Fetching last message");
        return messageService.getLast();
    }

    @GetMapping("/getByTime")
    @ResponseBody
    public List<Message> getMessagesInRange(
        @RequestParam("start")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        log.info("fetching messages in range");
        return messageService.getBetween(startTime, endTime);
    }

}
