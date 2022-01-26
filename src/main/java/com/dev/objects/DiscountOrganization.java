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



        public DiscountOrganization() { }

        public DiscountOrganization(OrganizationObject organization, DiscountObject discount) {
            this.organization = organization;
            this.discount = discount;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public OrganizationObject getOrganization() {
            return organization;
        }

        public void setOrganization(OrganizationObject organization) {
            this.organization = organization;
        }

        public DiscountObject getDiscount() {
            return discount;
        }

        public void setDiscount(DiscountObject discount) {
            this.discount = discount;
        }

    }



