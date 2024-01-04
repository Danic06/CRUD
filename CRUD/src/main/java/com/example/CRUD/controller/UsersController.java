package com.example.CRUD.controller;

import com.example.CRUD.entity.Users;
import com.example.CRUD.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/users")
public class UsersController {

    @Autowired
     private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<Users> getAll(){
        return usersService.getUsers();
    }

    @GetMapping("/{id}")
    public Optional<Users> getById(@PathVariable("id")Integer id){
        return usersService.getUsers(id);
    }

    @PostMapping
    public String save(@RequestBody Users users){
        return usersService.save(users);
    }

    @PutMapping("/{id}")
    public Optional<Users> update(@PathVariable("id")Integer id, @RequestBody Users newUsers){
        return usersService.update(id, newUsers);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id")Integer id){
        usersService.delete(id);
    }


}
