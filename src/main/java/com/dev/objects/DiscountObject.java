
package com.dev.objects;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "discount")
public class DiscountObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int discountId;

    @Column
    private String discount;

    @Column
    private String discountStart;

    @Column
    private String discountEnd;

    @Column
    private boolean validForEveryone;

    @ManyToMany
    @JoinTable (name = "organization_discount", joinColumns = {@JoinColumn(name="operationId")},
            inverseJoinColumns = {@JoinColumn(name = "organizationId")})
    Set<OrganizationObject> organization = new HashSet<>();


    public void setOrganizations(List<OrganizationObject> organizations) {
        this.organizations = organizations;
    }

    @Transient
    private List<OrganizationObject> organizations;



    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountStart() {
        return discountStart;
    }

    public void setDiscountStart(String discountStart) {
        this.discountStart = discountStart;
    }

    public String getDiscountEnd() {
        return discountEnd;
    }

    public void setDiscountEnd(String discountEnd) {
        this.discountEnd = discountEnd;
    }

    public boolean isValidForEveryone() {
        return validForEveryone;
    }

    public void setValidForEveryone(boolean validForEveryone) {
        this.validForEveryone = validForEveryone;
    }





}

