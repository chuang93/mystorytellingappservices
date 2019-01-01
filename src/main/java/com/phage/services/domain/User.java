package com.phage.services.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class User {
    //Annotations required for primary key field. Autogenerate this value uniquely
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "token", nullable = false, length = 100)
    private String token;

    @Column (name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "email_address", nullable = false, length = 45)
    private String email;

    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token = token;
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
    public Timestamp getCreateTime(){
        return createTime;
    }
    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }
}
