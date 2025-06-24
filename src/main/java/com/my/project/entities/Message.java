package com.my.project.entities;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Message {

    private String sender;
    private String messageContent;
    private LocalDateTime time;

    public Message(String messageContent, String sender) {
        this.messageContent = messageContent;
        this.sender = sender;
        this.time = LocalDateTime.now();
    }
}
