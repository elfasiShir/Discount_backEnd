
package com.dev.objects;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date discountStart;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date discountEnd;

    @Column
    private int validForEveryone = 0;



    @ManyToMany
    @JoinTable (name = "organization_discount", joinColumns = {@JoinColumn(name="discountId")},
            inverseJoinColumns = {@JoinColumn(name = "organizationId")})
    Set<OrganizationObject> organization = new HashSet<>();



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





}

