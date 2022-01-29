package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class OrganizationObject  {



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "organizationName")
    private String organizationName;
    @Transient

    boolean member;


    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade= {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = UserObject.class,
            mappedBy = "organizations")
    @JsonIgnoreProperties("organizations")
    private Set<UserObject> users = new HashSet<>();

//
//    @ManyToMany(
//            fetch = FetchType.LAZY,
//            cascade= {
//                    CascadeType.DETACH,
//                    CascadeType.MERGE,
//                    CascadeType.REFRESH,
//                    CascadeType.PERSIST
//            },
//            targetEntity = DiscountObject.class,
//            mappedBy = "organizations")
//    @JsonIgnoreProperties("organizations")
//    private Set<DiscountObject> discounts = new HashSet<>();


    //Constructors

    public OrganizationObject(int id, String organizationName) {
        this.id = id;
        this.organizationName = organizationName;
    }

    public OrganizationObject() {

    }

    public int getId() {
        return id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public Set<UserObject> getUsers() { return users; }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }


    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }



//    public Set<DiscountObject> getDiscounts() {
//        return discounts;
//    }
//
//    public void setDiscounts(Set<DiscountObject> discounts) {
//        this.discounts = discounts;
//    }

}
