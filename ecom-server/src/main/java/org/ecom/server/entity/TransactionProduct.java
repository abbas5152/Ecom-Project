package org.ecom.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class TransactionProduct {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   private long productId;

   private int quantity;
}
