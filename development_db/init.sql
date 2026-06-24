
\c product_db;

-- Drop table if exists (clean slate)
DROP TABLE IF EXISTS product;

-- Create product table
CREATE TABLE product (
    product_id   BIGSERIAL       PRIMARY KEY,
    name         VARCHAR(255)    NOT NULL,
    unit_price   NUMERIC(10, 2)  NOT NULL,
    description  VARCHAR(1000),
    category     VARCHAR(100),
    stock        INTEGER,
    created_at   TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- ── Sample Data (optional — useful for testing) ───────────────────────────
INSERT INTO product (name, unit_price, description, category, stock) VALUES
    ('Laptop',       999.99, 'High performance laptop',     'Electronics', 50),
    ('Wireless Mouse', 29.99, 'Ergonomic wireless mouse',  'Electronics', 200),
    ('Desk Chair',   249.99, 'Comfortable office chair',   'Furniture',   30),
    ('Notebook',       4.99, 'A5 lined notebook 200 pages','Stationery',  500);

-- Verify
SELECT * FROM product;
