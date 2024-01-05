package com.example.CRUD.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;


@Setter
@Getter
@Data
@Table("users")
public class Users implements Serializable {

    @Id
    private Integer Id;

    private String firstName;
    private String lastName;
    private Integer phone;
    private String email;



}
