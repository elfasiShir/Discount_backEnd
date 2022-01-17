package com.dev.objects;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class OrganizationObject {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int organizationId;

    @Column
    private String organizationName;


    @ManyToMany
    @JoinTable (name = "user_organization",
            joinColumns = {@JoinColumn(name="organizationId")},
            inverseJoinColumns = {@JoinColumn(name = "UserId")})
    Set<UserObject> userInOrganization;

    @ManyToMany
     (mappedBy = "discountForOrganization")
    Set<DiscountObject> discountId;



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



    public Set<DiscountObject> getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Set<DiscountObject> discountId) {
        this.discountId = discountId;
    }

    public Set<UserObject> getUserInOrganization() {
        return userInOrganization;
    }

    public void setUserInOrganization(Set<UserObject> userInOrganization) {
        this.userInOrganization = userInOrganization;
    }
}
