package com.my.project.Controllers;

import com.my.project.entities.Message;
import com.my.project.entities.Room;
import com.my.project.repo.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restapi/p1/rooms")
public class RoomController {
    private RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId) {
        if (roomRepository.findByRoomId(roomId) != null) {
            return ResponseEntity.badRequest().body("Room is already present");
        }

        Room room = new Room();
        room.setRoomId(roomId);
        Room save = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoomAuthorization(@PathVariable String roomId) {
        Room presentRoom = roomRepository.findByRoomId(roomId);

        if (presentRoom == null) {
            return ResponseEntity.badRequest().body("Room not present");
        }

        return ResponseEntity.ok(presentRoom);
    }


    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId,
                                                     @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                     @RequestParam(value = "size", defaultValue = "20", required = false) int size) {


       Room room =  roomRepository.findByRoomId(roomId);

       if(room == null){
           return ResponseEntity.badRequest().build();
       }
       List<Message> messages = room.getMessages();

       int start = Math.max(0, messages.size() - (page+1)*size);
       int end = Math.min(messages.size(), start + size);
       List<Message> paginatedMessages  = messages.subList(start, end);
       return ResponseEntity.ok(messages);
    }
}
