package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.model.Role;
import com.java2.ticketingsystembackend.model.User;
import com.java2.ticketingsystembackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        // Kiểm tra xem tên đăng nhập hoặc email đã tồn tại chưa
        if (userService.userExists(user.getUsername(), user.getEmail())) {
            return ResponseEntity.badRequest().body("Username or Email already exists");
        }

        // Thiết lập vai trò cho user (Role mặc định là 'USER')
        user.setRole(new Role());
        user.getRole().setId(1); // Giả sử Role 'USER' có id là 1

        // Lưu thông tin người dùng vào cơ sở dữ liệu
        userService.saveUser(user);

        // Trả về phản hồi thành công
        return ResponseEntity.ok("User registered successfully");
    }
}