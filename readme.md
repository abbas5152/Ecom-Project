# E-Commerce Coupon Management System

## Project Overview

The **E-Commerce Coupon Management System** is an API-driven solution for managing coupons in an e-commerce store. It 
supports the creation, retrieval, apply coupon, and manage expiry of coupons.
---

## Features

- **Coupon Management**: CRUD operations for coupons.
- **Discount Application**: Handles cart-level, product-level, and Buy X Get Y discounts.
- **Scheduled Tasks**: Automatically removes expired coupons.it will check in every 5 minutes
- **Pagination**: Supports paginated API responses for efficient data handling.Currently did a hardcoding there
- **Validation**: Ensures coupons meet business rules before comes to service layer.
- **Exception Handling**: Handles the exception and showing relevant messages with error code.

## **Tech Stack**

- **Backend Framework**: Spring Boot
- **Database**: PostgreSQL
- **ORM Framework**: Hibernate (JPA)
- **Dependency Management**: Maven
- **Programming Language**: Java

## Business Logic

### 1. **Cart-Wise Coupons**
- Applicable when the cart value exceeds a set threshold.
- Example: 10% off for cart values above 500 Rs.

### 2. **Product-Wise Coupons**
- Discounts on specific products.
- Example: 20% off on "Product 1."

### 3. **Buy X Get Y Coupons**
- Offers additional products free when specific items are purchased.
- Example: Buy 2 "Product 1" and get 1 "Product 3" free.

### 4. **Coupon Expiry**
- A scheduled job deletes expired coupons at regular intervals (5 Mins).

---

# Database Schema Documentation

## Tables

### 1. `coupons`
| Column              | Type            | Description                                               |
|---------------------|-----------------|-----------------------------------------------------------|
| `id`                | BIGINT          | Primary key                                               |
| `coupon_type`       | ENUM            | Type of coupon (`CART_WISE`, `PRODUCT_WISE`, `BXGY_WISE`) |
| `created_by`        | VARCHAR(255)    | Creator's name (`monk-commerce`)                          |
| `created_on`        | TIMESTAMP       | Creation timestamp                                        |
| `last_modified_by`  | VARCHAR(255)    | Last modifier's name(`monk-commerce`)                     |
| `is_deleted`        | BOOLEAN         | Soft delete flag                                          |
 |  `end_date`        | TIMESTAMP       | end date of coupon                                        |
---

### 2. `cart_wise_coupons`
| Column              | Type            | Description                               |
|---------------------|-----------------|-------------------------------------------|
| `id`                | BIGINT          | Foreign key to `coupons`                  |
| `threshold`         | DECIMAL         | thresold cart value for coupon applicable |
| `discount`          | DECIMAL         | Discount percentage                       |

---

### 3. `product_wise_coupons`
| Column              | Type            | Description              |
|---------------------|-----------------|--------------------------|
| `id`                | BIGINT          | Foreign key to `coupons` |
| `product_id`        | BIGINT          | Target product ID        |
| `discount`          | DECIMAL         | Discount percentage      |

---

### 4. `buy_x_get_y_coupons`
| Column              | Type            | Description                                             |
|---------------------|-----------------|---------------------------------------------------------|
| `id`                | BIGINT          | Foreign key to `coupons`                                |
| `repetition_limit`  | INT             | Maximum number of repetitions of the offer             |

---

### 5. `buy_x_get_y_buy_products` (Mapping Table)
| Column              | Type            | Description                           |
|---------------------|-----------------|---------------------------------------|
| `coupon_id`         | BIGINT          | Foreign key to `buy_x_get_y_coupons`  |
| `product_id`        | BIGINT          | Foreign key to `transaction_product ` |
| `quantity`          | INT             | Quantity of the product required      |

---

### 6. `buy_x_get_y_get_products` (Mapping Table)
| Column              | Type            | Description                           |
|---------------------|-----------------|---------------------------------------|
| `coupon_id`         | BIGINT          | Foreign key to `buy_x_get_y_coupons`  |
| `product_id`        | BIGINT          | Foreign key to `transaction_product`  |
| `quantity`          | INT             | Quantity of the free product          |

