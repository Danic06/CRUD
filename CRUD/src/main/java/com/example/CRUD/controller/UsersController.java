package com.example.CRUD.controller;

import com.example.CRUD.entity.Users;
import com.example.CRUD.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public Flux<Users> getAll() {
        return usersService.getUsers();
    }

    @GetMapping("/{id}")
    public Mono<Users> getById(@PathVariable Integer id) {
        return usersService.getUsers(id);
    }

    @PostMapping
    public Mono<String> save(@RequestBody Users users) {
        return usersService.save(users);
    }

    @PutMapping("/{id}")
    public Mono<String> update(@PathVariable("id") Integer id, @RequestBody Users newUsers) {
        return usersService.update(id, newUsers);
    }

    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable("id") Integer id) {
        return usersService.delete(id);
    }

}


