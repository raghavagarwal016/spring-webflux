package com.reactive.user.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.reactive.user.dto.UserDto;
import com.reactive.user.repository.UserRepository;
import com.reactive.user.service.UserService;
import com.reactive.user.util.ConverterUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Value("classpath:h2/init.sql")
  private Resource initSql;

  @Autowired
  private R2dbcEntityTemplate r2dbcEntityTemplate;

  @PostConstruct
  public void setup() throws IOException {
    String query = StreamUtils.copyToString(initSql.getInputStream(), StandardCharsets.UTF_8);
    r2dbcEntityTemplate.getDatabaseClient().sql(query).then().subscribe();
  }

  @Override
  public Flux<UserDto> getAllUsers() {
    return userRepository.findAll()
        .map(user -> ConverterUtil.toUserDtoFromUser(user));
  }

  @Override
  public Mono<UserDto> getUserById(Integer id) {
    return userRepository.findById(id)
        .map(user -> ConverterUtil.toUserDtoFromUser(user));
  }

  @Override
  public Mono<UserDto> saveUser(Mono<UserDto> userDtoMono) {
    return userDtoMono
        .flatMap(userDto -> userRepository.save(ConverterUtil.toUserFromUserDto(userDto)))
        .map(user -> ConverterUtil.toUserDtoFromUser(user));
  }

  @Override
  public Mono<UserDto> updateUser(Mono<UserDto> userDtoMono) {
    return userDtoMono
        .flatMap(userDto -> userRepository
            .findById(userDto.getId())
            .map(user -> {
              user.setBalance(userDto.getBalance());
              user.setName(userDto.getName());
              return user;
            })
        )
        .flatMap(user -> userRepository.save(user))
        .map(user -> ConverterUtil.toUserDtoFromUser(user));
  }

  @Override
  public Mono<Void> deleteUser(Integer id) {
    return userRepository.deleteById(id);
  }

}