---

### 7. `transaction_product`
| Column              | Type            | Description               |
|---------------------|-----------------|---------------------------|
| `id`                | BIGINT          | Primary key               |
| `product_id`        | BIGINT          | Identifier of the product |
| `quantity`          | INT             | Quantity of the product   |

---

## Relationships

- **`coupons`**: Parent table for all coupon types.
- **`cart_wise_coupons`**: Relates to `coupons` for cart-based discounts.
- **`product_wise_coupons`**: Relates to `coupons` for product-specific discounts.
- **`buy_x_get_y_coupons`**: Relates to `coupons` for Buy X Get Y offers.
- `buy_x_get_y_buy_products` and `buy_x_get_y_get_products`: Define products required and given for 
  `buy_x_get_y_coupons`.
- **`transaction_product`**: Used for defining product details and quantities in transactions.

## API Endpoints

- GET /api/coupons: Fetch paginated list of coupons.
- GET /api/coupons/{id}: Fetch particular coupon with id.
- POST /api/coupons: Create a new coupon based on details provided.
- PUT /api/coupons/{id}: Update an existing coupon.
- DELETE /api/coupons/{id}: Soft delete a coupon.
- POST /api/coupons/apply-coupons: Applicable coupons for a cart.
- POST /api/coupons/apply-coupons/{id}: Apply a coupon to a cart with particular coupon id.

## Scheduled Tasks

- Check and Remove Expired Coupons:
- Scheduled every 5 minutes to delete expired coupons using @Scheduled.

# **Implementation**

1. **Creating Coupons**
  - You can create any type of coupon, and the system will populate the relevant tables according to the coupon type.

2. **Saving Coupons**
  - All three types of coupons can be created and stored in the database with their respective details.

3. **Retrieving Coupons**
  - You can fetch a specific coupon by its ID or retrieve all coupons. The system provides complete details of the coupon.

4. **Updating Coupons**
  - The details of a coupon can be updated using its ID.

5. **Deleting Coupons**
  - A coupon can be soft-deleted by marking it as inactive, using its ID.

6. **Applying Coupons**
  - The system allows applying a specific coupon to the cart during checkout.

7. **Viewing Available Coupons**
  - You can retrieve a list of coupons that are currently available for the cart.

---

## **Possible Improvements**

1. **Use of DTOs in Responses**
  - The API should return well-structured **Data Transfer Objects (DTOs)** instead of exposing raw entity data.

2. **Database Optimization**
  - The current design requires multiple database calls to fetch all coupon details. This can be optimized for better performance.

3. **Support for Bulk Operations**
  - Add functionality to create, update, and delete multiple coupons in a single API call.

4. **Pagination for Coupon Listings**
  - Introduce pagination for fetching coupon lists instead of relying on hardcoded limits.

5. **Enhanced Query Capabilities**
  - Include query parameters to filter coupons based on:
    - **Coupon type**
    - **Product IDs**
    - **Threshold values**, etc.

6. **Redesign Coupon DTO**
  - Structure the **Coupon DTOs** to provide distinct responses for different coupon types, avoiding redundant or irrelevant fields.

7. **Refactor Scheduler**
  - Replace the 5-minute periodic scheduler with an event-driven approach to avoid potential inconsistencies.

---

## **Potential Additions**

1. **Customer-Specific Coupons**
  - Introduce customer-linked coupons for personalized discounts.

2. **Suggestion API**
  - Develop an API that suggests applicable coupons based on cart details. For example, if a customer adds product X to the cart, they might get product Y for free.

3. **Payment-Specific Coupons**
  - Add coupon types that are valid only for specific payment methods.

4. **Hierarchical Coupon Types**
  - Allow **Cart-Wise Coupons** to be subtypes of **Product-Wise Coupons** and vice versa.
  - Similarly, extend **Buy X Get Y Coupons** to include **Product-Wise Coupons** as a subtype.

5. **First Purchase Coupons**
  - Add a coupon type specifically for first-time buyers.

6. **Shipping-Free Coupons**
  - Introduce coupons that offer free shipping benefits.

---

## Explanation video of this project
- 