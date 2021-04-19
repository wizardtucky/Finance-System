package fms.webControllers;


import fms.model.User;
import fms.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(path = "/users")
public class UsersController {

    @Autowired
    private UsersRepository userRepository;

    @GetMapping(path="/allusers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("/get-by-id/{id}")
    public @ResponseBody User getById(@PathVariable("id") int id) {
        Optional<User> item = userRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        throw new Error("Not found");
    }

    @PutMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createItem(@RequestBody User item) {
        item.setId(-1); // Duomenu bazeje negali buti neigiamos 'id', todel ji automatiskai parinks tinkama id
        userRepository.save(item);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable int id) {
        Optional<User> item = userRepository.findById(id);
        if (item.isPresent()) {
            userRepository.delete(item.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object login(@RequestBody UserData userData) {
        List<User> users = userRepository.findByCredentials(userData.username, userData.password);

        if (users.size() == 0) {
            throw new Error();
        }

        return users.get(0);
    }

    static private class UserData {
        public String username;
        public String password;
    }
}