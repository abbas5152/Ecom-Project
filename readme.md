# E-Commerce Coupon Management System

## Project Overview

The **E-Commerce Coupon Management System** is an API-driven solution for managing coupons in an online shopping platform. It supports the creation, retrieval, application, and expiry management of coupons while adhering to robust business rules.

---

## Features

- **Coupon Management**: CRUD operations for coupons.
- **Discount Application**: Handles cart-level, product-level, and Buy X Get Y discounts.
- **Scheduled Tasks**: Automatically removes expired coupons.
- **Pagination**: Supports paginated API responses for efficient data handling.
- **Validation**: Ensures coupons meet business rules before application.

---

## Business Logic

### 1. **Cart-Wise Coupons**
- Applicable when the cart value exceeds a set threshold.
- Example: 10% off for cart values above $500.

### 2. **Product-Wise Coupons**
- Discounts on specific products.
- Example: 20% off on "Product A."

### 3. **Buy X Get Y Coupons**
- Offers additional products free when specific items are purchased.
- Example: Buy 2 "Product B" and get 1 "Product C" free.

### 4. **Coupon Expiry**
- A scheduled job deletes expired coupons at regular intervals.

---

## Database Schema

### **Tables**

#### 1. `coupons`
| Column              | Type           | Description                              |
|---------------------|----------------|------------------------------------------|
| `id`                | BIGINT         | Primary key                              |
| `coupon_type`       | ENUM           | Type of coupon (`CART_WISE`, `PRODUCT_WISE`, `BXGY_WISE`) |
| `created_by`        | VARCHAR(255)   | Creator's name                           |
| `created_on`        | TIMESTAMP      | Creation timestamp                       |
| `last_modified_by`  | VARCHAR(255)   | Last modifier's name                     |
| `is_deleted`        | BOOLEAN        | Soft delete flag                         |

#### 2. `cart_wise_coupons`
| Column         | Type     | Description                     |
|----------------|----------|---------------------------------|
| `id`           | BIGINT   | Foreign key to `coupons`        |
| `threshold`    | DECIMAL  | Minimum cart value              |
| `discount`     | DECIMAL  | Percentage discount             |

#### 3. `product_wise_coupons`
| Column         | Type     | Description                     |
|----------------|----------|---------------------------------|
| `id`           | BIGINT   | Foreign key to `coupons`        |
| `product_id`   | BIGINT   | Target product ID               |
| `discount`     | DECIMAL  | Percentage discount             |

#### 4. `buy_x_get_y_coupons`
| Column             | Type         | Description                     |
|--------------------|--------------|---------------------------------|
| `id`               | BIGINT       | Foreign key to `coupons`        |
| `repetition_limit` | INT          | Maximum repetitions of the offer|

#### 5. `buy_products` (Mapping Table)
| Column         | Type     | Description                     |
|----------------|----------|---------------------------------|
| `coupon_id`    | BIGINT   | Foreign key to `buy_x_get_y_coupons` |
| `product_id`   | BIGINT   | Product to buy                  |
| `quantity`     | INT      | Required quantity               |

#### 6. `get_products` (Mapping Table)
| Column         | Type     | Description                     |
|----------------|----------|---------------------------------|
| `coupon_id`    | BIGINT   | Foreign key to `buy_x_get_y_coupons` |
| `product_id`   | BIGINT   | Free product                    |
| `quantity`     | INT      | Free quantity                   |

---

## API Endpoints

- GET /api/coupons: Fetch paginated list of coupons.
- POST /api/coupons: Create a new coupon.
- PUT /api/coupons/{id}: Update an existing coupon.
- DELETE /api/coupons/{id}: Soft delete a coupon.
- POST /api/coupons/apply: Apply a coupon to a cart.

## Scheduled Tasks

- Check and Remove Expired Coupons:
- Scheduled every 5 minutes to delete expired coupons using @Scheduled.