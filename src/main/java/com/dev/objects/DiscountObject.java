
package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "discounts")
public abstract class DiscountObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int discountId;

    @Column
    private String discount;


    @Column
    private String discountShop;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date discountStart;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date discountEnd;

    @Column
    private int validForEveryone = 0;



    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = OrganizationObject.class)
    @JoinTable(name = "discounts_Organizations",
            joinColumns = @JoinColumn(name = "discountId"),
            inverseJoinColumns = @JoinColumn(name = "organizationId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"discountId", "organizationId"}))
    @JsonIgnoreProperties("discounts")
    private Set<OrganizationObject> organizations = new HashSet<>();


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

    public String getDiscountShop() {
        return discountShop;
    }

    public void setDiscountShop(String discountShop) {
        this.discountShop = discountShop;
    }

    public Date getDiscountStart() {
        return discountStart;
    }

    public void setDiscountStart(Date discountStart) {
        this.discountStart = discountStart;
    }

    public Date getDiscountEnd() {
        return discountEnd;
    }

    public void setDiscountEnd(Date discountEnd) {
        this.discountEnd = this.discountEnd;
    }

    public int getValidForEveryone() {
        return validForEveryone;
    }

    public void setValidForEveryone(int validForEveryone) {
        this.validForEveryone = validForEveryone;
    }


    public Set<OrganizationObject> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<OrganizationObject> organizations) {
        this.organizations = organizations;
    }
}

