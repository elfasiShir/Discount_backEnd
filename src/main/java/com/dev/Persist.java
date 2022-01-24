package com.dev;

import com.dev.objects.DiscountObject;
import com.dev.objects.OrganizationObject;
import com.dev.objects.ShopObject;
import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component
public class Persist {
    private final SessionFactory sessionFactory;
    private Connection connection;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ashCollege", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //הרשמה
    public boolean signUp(String username, String password) {
        boolean success = false;
        if (doesUsernameTaken(username)) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserObject userObject = new UserObject(username, password, Utils.createHash(username, password));
            session.save(userObject);
            transaction.commit();
            session.close();
            if (userObject.getUserId() != 0) {
                success = true;

            }
        }
        return success;
    }

    //כניסה
    public String logIn(String username, String password) {
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery(
                "FROM UserObject u WHERE u.username =:username AND u.password =:password")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();
        if (userObject != null){
            updateLoginTries(username);
            return userObject.getToken();}
        else
            return null;
    }

    public UserObject getUserByToken (String token){
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery(
                "FROM UserObject u WHERE u.token = :token")
                .setParameter("token",token)
                .uniqueResult();
        session.close();
        return userObject;
    }


    public UserObject getUserByUsername (String username){
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery(
                        "FROM UserObject u WHERE u.username = :username")
                .setParameter("username",username)
                .uniqueResult();
        session.close();
        return userObject;
    }


    public void countDownTries(String username){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject userObject = getUserByUsername (username);
        int loginTries = userObject.getLogin_tries()-1;
        userObject.setLogin_tries(loginTries);
        transaction.commit();
        session.close();
    }
    public int isBlocked(String username){
        //returns number of tries 0 is blocked
        return getUserByUsername(username).getLogin_tries();
    }

    public void updateLoginTries(String username){
        //updates user's login tries back to 5, activate this only when a user successfully logins
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject userObject = getUserByUsername (username);
        userObject.setLogin_tries(5);
        transaction.commit();
        session.close();
    }

    public boolean doesUsernameTaken (String username) {
        boolean usernameExist = false;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery(
                "FROM UserObject u WHERE u.username =:username")
                .setParameter("username", username)
                .uniqueResult();
        session.close();

        if (userObject != null) {
            usernameExist = true;
        }
        return usernameExist;
    }



    public OrganizationObject getOrganizationById (int id) {
        Session session = sessionFactory.openSession();
        OrganizationObject organization = (OrganizationObject)
                session.createQuery("FROM OrganizationObject o WHERE o.organizationId =:id")
                        .setParameter("id", id)
                        .uniqueResult();
        session.close();
        return organization;
    }

    public ShopObject getShopById (int id){
        Session session = sessionFactory.openSession();
        ShopObject shop = (ShopObject)
                session.createQuery("FROM ShopObject s WHERE s.shopId =:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return shop;
    }

    // get all organizations
    public List<OrganizationObject> gatAllOrganizations() {

        return sessionFactory.openSession().createQuery("FROM OrganizationObject ").list();

    }

    public List<ShopObject> getAllShops (){
        return sessionFactory.openSession().createQuery("FROM ShopObject ").list();

    }

    public List<DiscountObject> getAllDiscounts (){
        return sessionFactory.openSession().createQuery("FROM DiscountObject ").list();

    }

    public void addUserToOrganization(String token, int organizationId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject user = (UserObject)
                session.createQuery("FROM UserObject WHERE token =: token ")
                        .setParameter("token", token).getSingleResult();
        OrganizationObject organization = session.load(OrganizationObject.class, organizationId);
        if(user != null && organization != null){
            user.getOrganizations().add(organization);
            organization.getUsers().add(user);
        }
        transaction.commit();
        session.close();
    }

    public void deleteUserFromOrganization(String token, int organizationId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject user = (UserObject)
                session.createQuery("FROM UserObject WHERE token =: token ")
                        .setParameter("token", token).getSingleResult();
        OrganizationObject organization = session.load(OrganizationObject.class, organizationId);
        if(user != null && organization != null){
            user.getOrganizations().remove(organization);
            organization.getUsers().remove(user);
        }
        transaction.commit();
        session.close();
    }


    public List<OrganizationObject> gatAllOrganizationsForUser(String token) {
        UserObject user = getUserByToken(token);
        List<OrganizationObject> organizations = (List<OrganizationObject>) user.getOrganizations();
        return organizations;

    }
    public List<DiscountObject> gatAllDiscountsForUser(String token) {
        UserObject user = getUserByToken(token);
        List<DiscountObject> discounts = null;
        List<OrganizationObject> organizations = (List<OrganizationObject>) user.getOrganizations();
        for (OrganizationObject organization : organizations ){
            discounts.addAll(organization.getDiscounts());
        }
        return discounts;
    }

    public boolean doseUserBelongToOrganization (String token , int organizationId){
       UserObject user = getUserByToken(token);
       return user.getOrganizations().contains(getOrganizationById(organizationId)) ;
    }

    public void addDiscountToOrganization(int discountId, int organizationId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        DiscountObject discount = (DiscountObject)
                session.createQuery("FROM DiscountObject WHERE discountId =: discountId ")
                        .setParameter("discountId", discountId).getSingleResult();

        OrganizationObject organization = (OrganizationObject)
                session.createQuery("FROM OrganizationObject WHERE organizationId =: organizationId ")
                .setParameter("organizationId", organizationId).getSingleResult();

        if(discount != null && organization != null){
            discount.getOrganizations().add(organization);
            organization.getDiscounts().add(discount);
        }
        transaction.commit();
        session.close();
    }


    public List<UserObject> getUsersToSendDiscountNotification (DiscountObject discount) {
        List<UserObject> userObjectList = null;
        Set<OrganizationObject> organizations = discount.getOrganizations();
        for (OrganizationObject organization : organizations ){
         userObjectList.addAll(organization.getUsers());
        }
        userObjectList.clear();
     return userObjectList;
    }
}