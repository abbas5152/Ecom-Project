package org.ecom.model.product;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Products {

   private Long productId;

   private int quantity;

   private String createdBy;

   private Date createdOn;

   private String lastModifiedBy;

   private Date lastModifiedOn;
}
