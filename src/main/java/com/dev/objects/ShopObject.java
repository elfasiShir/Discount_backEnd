package com.dev.objects;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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




    public String getShopName() {return shopName;}

    public void setShopName(String shopName) {this.shopName = shopName;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }


}
