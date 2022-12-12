package com.revature.P1.Controller;

import com.revature.P1.DAO.ReimbursementDAO;
import com.revature.P1.Model.Reimbursement;
import com.revature.P1.Model.User;
import com.revature.P1.Service.UserService;
import com.revature.P1.Service.ReimbursementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.P1.Util.Exceptions.InvalidTokenException;
import com.revature.P1.Util.Exceptions.InvalidUserInputException;
import com.revature.P1.Util.Token.JWTUtility;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

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
        app.get("reimbursements",this::getAllReimbursements);
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
        ObjectMapper mapper = new ObjectMapper();
        Reimbursement reimbursement = mapper.readValue(context.body(), Reimbursement.class);
        reimbursementService.addRequest(reimbursement);
        context.json(reimbursement);
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