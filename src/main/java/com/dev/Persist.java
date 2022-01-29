package com.dev;

import com.dev.objects.*;
import com.dev.utils.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

    @Component
    public class Persist {
        private final SessionFactory sessionFactory;

        @Autowired
        public Persist (SessionFactory sf) {
            this.sessionFactory = sf;
        }


        public boolean signUp(String username, String password) {
        boolean success = false;
        if(username!=null && password!=null){
            if(!doesUsernameTaken(username)){
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                UserObject userObject = new UserObject();
                userObject.setUsername(username);
                userObject.setPassword(password);
                String token = Utils.createHash(username, password);
                userObject.setToken(token);
                session.save(userObject);
                transaction.commit();
                session.close();
                if(userObject.getId() != 0)
                    success = true;
            }
        }
        return success;
    }

    public String logIn(String username, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserObject userObject = (UserObject) session.createQuery(
                "FROM UserObject  WHERE username =:username AND password =:password")
                .setParameter("username", username)
                .setParameter("password", password)
                .list();
        transaction.commit();
        session.close();
        if (userObject != null){
            return userObject.getToken();}
        else
            return null;
    }

    public UserObject getUserByToken (String token){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserObject userObject = (UserObject) session.createQuery(
                "FROM UserObject  WHERE token = :token")
                .setParameter("token",token)
                .uniqueResult();

        transaction.commit();
        session.close();
        return userObject;
    }


    public boolean doesUsernameTaken (String username) {
        boolean usernameExist = false;
        Session session = sessionFactory.openSession();
        List list =  session.createQuery(
                "FROM UserObject  WHERE username =:username")
                .setParameter("username", username)
                .list();
        session.close();

        if (list.size() == 0) {
            usernameExist = true;
        }
        return usernameExist;
    }


    public OrganizationObject getOrganizationById (int id) {
        Session session = sessionFactory.openSession();
        OrganizationObject organization = (OrganizationObject)
                session.createQuery("FROM OrganizationObject o WHERE id =:id")
                        .setParameter("id", id)
                        .uniqueResult();
        session.close();
        return organization;
    }

    public ShopObject getShopById (int id){
        Session session = sessionFactory.openSession();
        ShopObject shop = (ShopObject)
                session.createQuery("FROM ShopObject WHERE id =:id")
                .setParameter("id",id)
                .uniqueResult();
        session.close();
        return shop;
    }

    public List<OrganizationObject> getAllOrganizations() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<OrganizationObject> allOrganizations = new ArrayList<>();

        for(Object organization : session.createQuery("FROM OrganizationObject ").list())
            allOrganizations.add((OrganizationObject) organization);


         transaction.commit();
         session.close();
        return allOrganizations;
    }

    public List<ShopObject> getAllShops (){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<ShopObject> allShops = new ArrayList<>();

        for(Object shop : session.createQuery("FROM ShopObject ").list())
            allShops.add((ShopObject) shop);


        transaction.commit();
        session.close();
        return allShops;
    }


    public List<DiscountObject> getAllDiscounts() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<DiscountObject> allDiscounts = new ArrayList<>();

        for(Object discount : session.createQuery("FROM DiscountObject ").list())
            allDiscounts.add((DiscountObject) discount);


        transaction.commit();
        session.close();
        return allDiscounts;
    }


    public List<HashMap> getAllDiscountsForTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<DiscountObject> allDiscounts = getAllDiscounts();
        List<HashMap> finalList = new ArrayList<>();

        OrganizationObject organization = new OrganizationObject();
            for(DiscountObject discount : allDiscounts) {
                if (!discount.isValidForEveryone()) {
                    for (Object discountOrgObject : session.createQuery("FROM DiscountOrganization WHERE discount =:discount")
                            .setParameter("discount", discount).list()) {
                        organization =(OrganizationObject) discountOrgObject;}

                }
                HashMap temp = new HashMap();
                temp.put("discount",discount);
                temp.put("organization",organization.getOrganizationName());
                temp.put("shop",discount.getDiscountShop());

                finalList.add(temp);

            }

        transaction.commit();
        session.close();
        return finalList;
    }

    public List<DiscountObject> getDiscountForOneShop(String token, int shopId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserObject user = getUserByToken(token);

        List finalList = null;
        if (user != null) {
            finalList = session.createQuery("FROM DiscountObject WHERE discountShop.id = :shopId ORDER BY id ")
                    .setParameter("shopId", shopId).list();
        }
        transaction.commit();
        session.close();
        return finalList;
    }

    public void addUserToOrganization(String token, int organizationId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject user = getUserByToken(token);
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
        UserObject user = getUserByToken(token);
        OrganizationObject organization = session.load(OrganizationObject.class, organizationId);
        if(user != null && organization != null){
            user.getOrganizations().remove(organization);
            organization.getUsers().remove(user);
        }
        transaction.commit();
        session.close();
    }

   public List<HashMap> getAllOrganizationsForUser(String token) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserObject user = getUserByToken(token);
        List<HashMap> organizationsList = new ArrayList<>();

        if(user != null){
            Set<OrganizationObject> userOrganizations = user.getOrganizations();
            List<OrganizationObject> organizations = session.createQuery("FROM OrganizationObject").list();
            for(OrganizationObject organization : organizations){
                HashMap temp = new HashMap();
                temp.put("organization", organization);
                temp.put("isAMember", userOrganizations.contains(organization));
                organizationsList.add(temp);
            }
        }

        transaction.commit();
        session.close();
        return organizationsList;
    }

    public List<HashMap> getAllDiscountsForUser(String token) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        UserObject user = getUserByToken(token);
        List<DiscountObject> discounts = session.createQuery("FROM DiscountObject ").list();
        List<HashMap> allDiscounts = new ArrayList<>();

        if(user != null){
            OrganizationObject organization = new OrganizationObject();
            for(DiscountObject discount : discounts) {
                for (Object disOrg : session.createQuery("FROM DiscountOrganization WHERE discount =:discount")
                        .setParameter("discount", discount).list()) {
                    organization =(OrganizationObject) disOrg;

                }
                HashMap temp = new HashMap();
                temp.put("discount",discount.getDiscount());
                temp.put("organization",organization.getOrganizationName());
                temp.put("shop",discount.getDiscountShop());
                allDiscounts.add(temp);
            }
        }
        transaction.commit();
        session.close();
        return allDiscounts;
    }

    public Set<UserObject> getUsersForOneDiscount(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Set<UserObject> users = new HashSet<>();
        DiscountObject discount = session.load(DiscountObject.class, id);

        if(discount.isValidForEveryone()) {
            users.addAll(session.createQuery("FROM UserObject ").list());
        }
        else {
            List discountOrganization = session.createQuery("FROM DiscountObject WHERE id = :id")
                    .setParameter("id", id).list();
            for (Object saleOrg : discountOrganization) {
                users.addAll(((DiscountOrganization) saleOrg).getOrganization().getUsers());
            }
        }

        transaction.commit();
        session.close();
        return users;
    }



}