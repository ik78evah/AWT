package com.group3.demo.todo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/demo")
public class ToDoController {

    private final ToDoEntityRepository toDoEntityRepository;

    ToDoController(ToDoEntityRepository toDoEntityRepository){
        this.toDoEntityRepository = toDoEntityRepository;
    }

    @RequestMapping("/")
    public String all(){
        return "Demo Backend is up and running";
    }

    @GetMapping("/all")
    List<ToDoEntity> all(@RequestHeader("UserId") String userId){
        return toDoEntityRepository.findAllByUserid(Integer.valueOf(userId));
    }

    @GetMapping("/allCompleted")
    List<ToDoEntity> getAllCompleted(@RequestHeader("UserId") String userId){
        return toDoEntityRepository.findAllByUserid(Integer.valueOf(userId)).stream().filter(todo -> {
            if (todo.getIsCompleted()){
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
    }

    @PostMapping("/setCompleted/{id}")
    ToDoEntity setCompleted(@RequestHeader("UserId") String userId,@PathVariable("id") Long id){
        ToDoEntity tmp = this.toDoEntityRepository.getById(id);
        if(tmp.getUserid().equals(Integer.valueOf(userId))){
            tmp.setIsCompleted(true);
            return this.toDoEntityRepository.save(tmp);
        }else return null;

    }

    @PostMapping("/add/{name}")
    ToDoEntity newToDo(@RequestHeader("UserId") String userId,@PathVariable("name") String title){
        ToDoEntity newEntity = ToDoEntity.builder()
                .userid(Integer.valueOf(userId))
                .title(title)
                .isCompleted(false)
                .build();
        return toDoEntityRepository.save(newEntity);
    }

    @DeleteMapping("delete/{id}")
    void deleteToDo(@RequestHeader("UserId") String userId,@PathVariable("id")Long id){
        ToDoEntity tmp = toDoEntityRepository.getById(id);
        if(tmp.getUserid().equals(Integer.valueOf(userId))){
            toDoEntityRepository.delete(toDoEntityRepository.getById(id));
        }
    }
}
