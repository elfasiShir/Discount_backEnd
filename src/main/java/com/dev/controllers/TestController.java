package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.DiscountObject;
import com.dev.objects.OrganizationObject;
import com.dev.objects.ShopObject;
import com.dev.objects.UserObject;
import com.dev.utils.MessagesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import java.util.*;

@RestController
public class TestController {
    List<Timer> timers = new ArrayList<>();


    @Autowired
    private Persist persist;
    @Autowired
    private final MessagesHandler messagesHandler = new MessagesHandler();


    @PostConstruct
    private void init() {
        List<DiscountObject> discounts = persist.getAllDiscounts();
        for (DiscountObject discount : discounts) {
            initializeTimersForDiscount(discount);
        }
    }

    @RequestMapping(value ="sign-up", method = RequestMethod.POST)
    public boolean signUp (String username, String password) {
        return persist.signUp(username, password);
    }

    @RequestMapping(value = "log-in" , method = RequestMethod.GET)
    public String logIn (String username , String password){
        return persist.logIn(username,password);
    }


    @RequestMapping(value ="doesUsernameTaken",  method = RequestMethod.GET)
    public boolean doesUsernameTaken(String username){
        return persist.doesUsernameTaken(username);
    }


    @RequestMapping(value ="get_organization_by_id" , method = RequestMethod.GET)
    public OrganizationObject getOrganizationById (int id){
        return persist.getOrganizationById( id);
    }

    @RequestMapping(value ="get_all_organizations" , method = RequestMethod.GET)
    public List<OrganizationObject> gatAllOrganizations(){
            return persist.getAllOrganizations();
    }

    @RequestMapping(value ="get_all_shops" , method = RequestMethod.GET)
    public List<ShopObject> getAllShops (){
            return persist.getAllShops ();
    }


    @RequestMapping(value ="add_user_to_organization", method = RequestMethod.POST)
    public void addUseToOrganization(String token, int organizationId){
        persist.addUserToOrganization(token, organizationId);
    }
    @RequestMapping(value ="delete_user_from_organization", method = RequestMethod.POST)
    public void deleteUserFromOrganization(String token, int organizationId){
        persist.deleteUserFromOrganization(token, organizationId);
    }
//    @RequestMapping(value ="add_discount_to_organization", method = RequestMethod.POST)
//    public void addDiscountToOrganization(int discountId, int organizationId){
//        persist.addDiscountToOrganization(discountId, organizationId);
//    }
    @RequestMapping(value ="get_all_organizations_for_user" , method = RequestMethod.GET)
    public List<OrganizationObject> gatAllOrganizationsForUser(String token) {
        return persist.getAllOrganizationsForUser(token);
    }
    @RequestMapping(value ="get_all_discounts_for_user" , method = RequestMethod.GET)
    public List<DiscountObject> gatAllDiscountsForUser(String token)  {
        return (List<DiscountObject>) persist.gatAllDiscountsForUser(token);
    }

//    @RequestMapping(value ="dose_user_belong_to_organization" , method = RequestMethod.GET)
//    public boolean doseUserBelongToOrganization (String token , int organizationId){
//        return persist.doseUserBelongToOrganization(token, organizationId);
//    }


    private void initializeTimersForDiscount(DiscountObject discount){
        long discountStartMilli = discount.getDiscountStart().toInstant().toEpochMilli();
        long discountEndMilli = discount.getDiscountEnd().toInstant().toEpochMilli();

        Timer startTimer = new Timer();
        Timer endTimer = new Timer();

        TimerTask startTask = new TimerTask() {
            @Override
            public void run() {
                messagesHandler.sendStartDiscount();
            }
        };
        TimerTask endTask = new TimerTask() {
            @Override
            public void run() {
                messagesHandler.sendEndDiscount();
            }
        };

        startTimer.schedule(startTask, new Date(discountStartMilli));
        endTimer.schedule(endTask, new Date(discountEndMilli));

        timers.add(startTimer);
        timers.add(endTimer);
    }
}
