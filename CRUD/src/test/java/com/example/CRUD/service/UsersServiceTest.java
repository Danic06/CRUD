package com.example.CRUD.service;

import com.example.CRUD.entity.Users;
import com.example.CRUD.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsersServiceTest {
    @Autowired
    private UsersService usersService;
    @MockBean
    private UsersRepository usersRepository;
    @Test
    void getUsers() {
     // Inicialización de Datos de Prueba
        ArrayList<Users> usersTest = new ArrayList<Users>();

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
        when(usersRepository.findAll()).thenReturn(usersTest);

    // Llamada al Método del Servicio
        List<Users> result = usersService.getUsers();

    // Verificación de Resultados
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("test", usersTest.get(3).getFirstName());
        assertEquals("test", usersTest.get(3).getLastName());
        assertEquals(1234, usersTest.get(3).getPhone());
        assertEquals("d@mail.com", usersTest.get(3).getEmail());
    }

    @Test
    void testGetUsers() {
        //Inicializando los datos de prueba
        List<Users> usersTes = new ArrayList<>();

        // Agregando usuarios de prueba a la lista
        usersTes.add(new Users());
        usersTes.add(new Users());
        usersTes.add(new Users());

        // Agregando un usuario de prueba especifico
        Users userTes = new Users();
        userTes.setId(1); // ID especifico para simular busqueda por ID
        userTes.setFirstName("test");
        userTes.setLastName("test");
        userTes.setPhone(1234);
        userTes.setEmail("d@mail.com");
        usersTes.add(userTes);

        // Simular el comportamiento del repositorio cuando se llama a findById
        when(usersRepository.findById(1)).thenReturn(Optional.of(userTes));

        // Llamar al método de servicio para obtener un usuario por ID
        Optional<Users> result = usersService.getUsers(1);

        // Verificar el resultado
        assertTrue(result.isPresent());
        Users usersId = result.get();
        assertEquals(1, usersId .getId());
        assertEquals("test", usersId .getFirstName());
        assertEquals("test", usersId .getLastName());
        assertEquals(1234, usersId .getPhone());
        assertEquals("d@mail.com", usersId .getEmail());
    }

    @Test
    void save() {
        Users userCreate = new Users();
        userCreate.setFirstName("Dani");
        userCreate.setLastName("cardona");
        userCreate.setPhone(314654321);
        userCreate.setEmail("dani@mail.com");

        // Simular el comportamiento del repositorio cuando se llama a save
        when(usersRepository.save(userCreate)).thenReturn(userCreate);

        // Llamar al método de servicio para crear un usuario
        String resultMessage = usersService.save(userCreate);

        // Verificar el resultado
        assertEquals("Usuario guardado", resultMessage);

        Users InvalidEmail = new Users();
        InvalidEmail.setFirstName("Invalid");
        InvalidEmail.setLastName("Email");
        InvalidEmail.setPhone(314654321);
        InvalidEmail.setEmail("invalid-email");

        // Llamar al método de servicio para crear un usuario con correo no válido
        String resultMessageInvalidEmail = usersService.save(InvalidEmail);

        // Verificar el resultado para el correo no válido
        assertEquals("Correo invalido", resultMessageInvalidEmail);
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
        when(usersRepository.findById(1)).thenReturn(Optional.of(existingUser));

        // Crear un objeto Users con los datos actualizados (correo válido)
        Users updatedUserValidEmail = new Users();
        updatedUserValidEmail.setId(1); // Asignar el mismo ID para simular la misma entidad
        updatedUserValidEmail.setFirstName("updatedFirstName");
        updatedUserValidEmail.setLastName("updatedLastName");
        updatedUserValidEmail.setPhone(987654321);
        updatedUserValidEmail.setEmail("updated.email@example.com");

        // Configurar ArgumentCaptor para capturar el objeto que se pasa a save
        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);

        // Simular el comportamiento del repositorio cuando se llama a save
        when(usersRepository.save(userCaptor.capture())).thenReturn(updatedUserValidEmail);

        // Llamar al método de servicio para actualizar el usuario con correo válido
        Optional<Users> resultValidEmail = usersService.update(1, updatedUserValidEmail);

        // Verificar el resultado para el correo válido
        assertTrue(resultValidEmail.isPresent());
        assertEquals("updated.email@example.com", resultValidEmail.get().getEmail());

        Users updatedUserInvalidEmail = new Users();
        updatedUserInvalidEmail.setEmail("invalid-email");  // Correo no válido

        // Llamar al método de servicio para actualizar el usuario con correo no válido
        Optional<Users> resultInvalidEmail = usersService.update(1, updatedUserInvalidEmail);

        // Verificar el resultado para el correo no válido
        assertFalse(resultInvalidEmail.isPresent());


        // Llamar al método de servicio para actualizar un usuario que no existe
        Optional<Users> resultUserNotFound = usersService.update(999, updatedUserInvalidEmail);

        // Verificar el resultado cuando el usuario no existe
        assertFalse(resultUserNotFound.isPresent());

        // Verificar que se llamó al método save con el objeto correcto
        Users capturedUser = userCaptor.getValue();
        assertNotNull(capturedUser);
        assertEquals("updated.email@example.com", capturedUser.getEmail());
        assertEquals("updatedFirstName", capturedUser.getFirstName());
        assertEquals("updatedLastName", capturedUser.getLastName());
        assertEquals(987654321, capturedUser.getPhone());
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
        when(usersRepository.findById(1)).thenReturn(Optional.of(existingUser));

        // Simular el comportamiento del repositorio cuando se llama a deleteById
        doNothing().when(usersRepository).deleteById(1);

        // Llamar al método de servicio para eliminar un usuario por ID
        assertDoesNotThrow(() -> usersService.delete(1));

        // Verificar que el método del repositorio se haya llamado correctamente
        verify(usersRepository).deleteById(1);
    }
}