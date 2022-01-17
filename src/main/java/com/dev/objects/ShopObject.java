package com.dev.objects;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Embeddable
@Entity
@Table(name= "shops")
public class ShopObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int shopId;

    @Column
    private String shopName;

    @Column
    private String type;

    @OneToMany
    private Set<DiscountObject> discount = new HashSet<DiscountObject>();

    public int getId() {return shopId;}

    public void setId(int id) {this.shopId = id;}

    public String getShopName() {return shopName;}

    public void setShopName(String shopname) {this.shopName = shopname;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
}
