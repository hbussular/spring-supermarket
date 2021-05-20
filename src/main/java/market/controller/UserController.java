package market.controller;

import io.swagger.annotations.*;
import market.dto.model.auth.LoggedUserDto;
import market.dto.model.user.UserCreateDto;
import market.dto.model.user.UserReadDto;
import market.controller.request.UserSignupRequest;
import market.dto.response.Response;
import market.serviceImpl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

  @Autowired
  private UserServiceImpl userService;

  @Autowired
  private ModelMapper modelMapper;

  @PostMapping("/signin")
  @ApiOperation(value = "${UserController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
  public Response login(//
      @ApiParam("Username") @RequestParam String username, //
      @ApiParam("Password") @RequestParam String password) {
    return Response.ok().setPayload(userService.signin(username, password));
  }

  @PostMapping("/signup")
  @ApiOperation(value = "${UserController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 422, message = "Username (or E-mail) is already in use")})
  public Response signup(@ApiParam("Signup User") @RequestBody UserSignupRequest user) {
    UserCreateDto userDto = new UserCreateDto()
            .setEmail(user.getEmail())
            .setFullName(user.getFullName())
            .setPassword(user.getPassword())
            .setUsername(user.getUsername());

    UserReadDto newUser = userService.signup(userDto);

    return Response.ok().setPayload(newUser);
  }

  @DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    userService.delete(username);
    return username;
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_CLIENT')")
  @ApiOperation(value = "${UserController.me}", response = LoggedUserDto.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public LoggedUserDto whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), LoggedUserDto.class);
  }

  @GetMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_CLIENT')")
  public String refresh(HttpServletRequest req) {
    return userService.refresh(req.getRemoteUser());
  }
}
