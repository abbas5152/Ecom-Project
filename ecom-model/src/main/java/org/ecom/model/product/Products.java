package org.ecom.model.product;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Products {

   private Long productId;

   private int quantity;

   private String createdBy;

   private Date createdOn;

   private String lastModifiedBy;

   private Date lastModifiedOn;
}
