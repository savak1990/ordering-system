package com.vklovan.userservice.service;

import com.vklovan.userservice.dto.UserDto;
import com.vklovan.userservice.repository.UserRepository;
import com.vklovan.userservice.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Flux<UserDto> getUsers() {
        return userRepository.findAll()
                .map(userMapper::toDto);
    }

    public Mono<UserDto> getUserById(final int userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto);
    }

    public Mono<UserDto> createUser(Mono<UserDto> userDtoMono) {
        return userDtoMono
                .map(userMapper::toEntity)
                .flatMap(userRepository::save)
                .map(userMapper::toDto);
    }

    public Mono<UserDto> updateUser(int id, Mono<UserDto> userDtoMono) {
        return userRepository.findById(id)
                .flatMap(u -> userDtoMono
                        .map(userMapper::toEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(userRepository::save)
                .map(userMapper::toDto);
    }

    public Mono<Void> deleteUser(int id) {
        return userRepository.deleteById(id);
    }

}
