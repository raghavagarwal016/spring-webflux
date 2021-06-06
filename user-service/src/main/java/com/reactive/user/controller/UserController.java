package com.reactive.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.user.dto.UserDto;
import com.reactive.user.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Flux<UserDto>> getAllUsers() {
    return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable("id") Integer id) {
    return userService.getUserById(id)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping(value = "/save")
  public Mono<ResponseEntity<UserDto>> saveUser(@RequestBody Mono<UserDto> userDtoMono) {
    return userService.saveUser(userDtoMono)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK));
  }

  @PutMapping(value = "/update")
  public Mono<ResponseEntity<UserDto>> updateUser(@RequestBody Mono<UserDto> userDtoMono) {
    return userService.updateUser(userDtoMono)
        .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK));
  }

  @DeleteMapping(value = "/delete/{id}")
  public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") Integer id) {
    return userService.deleteUser(id)
        .map(v -> new ResponseEntity<>(v, HttpStatus.OK));
  }

}
