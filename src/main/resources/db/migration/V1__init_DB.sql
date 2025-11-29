-- ============================================
-- 1. DROP FOREIGN KEYS
-- ============================================

ALTER TABLE IF EXISTS buckets DROP CONSTRAINT IF EXISTS fk_buckets_user;
ALTER TABLE IF EXISTS buckets_products DROP CONSTRAINT IF EXISTS fk_bprod_product;
ALTER TABLE IF EXISTS buckets_products DROP CONSTRAINT IF EXISTS fk_bprod_bucket;
ALTER TABLE IF EXISTS orders DROP CONSTRAINT IF EXISTS fk_orders_user;
ALTER TABLE IF EXISTS orders_details DROP CONSTRAINT IF EXISTS fk_od_order;
ALTER TABLE IF EXISTS orders_details DROP CONSTRAINT IF EXISTS fk_od_product;
ALTER TABLE IF EXISTS orders_details DROP CONSTRAINT IF EXISTS fk_od_details_self;
ALTER TABLE IF EXISTS products_category DROP CONSTRAINT IF EXISTS fk_pc_category;
ALTER TABLE IF EXISTS products_category DROP CONSTRAINT IF EXISTS fk_pc_product;
ALTER TABLE IF EXISTS users DROP CONSTRAINT IF EXISTS fk_users_bucket;

-- ============================================
-- 2. DROP TABLES
-- ============================================

DROP TABLE IF EXISTS buckets CASCADE;
DROP TABLE IF EXISTS buckets_products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS orders_details CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS products_category CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================
-- 3. DROP SEQUENCES
-- ============================================

DROP SEQUENCE IF EXISTS bucket_seq;
DROP SEQUENCE IF EXISTS category_seq;
DROP SEQUENCE IF EXISTS order_seq;
DROP SEQUENCE IF EXISTS orders_details_seq;
DROP SEQUENCE IF EXISTS product_seq;
DROP SEQUENCE IF EXISTS users_seq;

-- ============================================
-- 4. CREATE SEQUENCES
-- ============================================

CREATE SEQUENCE bucket_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE order_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE orders_details_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

-- ============================================
-- 5. CREATE TABLES
-- ============================================

CREATE TABLE buckets (
                         id BIGINT NOT NULL,
                         user_id BIGINT UNIQUE,
                         PRIMARY KEY (id)
);

CREATE TABLE buckets_products (
                                  bucket_id BIGINT NOT NULL,
                                  product_id BIGINT NOT NULL
);

CREATE TABLE categories (
                            id BIGINT NOT NULL,
                            title VARCHAR(255),
                            PRIMARY KEY (id)
);

CREATE TABLE orders (
                        id BIGINT NOT NULL,
                        price NUMERIC(38,2),
                        created_at TIMESTAMP(6),
                        updated_at TIMESTAMP(6),
                        user_id BIGINT,
                        address VARCHAR(255),
                        status VARCHAR(255) CHECK (status IN ('NEW','APPROVED','CANCELED','PAID','CLOSED')),
                        PRIMARY KEY (id)
);

CREATE TABLE orders_details (
                                id BIGINT NOT NULL,
                                amount NUMERIC(38,2),
                                price NUMERIC(38,2),
                                details_id BIGINT NOT NULL UNIQUE,
                                order_id BIGINT,
                                product_id BIGINT,
                                PRIMARY KEY (id)
);

CREATE TABLE products (
                          id BIGINT NOT NULL,
                          title VARCHAR(255),
                          price NUMERIC(38,2),
                          PRIMARY KEY (id)
);

CREATE TABLE products_category (
                                   category_id BIGINT NOT NULL,
                                   product_id BIGINT NOT NULL
);

CREATE TABLE users (
                       id BIGINT NOT NULL,
                       bucket_id BIGINT UNIQUE,
                       email VARCHAR(255),
                       password VARCHAR(255),
                       phone VARCHAR(255),
                       archived BOOLEAN NOT NULL,
                       role VARCHAR(255) CHECK (role IN ('ADMIN','CLIENT','MANAGER')),
                       username VARCHAR(255),
                       PRIMARY KEY (id)
);

-- =======================
-- 6. ADD FOREIGN KEYS
-- =======================

ALTER TABLE buckets
    ADD CONSTRAINT fk_buckets_user FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE buckets_products
    ADD CONSTRAINT fk_bprod_product FOREIGN KEY (product_id) REFERENCES products;

ALTER TABLE buckets_products
    ADD CONSTRAINT fk_bprod_bucket FOREIGN KEY (bucket_id) REFERENCES buckets;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE orders_details
    ADD CONSTRAINT fk_od_order FOREIGN KEY (order_id) REFERENCES orders;

ALTER TABLE orders_details
    ADD CONSTRAINT fk_od_product FOREIGN KEY (product_id) REFERENCES products;

ALTER TABLE orders_details
    ADD CONSTRAINT fk_od_details_self FOREIGN KEY (details_id) REFERENCES orders_details;

ALTER TABLE products_category
    ADD CONSTRAINT fk_pc_category FOREIGN KEY (category_id) REFERENCES categories;

ALTER TABLE products_category
    ADD CONSTRAINT fk_pc_product FOREIGN KEY (product_id) REFERENCES products;

ALTER TABLE users
    ADD CONSTRAINT fk_users_bucket FOREIGN KEY (bucket_id) REFERENCES buckets;

