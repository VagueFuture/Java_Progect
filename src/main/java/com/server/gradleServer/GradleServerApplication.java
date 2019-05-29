package com.server.gradleServer;

import com.server.gradleServer.chatController.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@SpringBootApplication
@RestController
public class GradleServerApplication {

	public static ArrayList<User> users = new ArrayList<>();
	public User lastUser;
	public static int lastId;

	public static void main(String[] args) {
		lastId = 0;
		SpringApplication.run(GradleServerApplication.class, args);
	}

	@RequestMapping(value="/getdata", method = RequestMethod.GET)
	public ResponseEntity<Object> getData(){
		return new ResponseEntity<>(lastUser.getName() ,HttpStatus.OK);
	}
	@RequestMapping(value="/postdata", method = RequestMethod.POST)
	public ResponseEntity<Object> postData(@RequestBody User user){
		lastId ++;
		user.setId(String.valueOf(lastId));
		users.add(user);
		lastUser = user;
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
}
