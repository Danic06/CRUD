package com.example.CRUD.service;

import com.example.CRUD.entity.Users;
import com.example.CRUD.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    public List<Users> getUsers(){
        return usersRepository.findAll();
    }

    public Optional <Users> getUsers(Integer id){
        return usersRepository.findById(id);
    }

    public String save(Users users){
        if(users.getEmail().contains("@")){
            usersRepository.save(users);
            return "Usuario guardado";

        }else{
            return "Correo invalido";
        }
    }

    public Optional<Users> update(Integer id, Users newUsers){
        Optional<Users> existingUsers= usersRepository.findById(id);
        if(existingUsers.isPresent()){
            if(newUsers.getEmail()!=null && newUsers.getEmail().contains("@")){
                return saveUpdate(existingUsers,newUsers);
            }else{
                return Optional.empty();
            }
        }else{
            return Optional.empty();
        }
    }
    private Optional<Users> saveUpdate(Optional<Users> existingUsers, Users newUsers ){
        Users updateUsers=existingUsers.get();

        if(newUsers.getFirstName()!=null){
            updateUsers.setFirstName(newUsers.getFirstName());
        }
        if (newUsers.getLastName()!=null){
            updateUsers.setLastName(newUsers.getLastName());
        }
        if(newUsers.getPhone()!=null){
            updateUsers.setPhone(newUsers.getPhone());
        }
        if(newUsers.getEmail()!=null){
            updateUsers.setEmail(newUsers.getEmail());
        }
        return Optional.of(usersRepository.save(updateUsers));
    }

    public void delete(Integer id){
        usersRepository.deleteById(id);
    }
}




