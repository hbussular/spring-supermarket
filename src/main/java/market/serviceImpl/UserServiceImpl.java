package market.serviceImpl;

import market.dto.mapper.UserMapper;
import market.dto.model.user.UserCreateDto;
import market.dto.model.user.UserReadDto;
import market.exception.CustomException;
import market.model.user.Role;
import market.model.user.User;
import market.repository.UserRepository;
import market.security.JwtTokenProvider;
import market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  public String signin(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public UserReadDto signup(UserCreateDto userDto) {
    User user = userRepository.findByEmail(userDto.getEmail());

    if (user != null) {
      throw new CustomException("E-mail is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    user = userRepository.findByUsername(userDto.getUsername());

    if (user != null) {
      throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    user = new User()
            .setEmail(userDto.getEmail())
            .setPassword(passwordEncoder.encode(userDto.getPassword()))
            .setFullName(userDto.getFullName())
            .setUsername(userDto.getUsername())
            .setRoles(new ArrayList<>(Collections.singletonList(Role.ROLE_CLIENT)));

    userRepository.save(user);

    return UserMapper.toUserDto(user);
  }

  public void delete(String username) {
    userRepository.deleteByUsername(username);
  }

  public User search(String username) {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new CustomException("The user does not exist", HttpStatus.NOT_FOUND);
    }

    return user;
  }

  public User whoami(HttpServletRequest req) {
    return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
  }

  public String refresh(String username) {
    return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
  }

  public void addRole(Role role, Long id) {
    User user = userRepository.findById(id).orElse(null);

    if (user == null) {
      throw new CustomException("The user does not exist", HttpStatus.NOT_FOUND);
    }

    // Add the new role
    List<Role> currentRoles = user.getRoles();

    currentRoles.add(role);
    user.setRoles(currentRoles);

    // Update user
    userRepository.save(user);
  }
}
