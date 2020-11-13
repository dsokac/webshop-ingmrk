CREATE TABLE IF NOT EXISTS products (
	id SERIAL PRIMARY KEY,
	code CHAR(10) NOT NULL CONSTRAINT unique_code UNIQUE,
	name VARCHAR(100) NOT NULL,
	price_hrk DECIMAL(8,2) NOT NULL CONSTRAINT valid_price CHECK(price_hrk >= 0),
	description TEXT DEFAULT NULL,
	is_available BOOLEAN DEFAULT TRUE
);

INSERT INTO products(code, name, price_hrk) VALUES
('0000000001', 'Test product 1', 23.99),
('0000000002', 'Test product 2', 3.99),
('0000000003', 'Test product 3', 2.99),
('0000000004', 'Test product 4', 5.99),
('0000000005', 'Test product 5', 100.99),
('0000000006', 'Test product 6', 78.99);

CREATE TABLE IF NOT EXISTS customers (
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	email VARCHAR(225) NOT NULL CONSTRAINT unique_email UNIQUE
);

INSERT INTO customers(first_name, last_name, email) VALUES 
('Ana', 'Anić', 'aanic@mail.com'),
('Karlo', 'Karlić', 'kkarlic@mail.com'),
('Iva', 'Ivić', 'iivic@mail.com'),
('Samuel', 'Stone', 'sstone@mail.com'),
('Elliot', 'West', 'ewest@mail.com'),
('Betsy', 'Smith', 'bsmith@mail.com'),
('Alyssia', 'May', 'amay@mail.com'),
('Eli', 'Watson', 'ewatson@mail.com'),
('Charlie', 'Webb', 'cwebb@mail.com');

CREATE TYPE order_status AS ENUM ('DRAFT', 'SUBMITTED');
CREATE TABLE IF NOT EXISTS orders (
	id SERIAL PRIMARY KEY,
	customer_id INTEGER NOT NULL,
	status order_status NOT NULL DEFAULT 'DRAFT',
	total_price_hrk DECIMAL(8,2) NOT NULL DEFAULT 0 CONSTRAINT valid_price_hrk CHECK(total_price_hrk >= 0),
	total_price_eur DECIMAL(9,2) NOT NULL DEFAULT 0 CONSTRAINT valid_price_eur CHECK(total_price_eur >= 0),
	CONSTRAINT fk_customer_id_customers_id FOREIGN KEY(customer_id) REFERENCES customers(id) ON UPDATE CASCADE ON DELETE CASCADE
);
COMMENT ON COLUMN orders.status IS 'type order_status with possible values DRAFT and SUBMITTED';

INSERT INTO orders(customer_id, status, total_price_hrk, total_price_eur) VALUES
(1, 'SUBMITTED', 319.8, 43.51),
(1, 'DRAFT', DEFAULT, DEFAULT),
(2, 'DRAFT', DEFAULT, DEFAULT),
(3, 'DRAFT', DEFAULT, DEFAULT),
(4, 'DRAFT', DEFAULT, DEFAULT),
(5, 'DRAFT', DEFAULT, DEFAULT),
(6, 'DRAFT', DEFAULT, DEFAULT),
(2, 'SUBMITTED', 388.87, 52.91), 
(3, 'SUBMITTED', 11.98, 1.63),
(7, 'SUBMITTED', 26.93, 3.66);

CREATE TABLE IF NOT EXISTS order_items (
	id SERIAL PRIMARY KEY,
	order_id INTEGER NOT NULL,
	product_id INTEGER NOT NULL,
	quantity SMALLINT NOT NULL CONSTRAINT valid_quantity CHECK(quantity >= 0),
	CONSTRAINT unique_order_item UNIQUE(order_id, product_id),
	CONSTRAINT fk_order_id_orders_id FOREIGN KEY(order_id) REFERENCES orders(id) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_product_id_productss_id FOREIGN KEY(product_id) REFERENCES products(id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO order_items(order_id, product_id, quantity) VALUES 
(1, 1, 12),
(1, 2, 8),
(2, 3, 9),
(2, 6, 2),
(8, 1, 12),
(8, 5, 1),
(9, 4, 2),
(10, 3, 5),
(10, 4, 2);