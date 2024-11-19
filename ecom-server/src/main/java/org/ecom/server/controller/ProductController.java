package org.ecom.server.controller;

import org.ecom.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

//@RestController
//@AllArgsConstructor
public class ProductController {

//   @Autowired
   private ProductService productService;
}
