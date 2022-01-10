package com.dev.objects;


import javax.persistence.*;

@Embeddable
@Entity
@Table(name= "shops")
public class ShopObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int shopId;

    @Column (name = "shopName")
    private String shopName;

    @Column (name = "type")
    private String type;



    public int getId() {return shopId;}

    public void setId(int id) {this.shopId = id;}

    public String getShopName() {return shopName;}

    public void setShopName(String shopname) {this.shopName = shopname;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
}
