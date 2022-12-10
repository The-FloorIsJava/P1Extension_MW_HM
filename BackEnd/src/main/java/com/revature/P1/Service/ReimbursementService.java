package com.revature.P1.Service;

import com.revature.P1.DAO.ReimbursementDAO;
import com.revature.P1.Model.User;
import com.revature.P1.Model.Reimbursement;

import java.util.List;

public class ReimbursementService extends User {



    private final ReimbursementDAO reimbursementDAO;

    public ReimbursementService(ReimbursementDAO reimbursementDAO){
        this.reimbursementDAO = reimbursementDAO;
    }
    User myUser;

    List<Reimbursement> reimbursementService;

    public void addRequest(Reimbursement reimbursement){
        reimbursementDAO.create(reimbursement);
    }


    public Reimbursement getReimbursement(int id){

        return reimbursementDAO.findById(id);
    }
    public Reimbursement getReimbursement(String name){

        return reimbursementDAO.findPersonalRequests(name);
    }

    public Reimbursement managerApproval(int id) {
        return reimbursementDAO.approve(id);
    }

    public Reimbursement managerDenial(int id) {
        return reimbursementDAO.deny(id);
    }

    public List<Reimbursement> getAllRequests(){

        return reimbursementDAO.findAll();
    }

    public List<Reimbursement> getPendingRequests(){
        return reimbursementDAO.findAllPending();
    }



}