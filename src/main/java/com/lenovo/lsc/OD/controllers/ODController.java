package com.lenovo.lsc.OD.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ODController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String test() {
        return "Hello world!";
    }

}
