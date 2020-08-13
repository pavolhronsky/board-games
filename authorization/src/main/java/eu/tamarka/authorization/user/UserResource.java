package eu.tamarka.authorization.user;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1")
public class UserResource {

  private final Logger log = LogManager.getLogger(this.getClass());

  private UserService userService;

  @Autowired
  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(
      path = "/me",
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
  )
  public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
    log.info("getCurrentUser: method={}, uri={}, headers=[Authorization: {}, Content-Type: {}].", request.getMethod(), request.getRequestURI(),
        request.getHeader("Authorization"), request.getHeader("Content-Type"));
    return userService.getCurrentUser();
  }

  @GetMapping(
      path = "/me/force",
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
  )
  public ResponseEntity<User> forceUpdateUser(HttpServletRequest request) {
    log.info("forceUpdateUser: method={}, uri={}, headers=[Authorization: {}, Content-Type: {}].", request.getMethod(), request.getRequestURI(),
        request.getHeader("Authorization"), request.getHeader("Content-Type"));
    return userService.updateUser();
  }

  @PutMapping(
      path = "/me",
      consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
  )
  public ResponseEntity<User> updateNickname(@RequestBody User userWithNewNickname, HttpServletRequest request) {
    log.info("updateNickname: method={}, uri={}, headers=[Authorization: {}, Content-Type: {}, Accept: {}], body={}.", request.getMethod(),
        request.getRequestURI(), request.getHeader("Authorization"), request.getHeader("Content-Type"), request.getHeader("Accept"),
        userWithNewNickname);
    return userService.updateNickname(userWithNewNickname);
  }

  @DeleteMapping(
      path = "/me"
  )
  public ResponseEntity<User> deleteCurrentUser(HttpServletRequest request) {
    log.info("deleteCurrentUser: method={}, uri={}, headers=[Authorization: {}].", request.getMethod(), request.getRequestURI(),
        request.getHeader("Authorization"));
    return userService.deleteCurrentUser();
  }

  @GetMapping(
      path = "users",
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
  )
  ResponseEntity<Page<User>> findPage(Pageable pageable, HttpServletRequest request) {
    log.info("findPage: method={}, uri={}, headers=[Authorization: {}, Content-Type: {}], page={}, size={}.", request.getMethod(),
        request.getRequestURI(), request.getHeader("Authorization"), request.getHeader("Content-Type"), pageable.getPageNumber(),
        pageable.getPageSize());
    return userService.findPage(pageable);
  }

  @PutMapping(
      path = "/users/{id}",
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
  )
  ResponseEntity<User> verifyUser(@PathVariable String id, HttpServletRequest request) {
    log.info("verifyUser: method={}, uri={}, headers=[Authorization: {}, Content-Type: {}]", request.getMethod(),
        request.getRequestURI(), request.getHeader("Authorization"), request.getHeader("Content-Type"));
    return userService.verifyUser(id);
  }

  @DeleteMapping(
      path = "/users/{id}",
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
  )
  ResponseEntity<User> disproveUser(@PathVariable String id, HttpServletRequest request) {
    log.info("disproveUser: method={}, uri={}, headers=[Authorization: {}, Content-Type: {}]", request.getMethod(),
        request.getRequestURI(), request.getHeader("Authorization"), request.getHeader("Content-Type"));
    return userService.disproveUser(id);
  }
}