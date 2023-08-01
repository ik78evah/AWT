package com.group3.demo.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoEntityRepository extends JpaRepository<ToDoEntity, Long> {
    List<ToDoEntity> findAllByUserid(Integer userid);
}
