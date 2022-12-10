package com.revature.P1.Controller;

import com.revature.P1.DAO.ReimbursementDAO;
import com.revature.P1.DAO.UserDAO;
import com.revature.P1.Model.Reimbursement;
import com.revature.P1.Model.User;
import com.revature.P1.Service.UserService;
import com.revature.P1.Service.ReimbursementService;
import com.revature.P1.Util.Exceptions.InvalidTokenException;
import com.revature.P1.Util.Exceptions.InvalidUserInputException;
import com.revature.P1.Util.Tokens.JWTUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

public class ReimbursementController {
    ReimbursementService reimbursementService;
    UserService userService;
    JWTUtility jwtUtility;

    String noManager = "not a manager";

    Javalin app;
    public ReimbursementController(Javalin app, UserService userService, JWTUtility jwtUtility){
        this.userService = userService;
        this.reimbursementService = new ReimbursementService(new ReimbursementDAO());
        this.app = app;
        this.jwtUtility = jwtUtility;
    }


    public void reimbursementEndpoint(){

//      app.get("hello", this::helloHandler);
        app.post("reimbursement",this::postReimbursementHandler);
        app.get("reimbursementRequests",this::getAllReimbursements);
        app.get("reimbursement/{ticketId}",this::getSpecificReimbursement);
        app.post("idApproval/{ticketId}",this::managerApproval);
        app.post("idDenial/{ticketId}",this::managerDenial);
        app.get("getPending",this::getPendingRequests);
        app.get("personalReimbursements/{userName}", this::getOwnRequests);

    }
    private void getSpecificReimbursement(Context context) {
        if (isManager()){
            String id = context.pathParam("ticketId");
            Reimbursement reimbursement = reimbursementService.getReimbursement(Integer.parseInt(id));
            context.json(reimbursement);
        }else {
            context.json(noManager);
        }
    }
    private void managerApproval(Context context){
        if (isManager()){
            String id = context.pathParam("ticketId");
            reimbursementService.managerApproval(Integer.parseInt(id));
            Reimbursement reimbursement = reimbursementService.getReimbursement(Integer.parseInt(id));
            context.json(reimbursement);
        }else {
            context.json(noManager);
        }
    }
    private void managerDenial(Context context) {
        if (isManager()){
            String id = context.pathParam("ticketId");
            reimbursementService.managerDenial(Integer.parseInt(id));
            Reimbursement reimbursement = reimbursementService.getReimbursement(Integer.parseInt(id));
            context.json(reimbursement);
        }else {
            context.json(noManager);
        }
    }
    //
    private void getAllReimbursements(Context context) {
        // If user is a manager: send back all requests in DB
        // If user is not a manager: send back ONLY requests matching user's name
        // Add error checking for invalid tokens - MW
        if (isManager()){
            List<Reimbursement> allRequests = reimbursementService.getAllRequests();
            context.json(allRequests);
        }else {
            context.json(noManager);
        }
    }
    private void getPendingRequests(Context context) {
        if (isManager()){
            List<Reimbursement> allPendingRequests = reimbursementService.getPendingRequests();
            context.json(allPendingRequests);
        }else {
            context.json(noManager);
        }
    }

    private void postReimbursementHandler(Context context) throws JsonProcessingException {
        // Post Reimbursement System Updated, username taken from token,
        // Added Error Handling for invalid Tokens and Entries
        String token = context.header("Authorization");
        try{
            jwtUtility.isTokenValid(token);
            User user = jwtUtility.extractTokenDetails(token);
            String userName = user.getUserName();

            ObjectMapper mapper = new ObjectMapper();
            Reimbursement reimbursement = mapper.readValue(context.body(), Reimbursement.class);
            if(reimbursement.getDescription() == null || reimbursement.getDescription().trim() == "" ||
               reimbursement.getAmount() <= 0){
                throw new InvalidUserInputException("Please enter a valid dollar amount and description.");
            }
            reimbursement.setUserName(userName);
            reimbursementService.addRequest(reimbursement);
            context.json(reimbursement);
        } catch(InvalidTokenException e) {
            context.status(401);
            context.json(e.getMessage());
        } catch(InvalidUserInputException e){
            context.status(400);
            context.json(e.getMessage());
        }
    }


    private void getOwnRequests(Context context) {

        String name = context.pathParam("userName");

        context.json(reimbursementService.getReimbursement(name));


    }


    public boolean isManager(){
        if (this.userService.getSessionUser().getPosition().equals("manager")){
            return true;
        }else {
            return false;
        }

    }
    public boolean isCurrentUser(){
        if(this.userService.getSessionUser().getUserName().equals(reimbursementService.getUserName())){
            return true;
        }else{
            return false;
        }
    }

}