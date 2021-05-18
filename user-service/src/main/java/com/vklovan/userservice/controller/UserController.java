package com.vklovan.userservice.controller;

import com.vklovan.userservice.dto.UserDto;
import com.vklovan.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Flux<UserDto> getAll() {
        return userService.getUsers();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable int id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> createUser(@RequestBody Mono<UserDto> userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(
            @PathVariable int id,
            @RequestBody Mono<UserDto> userDto) {
        return userService
                .updateUser(id, userDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

}
