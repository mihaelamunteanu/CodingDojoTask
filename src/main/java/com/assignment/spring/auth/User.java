package com.assignment.spring.auth;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "app_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique=true)
    private String userName;
    private String password;
    private boolean active;
    private boolean lockedUser;
    private LocalDate startDate;
    private LocalDate expireDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    		  name = "user_role", 
    		  joinColumns = @JoinColumn(name = "user_fk"), 
    		  inverseJoinColumns = @JoinColumn(name = "role_fk"))
    private List<Role> roles;

}
