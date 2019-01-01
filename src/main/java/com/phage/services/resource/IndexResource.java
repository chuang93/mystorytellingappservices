package com.phage.services.resource;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//for use of the '/' base index URL of the services
@RestController
public class IndexResource {

    @RequestMapping( value = "/", method = RequestMethod.GET)
    public String Index(){
        return "index resource";
    }
}
