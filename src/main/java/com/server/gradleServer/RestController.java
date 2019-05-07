package com.server.gradleServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RestController {

   private int two = 2;

    @RequestMapping("/2")
    @ResponseBody
   String test(){
        return "Hello";
    }

}
