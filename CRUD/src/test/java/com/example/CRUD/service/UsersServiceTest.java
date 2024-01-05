package com.example.CRUD.service;

import com.example.CRUD.entity.Users;
import com.example.CRUD.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@SpringBootTest
class UsersServiceTest {
    @Autowired
    private UsersService usersService;
    @MockBean
    private UsersRepository usersRepository;


    @Test
    void getUsers() {
        // Inicialización de Datos de Prueba
        ArrayList<Users> usersTest = new ArrayList<>();

        // Agregando Usuarios de Prueba a la Lista
        usersTest.add(new Users());
        usersTest.add(new Users());
        usersTest.add(new Users());

        // Agregando un Usuario de Prueba Específico
        Users userTest = new Users();
        userTest.setFirstName("test");
        userTest.setLastName("test");
        userTest.setPhone(1234);
        userTest.setEmail("d@mail.com");
        usersTest.add(userTest);

        // Configuración de Comportamiento Mock
        when(usersRepository.findAll()).thenReturn(Flux.fromIterable(usersTest));

        // Llamada al Método del Servicio
        Flux<Users> result = usersService.getUsers();

        // Verificación de Resultados con StepVerifier
        StepVerifier.create(result)
                .expectNextCount(3) // Espera 3 usuarios sin verificar sus detalles
                .assertNext(user -> {
                    assertEquals("test", user.getFirstName());
                    assertEquals("test", user.getLastName());
                    assertEquals(1234, user.getPhone());
                    assertEquals("d@mail.com", user.getEmail());
                })
                .verifyComplete();
    }

    @Test
    void testGetUsers() {
        // Inicializando los datos de prueba
        List<Users> usersTest = new ArrayList<>();

        // Agregando usuarios de prueba a la lista
        usersTest.add(new Users());
        usersTest.add(new Users());
        usersTest.add(new Users());

        // Agregando un usuario de prueba especifico
        Users userTest = new Users();
        userTest.setId(1); // ID especifico para simular busqueda por ID
        userTest.setFirstName("test");
        userTest.setLastName("test");
        userTest.setPhone(1234);
        userTest.setEmail("d@mail.com");
        usersTest.add(userTest);

        // Configuración de comportamiento mock para el repositorio
        when(usersRepository.findById(1)).thenReturn(Mono.just(userTest));

        // Llamada al método del servicio para obtener un usuario por ID
        Mono<Users> result = usersService.getUsers(1);

        // Verificación de resultados con StepVerifier
        StepVerifier.create(result)
                .expectNextMatches(usersId -> {
                    assertEquals(1, usersId.getId());
                    assertEquals("test", usersId.getFirstName());
                    assertEquals("test", usersId.getLastName());
                    assertEquals(1234, usersId.getPhone());
                    assertEquals("d@mail.com", usersId.getEmail());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void save() {
        Users userCreate = new Users();
        userCreate.setFirstName("Dani");
        userCreate.setLastName("cardona");
        userCreate.setPhone(314654321);
        userCreate.setEmail("dani@mail.com");

        // Simular el comportamiento del repositorio cuando se llama a save
        when(usersRepository.save(userCreate)).thenReturn(Mono.just(userCreate));

        // Llamar al método de servicio para crear un usuario
        Mono<String> resultMono = usersService.save(userCreate);

        // Verificar el resultado usando StepVerifier
        StepVerifier.create(resultMono)
                .expectNext("Usuario guardado")
                .verifyComplete();

        // Verificar que el método del repositorio se haya llamado correctamente


        Users invalidEmailUser = new Users();
        invalidEmailUser.setFirstName("Invalid");
        invalidEmailUser.setLastName("Email");
        invalidEmailUser.setPhone(123456789);
        invalidEmailUser.setEmail("invalid-email");

        // Llamar al método de servicio para crear un usuario con correo no válido
        Mono<String> resultInvalidEmailMono = usersService.save(invalidEmailUser);

        // Verificar el resultado para el correo no válido usando StepVerifier
        StepVerifier.create(resultInvalidEmailMono)
                .expectNext("Correo inválido")
                .verifyComplete();

    }



    @Test
    void update() {
        // Crear un usuario existente con correo válido
        Users existingUser = new Users();
        existingUser.setId(1);
        existingUser.setFirstName("jul");
        existingUser.setLastName("perez");
        existingUser.setPhone(123456789);
        existingUser.setEmail("jul.p@example.com");

        // Simular el comportamiento del repositorio cuando se llama a findById
        UsersRepository usersRepository = mock(UsersRepository.class);
        when(usersRepository.findById(1)).thenReturn(Mono.just(existingUser));

        // Crear un objeto Users con los datos actualizados (correo válido)
        Users updatedUserValidEmail = new Users();
        updatedUserValidEmail.setId(1);
        updatedUserValidEmail.setFirstName("updatedFirstName");
        updatedUserValidEmail.setLastName("updatedLastName");
        updatedUserValidEmail.setPhone(987654321);
        updatedUserValidEmail.setEmail("updated.email@example.com");

        // Simular el comportamiento del repositorio cuando se llama a save
        when(usersRepository.save(Mockito.<Users>any())).thenAnswer(invocation -> {
            Users savedUser = invocation.getArgument(0);
            return Mono.just(savedUser);
        });


        // Crear una instancia de UsersService con el mock de UsersRepository
        UsersService usersService = new UsersService(usersRepository);

        // Llamar al método de servicio para actualizar el usuario con correo válido
        Mono<String> resultValidEmailMono = usersService.update(1, updatedUserValidEmail);

        // Verificar el resultado para el correo válido usando StepVerifier
        StepVerifier.create(resultValidEmailMono)
                .expectNext("Usuario actualizado")
                .verifyComplete();



        // Crear un objeto Users con un correo no válido
        Users updatedUserInvalidEmail = new Users();
        updatedUserInvalidEmail.setId(1);
        updatedUserInvalidEmail.setFirstName("updatedFirstName");
        updatedUserInvalidEmail.setLastName("updatedLastName");
        updatedUserInvalidEmail.setPhone(987654321);
        updatedUserInvalidEmail.setEmail("invalid-email");

        // Crear una instancia de UsersService con el mock de UsersRepository

        // Llamar al método de servicio para actualizar el usuario con correo no válido
        Mono<String> resultInvalidEmailMono = usersService.update(1, updatedUserInvalidEmail);

        // Verificar el resultado para el correo no válido usando StepVerifier
        StepVerifier.create(resultInvalidEmailMono)
                .expectNext("Usuario no actualizado")
                .verifyComplete();
    }


    @Test
    void delete() {
        Users existingUser = new Users();
        existingUser.setId(1);
        existingUser.setFirstName("Dani");
        existingUser.setLastName("cardona");
        existingUser.setPhone(123456789);
        existingUser.setEmail("dani@mail.com");

        // Simular el comportamiento del repositorio cuando se llama a findById
        when(usersRepository.findById(1)).thenReturn(Mono.just(existingUser));

        // Simular el comportamiento del repositorio cuando se llama a deleteById
        when(usersRepository.deleteById(1)).thenReturn(Mono.empty());

        // Llamar al método de servicio para eliminar un usuario por ID
        usersService.delete(1).block(); // Bloquear para esperar la operación asincrónica

        // Verificar que el método del repositorio se haya llamado correctamente
        verify(usersRepository).deleteById(1);
    }
}