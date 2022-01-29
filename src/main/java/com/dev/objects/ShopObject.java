package com.dev.objects;
import javax.persistence.*;


@Embeddable
@Entity
@Table(name= "shops")
public class ShopObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int id;

    @Column
    private String shopName;


    public String getShopName() {return shopName;}

    public void setShopName(String shopName) {this.shopName = shopName;}

    public int getId() {
        return id;
    }


}
