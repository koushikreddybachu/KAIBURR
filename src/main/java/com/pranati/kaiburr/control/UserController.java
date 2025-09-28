package com.pranati.kaiburr.control;


import com.pranati.kaiburr.model.Task;
import com.pranati.kaiburr.service.UserService;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/Tasks")
    public List<Document> getTasks(@RequestParam(required = false) String id) {
        System.out.println("Inside getTasks");
        return userService.getTasks(id);
    }
    @PostMapping("/create")
    public String addTask(@RequestBody Task task){
        userService.addTask(task);
        return "Sucessfully create Task";
    }
    @DeleteMapping("/Tasks")
    public String deleteTask(@RequestParam String id){

        return userService.deleteTask(id);
    }
}
