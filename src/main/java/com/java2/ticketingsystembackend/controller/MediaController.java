package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.CreateMediaDTO;
import com.java2.ticketingsystembackend.dto.MediaDTO;
import com.java2.ticketingsystembackend.entity.Media;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.mapper.MediaMapper;
import com.java2.ticketingsystembackend.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/event/{uuid}")
    public ResponseEntity<List<MediaDTO>> getMediaByEvent(@PathVariable String uuid) {
        try {
            return ResponseEntity.ok(mediaService.getMediaByEventUuid(uuid));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<MediaDTO> addMediaToEvent(@PathVariable Integer eventId, @RequestBody CreateMediaDTO mediaDTO) {
        Media media = mediaService.addMediaToEvent(eventId, mediaDTO);
        return ResponseEntity.ok(MediaMapper.toMediaDTO(media));
    }

    @PutMapping("/{mediaId}")
    public ResponseEntity<MediaDTO> updateMedia(@PathVariable Integer mediaId, @RequestBody CreateMediaDTO mediaDTO) {
        Media media = mediaService.updateMedia(mediaId, mediaDTO);
        return ResponseEntity.ok(MediaMapper.toMediaDTO(media));
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Integer mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }
}

