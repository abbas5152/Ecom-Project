package org.ecom.server.entity;

import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.ecom.model.enums.CouponType;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicUpdate
@Inheritance(strategy = InheritanceType.JOINED)
public class Coupon {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   @Enumerated(value = EnumType.STRING)
   private CouponType couponType;

   @Column(name = "end_date")
   private Date endDate;

   @Column(name = "created_by", updatable = false)
   private String createdBy;

   @Column(name = "created_on", updatable = false)
   private Date createdOn;

   @Column(name = "last_modified_by")
   private String lastModifiedBy;

   @Column(name = "last_modified_on")
   private Date lastModifiedOn;

   @Column(name = "is_deleted")
   private Boolean isDeleted;

   @Column(name = "is_completed")
   private Boolean isCompleted;

   @PrePersist
   protected void onCreate() {
      // Set the time zone to UTC
      TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
      TimeZone.setDefault(utcTimeZone);
      Date utcDate = new Date(System.currentTimeMillis());
      createdOn = utcDate;
      createdBy = "monk-commerce";
      lastModifiedOn = utcDate;
      lastModifiedBy = "monk-commerce";
      isDeleted = false;
   }

   @PreUpdate
   protected void onUpdate() {
      // Set the time zone to UTC
      TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
      TimeZone.setDefault(utcTimeZone);
      Date utcDate = new Date(System.currentTimeMillis());
      lastModifiedOn = utcDate;
      lastModifiedBy = "monk-commerce";

   }
}

