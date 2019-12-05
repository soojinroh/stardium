package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerRequestDto;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private RoomService roomService;
    private PlayerService playerService;

    public RoomController(RoomService roomService, PlayerService playerService) {
        this.roomService = roomService;
        this.playerService = playerService;
    }

    @GetMapping
    public String mainRoomList(Model model) {
        List<RoomResponseDto> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        return "mainRooms";
    }

    @GetMapping("/createForm")
    public String createFrom() {
        return "createRoom";
    }

    @GetMapping("/updateForm")
    public String updateForm() {
        return "updateRoom";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity create(@RequestBody final RoomRequestDto roomRequest, final HttpSession session) {
        PlayerResponseDto loginPlayerDto = (PlayerResponseDto) session.getAttribute("login");
        Player loginPlayer = playerService.findByPlayerEmail(loginPlayerDto.getEmail());
        Long roomId = roomService.create(roomRequest, loginPlayer);
        return ResponseEntity.ok(roomId);
    }

    @GetMapping("/{roomId}")
    public String get(@PathVariable Long roomId, Model model) {
        Room room = roomService.findRoom(roomId);
        model.addAttribute("room", room);
        return "room";
    }

    @PutMapping("/{roomId}")
    @ResponseBody
    public ResponseEntity update(@PathVariable Long roomId, @RequestBody RoomRequestDto roomRequestDto, HttpSession httpSession) {
        PlayerResponseDto loginPlayerDto = (PlayerResponseDto) httpSession.getAttribute("login");
        Player player = playerService.findByPlayerEmail(loginPlayerDto.getEmail());
        Long updatedRoomId = roomService.update(roomId, roomRequestDto, player);
        return ResponseEntity.ok(updatedRoomId);
    }

    @DeleteMapping("/{roomId}")
    public String delete(@PathVariable Long roomId) {
        roomService.delete(roomId);
        return "redirect:/main";
    }

}
