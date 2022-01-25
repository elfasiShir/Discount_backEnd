package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.DiscountObject;
import com.dev.objects.OrganizationObject;
import com.dev.objects.ShopObject;
import com.dev.objects.UserObject;
import com.dev.utils.MessagesHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@RestController
public class TestController {
    private final MessagesHandler messagesHandler;
    @Autowired
    private Persist persist;


    public TestController(MessagesHandler messagesHandler) {
        this.messagesHandler = messagesHandler;
    }

    @PostConstruct
    private void init () {
    }

    //הרשמה
    @RequestMapping(value ="sign-up", method = RequestMethod.POST)
    public boolean signUn (@RequestParam String username, String password) {
        return persist.signUp(username, password);
    }
    //להתחבר
    @RequestMapping(value = "log-in" , method = RequestMethod.GET)
    public String logIn (String username , String password){
        return persist.logIn(username,password);
    }


    @RequestMapping(value ="doesUsernameTaken",  method = RequestMethod.GET)
    public boolean doesUsernameExists(String username){
        return persist.doesUsernameTaken(username);
    }

    @RequestMapping(value ="countDownTries", method = RequestMethod.POST)
    public void countDownTries(String username){
        persist.countDownTries(username);
    }

    @RequestMapping(value ="isBlocked" , method = RequestMethod.GET)
    public int isBlocked(String username){
        return persist.isBlocked(username);
    }

    @RequestMapping(value ="updateLoginTries", method = RequestMethod.POST)
    public void updateLoginTries(String username){
        persist.updateLoginTries(username);
    }

    @RequestMapping(value ="get_organization_by_id" , method = RequestMethod.GET)
    public OrganizationObject getOrganizationById (int id){
        return persist.getOrganizationById( id);
    }

    @RequestMapping(value ="get_all_organizations" , method = RequestMethod.GET)
    public List<OrganizationObject> gatAllOrganizations(){
            return persist.gatAllOrganizations();
    }

    @RequestMapping(value ="get_all_shops" , method = RequestMethod.GET)
    public List<ShopObject> getAllShops (){
            return persist.getAllShops ();
    }

    @RequestMapping(value ="get_all_discounts" , method = RequestMethod.GET)
    public List<DiscountObject> getAllDiscounts (){
        return persist.getAllDiscounts ();
    }
    @RequestMapping(value ="get_all_discounts_to_table" , method = RequestMethod.GET)
    public List<ArrayList<String>> getAllDOS (){
        return persist.getAllDOS ();
    }

    @RequestMapping(value ="add_user_to_organization", method = RequestMethod.POST)
    public void addUseToOrganization(String token, int organizationId){
        persist.addUserToOrganization(token, organizationId);
    }
    @RequestMapping(value ="delete_user_from_organization", method = RequestMethod.POST)
    public void deleteUserFromOrganization(String token, int organizationId){
        persist.deleteUserFromOrganization(token, organizationId);
    }
    @RequestMapping(value ="add_discount_to_organization", method = RequestMethod.POST)
    public void addDiscountToOrganization(int discountId, int organizationId){
        persist.addDiscountToOrganization(discountId, organizationId);
    }
    @RequestMapping(value ="get_all_organizations_for_user" , method = RequestMethod.GET)
    public List<OrganizationObject> gatAllOrganizationsForUser(String token) throws JsonProcessingException {
        return persist.gatAllOrganizationsForUser(token);
    }
    @RequestMapping(value ="get_all_discounts_for_user" , method = RequestMethod.GET)
    public List<DiscountObject> gatAllDiscountsForUser(String token) throws JsonProcessingException {

        return persist.gatAllDiscountsForUser(token);
    }


    @RequestMapping(value ="dose_user_belong_to_organization" , method = RequestMethod.GET)
    public boolean doseUserBelongToOrganization (String token , int organizationId){
        return persist.doseUserBelongToOrganization(token, organizationId);
    }
    @RequestMapping(value ="get_users_to_send_discount_notification" , method = RequestMethod.GET)
    public List<UserObject> getUsersToSendDiscountNotification (DiscountObject discount) {
        return persist.getUsersToSendDiscountNotification(discount);
    }


}
