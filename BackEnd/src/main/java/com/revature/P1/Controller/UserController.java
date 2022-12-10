package com.revature.P1.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.P1.DAO.UserDAO;
import com.revature.P1.Model.User;
import com.revature.P1.Service.UserService;
import com.revature.P1.Util.DTO.LoginCreds;
import com.revature.P1.Util.Exceptions.InvalidUserInputException;
import com.revature.P1.Util.Tokens.JWTUtility;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class UserController {

    UserService userService;
    Javalin app;
    JWTUtility jwtUtility; // Object for all token functions - MW
    public UserController(UserService userService){
        this.userService = userService;
    }

    public UserController(Javalin app, UserService userService, JWTUtility jwtUtility) {
        this.userService = userService;
        this.app = app;
        this.jwtUtility = jwtUtility; // Updated to take injected JWT object from Application - MW
    }



    public void userEndpoint(){
        app.get("hello", this::helloHandler);
        app.post("register", this::postUserHandler);
        app.post("login", this::loginHandler);
        app.delete("logout", this::logoutHandler);
        app.get("user", this::listUserHandler); // replaced endpoint to match allUsers.js - MW
    }
    private void helloHandler(Context context) {

        context.result("Welcome to the Galaxy!");
    }
    private void postUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(context.body(), User.class);
        user = userService.addUser(user);
        if (user == null) {
            context.status(409); // Code for JS to catch if username is already registered - MW
            context.json("Your User Name is already registered.");
        } else {
            context.json(user);
        }
    }
    private void loginHandler(Context context) throws JsonProcessingException {
        // Updated userService.login to get user object from DAO, JWTs working as well.
        // New Try/Catch block to properly handle Exceptions from other layers. - MW
        ObjectMapper mapper = new ObjectMapper();
        LoginCreds loginCreds = mapper.readValue(context.body(), LoginCreds.class);
        try {
            User user = userService.login(loginCreds.getUserName(), loginCreds.getPassword());
            String token = jwtUtility.createToken(user);
            context.header("Authorization", token);
            context.json("Successfully logged in!");
        } catch (InvalidUserInputException e){
            context.status(401);
            context.json(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            context.status(500);
            context.json("Developer action needed, please contact an administrator. Sorry for the inconvenience.");
        }
    }
    private void logoutHandler(Context context) {
        String userName = userService.getSessionUser().getUserName();
        userService.logout();
        context.json(userName + " has now logged out");
    }

    private void listUserHandler(Context context) {
        // Created this handler for "users" endpoint, token utility also included - MW
        String auth = context.header("Authorization");
        User authUser = jwtUtility.extractTokenDetails(auth);
        if(!authUser.getPosition().equals("manager")){
            context.status(403);
            context.json("Unauthorized Request - Not a manager");
        } else {
            List<User> allUsers = userService.getUser();
            context.header("RespMessage", "Successfully retrieved all users.");
            context.json(allUsers);
        }
    }


}
