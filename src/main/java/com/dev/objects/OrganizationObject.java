package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class OrganizationObject  {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int organizationId;

    @Column
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


    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade= {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = DiscountObject.class,
            mappedBy = "organizations")
    @JsonIgnoreProperties("organizations")
    private Set<DiscountObject> discounts = new HashSet<>();

    //contactor
    public OrganizationObject (OrganizationObject organizations){
        this.organizationId = organizations.getOrganizationId();
        this.organizationName = organizations.getOrganizationName();
    }

    public OrganizationObject() {

    }


    public int getOrganizationId() {return organizationId; }

    public void setOrganizationId(int organizationId) { this.organizationId = organizationId; }

    public String getOrganizationName() { return organizationName; }

    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }


    public Set<UserObject> getUsers() {
        return users;
    }

    public void setUsers(Set<UserObject> users) {
        this.users = users;
    }

    public Set<DiscountObject> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<DiscountObject> discounts) {
        this.discounts = discounts;
    }

}
