package com.reactive.user.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.reactive.user.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

  @Modifying
  @Query("update users " +
      "set balance = balance - ?2 " +
      "where id = ?1 " +
      "and balance >= ?2")
  Mono<Boolean> updateUserBalance(int userId, int amount);

}
