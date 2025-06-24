package com.my.project.Controllers;

import com.my.project.entities.Message;
import com.my.project.entities.Room;
import com.my.project.playload.MessageRequest;
import com.my.project.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
public class ChatController{

    @Autowired
    private RoomRepository roomRepository;

    public ChatController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    //for sending and receiving messages
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ){
      Room room =  roomRepository.findByRoomId(request.getRoomId());

      Message message = new Message();
      message.setMessageContent(request.getContent());
      message.setSender(request.getSender());
      message.setTime(LocalDateTime.now());

      if(room != null){
          room.getMessages().add(message);
          roomRepository.save(room);
      }else{
          throw new RuntimeException("room not found !!");
      }
        return message;
    }

}
