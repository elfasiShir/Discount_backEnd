
package com.dev.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "discounts")
public abstract class DiscountObject {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String discount;


    @JoinColumn
    @ManyToOne
    private ShopObject discountShop;


    @Column
    private LocalDateTime discountStart;

    @Column

    private LocalDateTime discountEnd;



    @Column
    private boolean validForEveryone = true;


//
//    @ManyToMany(
//            fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.DETACH,
//                    CascadeType.MERGE,
//                    CascadeType.REFRESH,
//                    CascadeType.PERSIST
//            },
//            targetEntity = OrganizationObject.class)
//    @JoinTable(name = "discounts_Organizations",
//            joinColumns = @JoinColumn(name = "discountId"),
//            inverseJoinColumns = @JoinColumn(name = "organizationId"),
//            uniqueConstraints = @UniqueConstraint(columnNames = {"discountId", "organizationId"}))
//    @JsonIgnoreProperties("discounts")
//    private Set<OrganizationObject> organizations = new HashSet<>();

    public DiscountObject(String discount, ShopObject discountShop){
        this.discount = discount;
        this.discountShop = discountShop;
    }
    public DiscountObject(){}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public ShopObject getDiscountShop() {
        return discountShop;
    }

    public void setDiscountShop(ShopObject discountShop) {
        this.discountShop = discountShop;
    }

    public LocalDateTime getDiscountStart() {
        return discountStart;
    }

    public void setDiscountStart(LocalDateTime discountStart) {
        this.discountStart = discountStart;
    }

    public LocalDateTime getDiscountEnd() {
        return discountEnd;
    }

    public void setDiscountEnd(LocalDateTime discountEnd) {
        this.discountEnd = discountEnd;
    }


    public boolean isValidForEveryone() {
        return validForEveryone;
    }

    public void setValidForEveryone(boolean validForEveryone) {
        this.validForEveryone = validForEveryone;
    }


//
//    public Set<OrganizationObject> getOrganizations() {
//        return organizations;
//    }
//
//    public void setOrganizations(Set<OrganizationObject> organizations) {
//        this.organizations = organizations;
//    }
}

