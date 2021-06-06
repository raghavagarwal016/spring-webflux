package com.reactive.user.service;

import com.reactive.user.dto.UserDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

  Flux<UserDto> getAllUsers();

  Mono<UserDto> getUserById(Integer id);

  Mono<UserDto> saveUser(Mono<UserDto> userDtoMono);

  Mono<UserDto> updateUser(Mono<UserDto> userDtoMono);

  Mono<Void> deleteUser(Integer id);
}
