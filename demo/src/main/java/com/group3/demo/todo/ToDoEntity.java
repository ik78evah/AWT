package com.group3.demo.todo;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoEntity {


    private @Id @GeneratedValue Long id;

    private String title;
    private Boolean isCompleted;
    private Integer userid;

}
