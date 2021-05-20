package market.service;

import market.dto.model.user.UserCreateDto;
import market.dto.model.user.UserReadDto;
import market.model.user.Role;
import market.model.user.User;
import javax.servlet.http.HttpServletRequest;


public interface UserService {
    UserReadDto signup(UserCreateDto entry);
    String signin(String username, String password);
    User search(String username);
    User whoami(HttpServletRequest req);
    String refresh(String username);
    void addRole(Role role, Long id);
}