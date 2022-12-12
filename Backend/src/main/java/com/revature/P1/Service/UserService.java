package com.revature.P1.Service;

import com.revature.P1.DAO.UserDAO;
import com.revature.P1.Model.User;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class UserService {


    private User sessionUser = null;

    private UserDAO userDAO;

    private Logger logger = LogManager.getLogger();

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    public User addUser(User user){

        List<String> userNames = new ArrayList<>();
        boolean isUnique;


        if (userNames.contains(user.getUserName())){
            isUnique = false;
        } else {
            isUnique = true;
        }

        if (isUnique){
            return userDAO.create(user);
        } else {
            return null;
        }
    }

    public User getUser(User user){
        return null;
    }

    public void removeUser( String userName){

    }



    public User login(String userName, String password){
        //implement with dao

        return userDAO.loginCheck(userName,password);

    }
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }


    public void logout(){
        this.sessionUser = null;
    }

    public User getSessionUser(){
        return this.sessionUser;
    }
}