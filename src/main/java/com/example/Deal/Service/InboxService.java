package com.example.Deal.Service;

import com.example.Deal.DTO.rabbit.RabbitMessage;
import com.example.Deal.Repository.RabbitMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides means to manage messages in 'inbox' database table.
 * Used to release inbox pattern.
 */
@Service
@RequiredArgsConstructor
public class InboxService {

    private final RabbitMessageRepository repository;

    /**
     * Checks if passed message is actual.
     * Compares timestamp of stored and passed messages.
     * Passed message is actual if its timestamp value is
     * less than stored message timestamp value.
     * Otherwise - message is not actual.
     *
     * @param message message must be checked
     * @return true if actual, false is not actual
     */
    public boolean isActual(RabbitMessage message) {
        Optional<RabbitMessage> optMessage = repository.findById(message.getId());
        if (optMessage.isPresent()) {
            if (optMessage.get().getTime().after(message.getTime())) {
                return false;
            }
        }
        repository.save(message);
        return true;
    }

}
