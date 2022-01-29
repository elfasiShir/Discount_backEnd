package com.dev.objects;


import javax.persistence.*;


    @Entity
    @Table(name = "Discount_organizations")
    public class DiscountOrganization {
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "organization")
        private OrganizationObject organization;


        @ManyToOne
        @JoinColumn(name = "discounts")
        private DiscountObject discount;


        public DiscountOrganization(int id) {
            this.id = id;
        }
        public DiscountOrganization(int id, OrganizationObject organization, DiscountObject discount) {
            this.id = id;
            this.organization = organization;
            this.discount = discount;
        }

        public DiscountOrganization() {

        }


        //Getters
        public int getId() { return id; }
        public OrganizationObject getOrganization() { return organization; }
        public DiscountObject getDiscount() { return discount; }

        //Setters
        public void setOrganization(OrganizationObject organization) { this.organization = organization; }
        public void setDiscount(DiscountObject discount) { this.discount = discount; }


    }
