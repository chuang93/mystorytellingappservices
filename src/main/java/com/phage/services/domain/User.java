package com.phage.services.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class User {
    //Annotations required for primary key field. Autogenerate this value uniquely
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String last_name;

    private String first_name;

    private String email_address;

    private Timestamp created_time;

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public String getLast_name(){
        return last_name;
    }
    public void setLast_name(String last_name){
        this.last_name = last_name;
    }
    public String getFirst_name(){
        return first_name;
    }
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }
    public String getEmail_address(){
        return email_address;
    }
    public void setEmail_address(String email_address){
        this.email_address = email_address;
    }
    public Timestamp getCreate_Time(){
        return created_time;
    }
    public void setCreate_Time(Timestamp created_time)
    {
        this.created_time = created_time;
    }
}
