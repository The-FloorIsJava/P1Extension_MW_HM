package com.revature.P1.Util;

import com.revature.P1.Controller.ReimbursementController;
import com.revature.P1.Controller.UserController;
import com.revature.P1.DAO.UserDAO;
import com.revature.P1.Service.UserService;
import com.revature.P1.Util.Token.JWTUtility;
import io.javalin.Javalin;

public class Application {
    public static void main(String[] args) {

        UserService userService = new UserService(new UserDAO());


        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors ->{
                cors.add(it -> {
                    it.anyHost();
                    it.exposeHeader("Authorization");
                });
            });
        }).start(8081);


        JWTUtility jwtUtility = new JWTUtility();
        new UserController(app, userService, jwtUtility).userEndpoint();
        new ReimbursementController(app, userService, jwtUtility).reimbursementEndpoint();
      //  MenuItemController menuItemController = new MenuItemController();

      //  menuItemController.menuItemEndpoint(app);
    }
}
