package com.example.CRUD.service;

import com.example.CRUD.entity.Users;
import com.example.CRUD.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UsersService {


    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public Flux<Users> getUsers(){
        return usersRepository.findAll();
    }

    public Mono<Users> getUsers(Integer id){
        return usersRepository.findById(id);
    }



    public Mono<String> save(Users users) {
        if (users.getEmail() != null && users.getEmail().contains("@")) {
            return usersRepository.save(users)
                    .map(savedUser -> "Usuario guardado");
        } else {
            // Considera manejar el caso en que el correo es nulo.
            return Mono.just("Correo inválido");
        }
    }



    public Mono<String> update(Integer id, Users newUsers) {
        return usersRepository.findById(id)
                .flatMap(existingUser -> {
                    if (newUsers.getEmail() != null && newUsers.getEmail().contains("@")) {
                        return saveUpdate(existingUser, newUsers)
                                .map(updatedUser -> "Usuario actualizado");
                    } else {
                        return Mono.just("Usuario no actualizado");
                    }
                })
                .switchIfEmpty(Mono.just("Usuario no encontrado"));
    }

    Mono<Users> saveUpdate(Users existingUsers, Users newUsers){

        if(newUsers.getFirstName()!=null){
            existingUsers.setFirstName(newUsers.getFirstName());
        }
        if (newUsers.getLastName()!=null){
            existingUsers.setLastName(newUsers.getLastName());
        }
        if(newUsers.getPhone()!=null){
            existingUsers.setPhone(newUsers.getPhone());
        }
        if(newUsers.getEmail()!=null){
            existingUsers.setEmail(newUsers.getEmail());
        }
        return usersRepository.save(existingUsers);
    }

    public Mono<String> delete(Integer id) {
        return usersRepository.findById(id)
                .flatMap(user -> usersRepository.deleteById(id)
                        .then(Mono.just("Usuario eliminado")))
                .switchIfEmpty(Mono.just("Usuario no encontrado"));
    }

}




