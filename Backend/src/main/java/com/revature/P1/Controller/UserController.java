package com.revature.P1.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.P1.DAO.UserDAO;
import com.revature.P1.Model.User;
import com.revature.P1.Service.UserService;
import com.revature.P1.Util.DTO.LoginCreds;
import com.revature.P1.Util.Exceptions.InvalidUserInputException;
import com.revature.P1.Util.Token.JWTUtility;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;

public class UserController {

    UserService userService;
    Javalin app;

    JWTUtility jwtUtility;

    private Logger logger = LogManager.getLogger();
    public UserController(UserService userService){
        this.userService = userService;
    }

    public UserController(Javalin app, UserService userService, JWTUtility jwtUtility) {
        this.userService = userService;
        this.app = app;
        this.jwtUtility = jwtUtility;
    }



    public void userEndpoint(){
        app.get("hello", this::helloHandler);
        app.post("register", this::postUserHandler);
        app.post("login", this::loginHandler);
        app.delete("logout", this::logoutHandler);
        app.get("user", this::getAllUsersHandler);

    }
    private void helloHandler(Context context) {

        context.result("Welcome to the Galaxy!");
    }
    private void postUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(context.body(), User.class);
        user = userService.addUser(user);
        if (user == null) {
            context.json("Your User Name is already registered.");
        }else {
            context.json(user);
        }
    }
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LoginCreds loginCreds = mapper.readValue(context.body(), LoginCreds.class);
        try {
            User user = userService.login(loginCreds.getUserName(), loginCreds.getPassword());
            String token = jwtUtility.createToken(user);
            context.header("Authorization", token);
            context.json("Successfully logged in");

        } catch (InvalidUserInputException e){
            context.status(404);
            context.json(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            context.status(500);
            context.json("The developers need to fix something, apologies for any inconvience");
        }

    }
    private void logoutHandler(Context context) {
        String userName = userService.getSessionUser().getUserName();
        userService.logout();
        context.json(userName + " has now logged out");
    }

    private void getAllUsersHandler(Context context) {
        String auth = context.header("Authorization");
        User authUser = jwtUtility.extractTokenDetails(auth);
        logger.info("{} attempted to hit the getAllUsersHandler", authUser);
        if(!String.valueOf(authUser.getPosition()).equals("manager")){
            context.status(403);
            context.json("Unauthorized Request - change position to manager");
        } else {
            List<User> allUsers = userService.getAllUsers();
//        similar as context.result, but the content type is json rather than text.
            context.header("RespMessage", "Successfully showing all users");
            context.json(allUsers);
        }
    }

}
