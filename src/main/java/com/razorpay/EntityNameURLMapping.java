package com.razorpay;

import java.util.Arrays;

 /**
 * Enum name is acting as url & entity is denoting Entity class
  * ex: https://api.razorpay.com/v1/invocies
  * getEntityName method will take "invoices" from above mentioned url
  * & will return "invoice" as entity name as mentioned below in mapping INVOICES("invoice")
 */

public enum EntityNameURLMapping {
  
    INVOICES("invoice"),
    SETTLEMENTS("settlement"),
	  PAYMENTS("payment"),
    PAYMENT_LINKS("payment_link"),
    ITEMS("item"); 

    private String entity;

    EntityNameURLMapping(String entity) {
        this.entity= entity;
    }
    
    private String getEntity()
    {
        return entity;
    }

    public static String getEntityName(String urlStirng)
    {
        EntityNameURLMapping item = Arrays.asList(values()).stream().filter( val -> val.name().equalsIgnoreCase(urlStirng)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to resolve"));
        return item.getEntity();
    }
}