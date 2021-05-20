package market.service;

import market.dto.model.user.UserCreateDto;
import market.dto.model.user.UserReadDto;
import market.model.user.Role;
import market.model.user.User;
import market.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        UserCreateDto newAdminDto = new UserCreateDto();

        newAdminDto.setUsername("admin");
        newAdminDto.setPassword("admin");
        newAdminDto.setFullName("Administrator");
        newAdminDto.setEmail("admin@pet.com");

        UserReadDto adminDto = userService.signup(newAdminDto);
        userService.addRole(Role.ROLE_ADMIN, adminDto.getId());

        UserCreateDto newEmployee = new UserCreateDto();

        newEmployee.setUsername("employee");
        newEmployee.setPassword("employee");
        newEmployee.setFullName("Employee");
        newEmployee.setEmail("employee@pet.com");

        userService.signup(newEmployee);
    }

    @Test
    public void throwsExceptionWhenTheEmailIsNotUnique() {
        UserCreateDto userDto = new UserCreateDto();
        userDto.setEmail("admin@pet.com");

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userService.signup(userDto)
        );

        Assertions.assertTrue(thrown.getMessage().contains("E-mail is already in use"));
    }

    @Test
    public void throwsExceptionWhenTheUsernameIsNotUnique() {
        UserCreateDto userDto = new UserCreateDto();
        userDto.setEmail("admin-operator@pet.com");
        userDto.setUsername("admin");

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userService.signup(userDto)
        );

        Assertions.assertTrue(thrown.getMessage().contains("Username is already in use"));
    }

    @Test
    public void throwsExceptionWhenTheUsernameIsNotFound() {
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userService.search("anyUsername")
        );

        Assertions.assertTrue(thrown.getMessage().contains("The user does not exist"));
    }

    @Test
    public void shouldAddAdminPermissionToAnUser() {
        UserCreateDto newUser = new UserCreateDto();

        newUser.setUsername("henrique");
        newUser.setPassword("qwe123");
        newUser.setFullName("Henrique");
        newUser.setEmail("henrique@pet.com");

        UserReadDto newUserDto = userService.signup(newUser);

        userService.addRole(Role.ROLE_ADMIN, newUserDto.getId());
        Optional<User> newUpdatedUser = userRepository.findById(newUserDto.getId());

        Assertions.assertTrue(newUpdatedUser.get().getRoles().contains(Role.ROLE_ADMIN));
    }
}
