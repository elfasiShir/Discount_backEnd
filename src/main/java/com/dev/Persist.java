package com.dev;

import com.dev.objects.*;
import com.dev.utils.Utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.*;


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


    public boolean signUp(String username, String password) {
        boolean success = false;
        if(username!=null && password!=null){
            if(!doesUsernameTaken(username)){
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                UserObject userObject = new UserObject(username, password, Utils.createHash(username, password));
                session.save(userObject);
                transaction.commit();
                session.close();
                if(userObject.getUserId() != 0)
                    success = true;
            }
        }
        return success;
    }

    public String logIn(String username, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List list = session.createQuery("FROM UserObject WHERE username = :username AND password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .list();

        transaction.commit();
        session.close();
        return list.size() != 0 ? ((UserObject) list.get(0)).getToken() : null;
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

    public List<OrganizationObject> getAllOrganizations() {
        Session session = sessionFactory.openSession();
        List<OrganizationObject> allOrganizations = (List<OrganizationObject>) session.createQuery(
                        " FROM OrganizationObject ")
                .list();
        session.close();
        return allOrganizations;
    }

    public List<ShopObject> getAllShops (){
        Session session = sessionFactory.openSession();
        List<ShopObject> allShops = (List<ShopObject>) session.createQuery(
                        " FROM ShopObject ")
                .list();
        session.close();
        return allShops;
    }


//    public List<UserObject> getAllUsers() {
//        Session session = sessionFactory.openSession();
//        List<UserObject> allUsers = (List<UserObject>) session.createQuery(
//                        " FROM UserObject ")
//                .list();
//        session.close();
//        return allUsers;
//    }

    public List<DiscountObject> getAllDiscounts() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<DiscountObject> discounts = new ArrayList<>();
        List temp = session.createQuery("FROM DiscountObject ").list();
        for(Object d : temp)
            discounts.add((DiscountObject) d);
        transaction.commit();
        session.close();
        return discounts;
    }


    public List<HashMap> getAllDiscountsForTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<DiscountObject> allDiscounts = getAllDiscounts();
        List<HashMap> finalList = new ArrayList<>();

            for(DiscountObject discount : allDiscounts) {
                if (!discount.isValidForEveryone()) {
                    for (Object discountOrgObject : session.createQuery("FROM DiscountOrganization WHERE discount =:discount")
                            .setParameter("discount", discount).list()) ;

                }
                HashMap temp = new HashMap();
                temp.put("discount",discount);
                temp.put("shop",discount.getDiscountShop());
                finalList.add(temp);

            }

        transaction.commit();
        session.close();
        return finalList;
    }

    public List<DiscountObject> gatAllDiscountsForUser(String token) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserObject user = getUserByToken(token);
        Set<DiscountObject> finalList = null;

        if (user != null) {
            Set<OrganizationObject> userOrganizations = user.getOrganizations();
            finalList = new HashSet<DiscountObject>(session.createQuery("FROM DiscountObject WHERE validForEveryone = true").list());
            for (Object saleOrgObject : session.createQuery("FROM DiscountOrganization ").list()) {
                if(userOrganizations.contains(((DiscountOrganization)saleOrgObject).getOrganization())){
                    finalList.add(((DiscountOrganization) saleOrgObject).getDiscount());
                }
            }
        }
        transaction.commit();
        session.close();
        return (List<DiscountObject>) finalList;
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


    public List<OrganizationObject> getAllOrganizationsForUser(String token) {
        UserObject user = getUserByToken(token);
        List<OrganizationObject> organizations = this.getAllOrganizations();
        for (OrganizationObject organization : organizations) {
            if (!user.getOrganizations().contains(organization)) { organization.setMember(false); }
            else { organization.setMember(true);}
        }
        return organizations;

    }

    public Set<DiscountObject> getAllDiscountsForUser (String token) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserObject user = getUserByToken(token);
        Set<DiscountObject> finalList = null;

        if (user != null) {
            Set<OrganizationObject> userOrganizations = user.getOrganizations();
            finalList = new HashSet<DiscountObject>(session.createQuery("FROM DiscountObject WHERE validForEveryone = true").list());
            for (Object discountOrgObject : session.createQuery("FROM DiscountOrganization ").list()) {
                if(userOrganizations.contains(((DiscountOrganization)discountOrgObject ).getOrganization())){
                    finalList.add(((DiscountOrganization)discountOrgObject).getDiscount());
                }
            }
        }
        transaction.commit();
        session.close();
        return finalList;
    }
}