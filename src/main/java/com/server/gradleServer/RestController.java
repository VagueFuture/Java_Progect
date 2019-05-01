package com.server.gradleServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RestController {

    int two = 2;

    @RequestMapping("/1")
    @ResponseBody
   String test(){

        return "Hello";
    }

    int test2(){

        return two;
    }

}
