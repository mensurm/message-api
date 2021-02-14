package com.skf.messaging.repository;

import com.skf.messaging.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private static final String HASH_KEY = "message";
    private final RedisTemplate redisTemplate;

    public void save(Message message, double score) {
        redisTemplate.opsForZSet().add(HASH_KEY, message, score);
    }

    public Set<Message> getRange(int rangeStart, int rangeEnd) {
        return (Set<Message>) redisTemplate.opsForZSet().range(HASH_KEY, rangeStart, rangeEnd);
    }

    public Set<Message> getRangeByScore(double scoreMin, double scoreMax) {
        return (Set<Message>) redisTemplate.opsForZSet().rangeByScore(HASH_KEY, scoreMin, scoreMax);
    }

}
