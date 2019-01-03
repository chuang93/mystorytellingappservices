package com.phage.services.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class LocalUser {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name="username", nullable = false, length = 45)
    private String username;

    @Column(name="password", nullable = false, length =45)
    private String password;

    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getUserName(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public Timestamp getCreateTime(){
        return createTime;
    }
    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }
}
