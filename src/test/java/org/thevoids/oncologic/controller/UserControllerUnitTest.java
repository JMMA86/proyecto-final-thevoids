package org.thevoids.oncologic.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.controller.api.UserController;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        String identification1 = "1234567890";
        String name1 = "Juan Marin";
        
        User user1 = new User();
        user1.setIdentification(identification1);
        user1.setFullName(name1);

        String identification2 = "0987654321";
        String name2 = "Maria Perez";

        User user2 = new User();
        user2.setIdentification(identification2);
        user2.setFullName(name2);

        userList = Arrays.asList(user1, user2);
    }

    // @Test FIXME
    // void getAllUsers_WhenCalled_ReturnsAllUsers() {
    //     // Arrange
    //     when(userService.getAllUsers()).thenReturn(userList);

    //     // Act
    //     ResponseEntity<List<User>> response = userController.getAllUsers();

    //     // Assert
    //     assertEquals(200, response.getStatusCode().value());
    //     assertEquals(expectedResponse, response.getBody());
    // }
}