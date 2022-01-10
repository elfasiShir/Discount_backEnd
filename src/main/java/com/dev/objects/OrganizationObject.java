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



    public void setOperations(List<DiscountObject> operations) {
        this.operations = operations;
    }

    @Transient
    private List<DiscountObject> operations;


    @ManyToMany
    @JoinTable (name = "user_organization", joinColumns = {@JoinColumn(name="organizationId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")})
    Set<UserObject> users = new HashSet<>();

    @ManyToMany
    @JoinTable (name = "organization_discount", joinColumns = {@JoinColumn(name="organizationId")},
            inverseJoinColumns = {@JoinColumn(name = "operationId")})
    Set<DiscountObject> operation = new HashSet<>();



    public int getOrganizationId() {return organizationId; }

    public void setOrganizationId(int organizationId) { this.organizationId = organizationId; }

    public String getOrganizationName() { return organizationName; }

    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }


}