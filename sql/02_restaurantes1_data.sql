-- ============================================================
-- CRAVENET - Database 2: restaurantes1_data
-- Purpose : Restaurants, Dishes, Prices, Orders
-- Run this file SECOND in MySQL Workbench / command line
-- ============================================================

CREATE DATABASE IF NOT EXISTS restaurantes1_data;
USE restaurantes1_data;

-- ============================================================
-- DROP ALL TABLES (for clean re-run)
-- ============================================================
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS price_data;
DROP TABLE IF EXISTS restaurant_dish;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS restaurants;

-- ============================================================
-- TABLE: restaurants
-- ============================================================
CREATE TABLE restaurants (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- ============================================================
-- TABLE: dishes
-- ============================================================
CREATE TABLE dishes (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    image_path VARCHAR(255)
);

-- ============================================================
-- TABLE: restaurant_dish  (many-to-many junction)
-- ============================================================
CREATE TABLE restaurant_dish (
    restaurant_id INT NOT NULL,
    dish_id       INT NOT NULL,
    PRIMARY KEY (restaurant_id, dish_id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
    FOREIGN KEY (dish_id)       REFERENCES dishes(id)
);

-- ============================================================
-- TABLE: price_data
-- ============================================================
CREATE TABLE price_data (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    dish_id       INT            NOT NULL,
    restaurant_id INT            NOT NULL,
    swiggy_price  DECIMAL(10,2)  NOT NULL,
    zomato_price  DECIMAL(10,2)  NOT NULL,
    FOREIGN KEY (dish_id)       REFERENCES dishes(id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
    UNIQUE KEY uq_dish_restaurant (dish_id, restaurant_id)
);

-- ============================================================
-- TABLE: orders
-- ============================================================
CREATE TABLE orders (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    dish_name       VARCHAR(100) NOT NULL,
    restaurant_name VARCHAR(100) NOT NULL,
    platform        VARCHAR(50)  NOT NULL,
    order_time      DATETIME     NOT NULL DEFAULT NOW()
);

-- ============================================================
-- INSERT: Restaurants (36 total)
-- ============================================================
INSERT INTO restaurants (name) VALUES
('Dominos'),                        -- id 1
('Pizza Hut'),                      -- id 2
('Bhagwati Restaurant'),            -- id 3
('Burger King'),                    -- id 4
('Mc Donalds'),                     -- id 5
('The J'),                          -- id 6
('WTC'),                            -- id 7
('Chai Sutta Bar'),                 -- id 8
('Food Adda'),                      -- id 9
('East Asia'),                      -- id 10
('Flavours of China'),              -- id 11
('Banana Leaf'),                    -- id 12
('Persian Darbar'),                 -- id 13
('House of Biryani'),               -- id 14
('Amar Fast Food Restaurant'),      -- id 15
('Sattva Veg Restaurant'),          -- id 16
('Achija'),                         -- id 17
('Great Indian Khichdi by EatFit'), -- id 18
('Makhan Maar Ke'),                 -- id 19
('Mom Hand Kitchen'),               -- id 20
('Paratha Express'),                -- id 21
('Merwans Cake'),                   -- id 22
('Monginis Cake Shop'),             -- id 23
('Bombay Ice Cream'),               -- id 24
('Baskin Robbins'),                 -- id 25
('South Tiffin Box'),               -- id 26
('South Delight'),                  -- id 27
('Bombay Bytes'),                   -- id 28
('MM Mithaiwala'),                  -- id 29
('Jain Sweets'),                    -- id 30
('Cafe Coffee Day'),                -- id 31
('Subway'),                         -- id 32
('Frozen Bottle'),                  -- id 33
('Keventers'),                      -- id 34
('UFO Fries'),                      -- id 35
('SnakeyWakey');                    -- id 36

-- ============================================================
-- INSERT: Dishes (18 total)
-- image_path uses forward slashes; Java prefixes "file:" at runtime
-- NOTE: Update the base path below to YOUR machine's project path
-- ============================================================
SET @base = 'C:/Users/dell/.gemini/antigravity-ide/scratch/Cravenet/app/resources/dishes/';

INSERT INTO dishes (name, image_path) VALUES
('Pizza',       CONCAT(@base, 'pizza.jpg')),        -- id 1
('Burger',      CONCAT(@base, 'Burger.jpg')),       -- id 2
('Pasta',       CONCAT(@base, 'Pasta.jpg')),        -- id 3
('Noodles',     CONCAT(@base, 'noodles.jpg')),      -- id 4
('Biryani',     CONCAT(@base, 'biryani.jpg')),      -- id 5
('Pav Bhaji',   CONCAT(@base, 'PavBhaji.jpg')),     -- id 6
('Khichdi',     CONCAT(@base, 'khichdi.jpg')),      -- id 7
('Paratha',     CONCAT(@base, 'Paratha.jpg')),      -- id 8
('Cake',        CONCAT(@base, 'cake.jpg')),         -- id 9
('Ice Cream',   CONCAT(@base, 'Ice Cream.jpg')),    -- id 10
('Dosa',        CONCAT(@base, 'Dosa.jpg')),         -- id 11
('Vada Pav',    CONCAT(@base, 'Vada pav.jpg')),     -- id 12
('Idli',        CONCAT(@base, 'IDLI.jpg')),         -- id 13
('Sandwich',    CONCAT(@base, 'Sandwich.jpg')),     -- id 14
('Milkshake',   CONCAT(@base, 'Milkshakes.jpg')),   -- id 15
('Fries',       CONCAT(@base, 'Fries.jpg')),        -- id 16
('Juice',       CONCAT(@base, 'Juices.jpg')),       -- id 17
('Cold Drink',  CONCAT(@base, 'cold drink.jpg'));   -- id 18

-- ============================================================
-- INSERT: restaurant_dish (which restaurant serves which dish)
-- ============================================================

-- Pizza (dish_id = 1)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 1 FROM restaurants WHERE name IN ('Dominos', 'Pizza Hut', 'Bhagwati Restaurant');

-- Burger (dish_id = 2)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 2 FROM restaurants WHERE name IN ('Burger King', 'Mc Donalds', 'The J');

-- Pasta (dish_id = 3)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 3 FROM restaurants WHERE name IN ('WTC', 'Chai Sutta Bar', 'Food Adda');

-- Noodles (dish_id = 4)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 4 FROM restaurants WHERE name IN ('East Asia', 'Flavours of China', 'Bhagwati Restaurant');

-- Biryani (dish_id = 5)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 5 FROM restaurants WHERE name IN ('Banana Leaf', 'Persian Darbar', 'House of Biryani');

-- Pav Bhaji (dish_id = 6)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 6 FROM restaurants WHERE name IN ('Bhagwati Restaurant', 'Amar Fast Food Restaurant', 'Sattva Veg Restaurant');

-- Khichdi (dish_id = 7)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 7 FROM restaurants WHERE name IN ('Achija', 'Sattva Veg Restaurant', 'Great Indian Khichdi by EatFit');

-- Paratha (dish_id = 8)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 8 FROM restaurants WHERE name IN ('Makhan Maar Ke', 'Mom Hand Kitchen', 'Paratha Express');

-- Cake (dish_id = 9)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 9 FROM restaurants WHERE name IN ('Merwans Cake', 'Monginis Cake Shop');

-- Ice Cream (dish_id = 10)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 10 FROM restaurants WHERE name IN ('Bombay Ice Cream', 'Baskin Robbins', 'Bhagwati Restaurant');

-- Dosa (dish_id = 11)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 11 FROM restaurants WHERE name IN ('South Tiffin Box', 'South Delight', 'Sattva Veg Restaurant');

-- Vada Pav (dish_id = 12)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 12 FROM restaurants WHERE name IN ('Bombay Bytes', 'MM Mithaiwala', 'Jain Sweets');

-- Idli (dish_id = 13)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 13 FROM restaurants WHERE name IN ('South Tiffin Box', 'South Delight', 'Sattva Veg Restaurant');

-- Sandwich (dish_id = 14)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 14 FROM restaurants WHERE name IN ('Food Adda', 'Subway', 'Cafe Coffee Day');

-- Milkshake (dish_id = 15)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 15 FROM restaurants WHERE name IN ('Frozen Bottle', 'Keventers');

-- Fries (dish_id = 16)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 16 FROM restaurants WHERE name IN ('Mc Donalds', 'UFO Fries');

-- Juice (dish_id = 17)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 17 FROM restaurants WHERE name IN ('SnakeyWakey');

-- Cold Drink (dish_id = 18)
INSERT INTO restaurant_dish (restaurant_id, dish_id)
SELECT id, 18 FROM restaurants WHERE name IN ('Dominos', 'Pizza Hut', 'Mc Donalds', 'Burger King');

-- ============================================================
-- INSERT: price_data
-- Using subqueries so IDs do not need to be hardcoded
-- ============================================================

-- Pizza prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(1, (SELECT id FROM restaurants WHERE name='Dominos'),            450.00, 500.00),
(1, (SELECT id FROM restaurants WHERE name='Pizza Hut'),          400.00, 450.00),
(1, (SELECT id FROM restaurants WHERE name='Bhagwati Restaurant'),420.00, 470.00);

-- Burger prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(2, (SELECT id FROM restaurants WHERE name='Burger King'), 250.00, 300.00),
(2, (SELECT id FROM restaurants WHERE name='Mc Donalds'),  230.00, 280.00),
(2, (SELECT id FROM restaurants WHERE name='The J'),       240.00, 290.00);

-- Pasta prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(3, (SELECT id FROM restaurants WHERE name='WTC'),           350.00, 400.00),
(3, (SELECT id FROM restaurants WHERE name='Chai Sutta Bar'),330.00, 380.00),
(3, (SELECT id FROM restaurants WHERE name='Food Adda'),     340.00, 390.00);

-- Noodles prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(4, (SELECT id FROM restaurants WHERE name='East Asia'),           180.00, 200.00),
(4, (SELECT id FROM restaurants WHERE name='Flavours of China'),   160.00, 180.00),
(4, (SELECT id FROM restaurants WHERE name='Bhagwati Restaurant'), 170.00, 190.00);

-- Biryani prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(5, (SELECT id FROM restaurants WHERE name='Banana Leaf'),    400.00, 450.00),
(5, (SELECT id FROM restaurants WHERE name='Persian Darbar'), 420.00, 470.00),
(5, (SELECT id FROM restaurants WHERE name='House of Biryani'),380.00, 430.00);

-- Pav Bhaji prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(6, (SELECT id FROM restaurants WHERE name='Bhagwati Restaurant'),      100.00, 120.00),
(6, (SELECT id FROM restaurants WHERE name='Amar Fast Food Restaurant'),110.00, 130.00),
(6, (SELECT id FROM restaurants WHERE name='Sattva Veg Restaurant'),     90.00, 110.00);

-- Khichdi prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(7, (SELECT id FROM restaurants WHERE name='Achija'),                        150.00, 160.00),
(7, (SELECT id FROM restaurants WHERE name='Sattva Veg Restaurant'),         140.00, 150.00),
(7, (SELECT id FROM restaurants WHERE name='Great Indian Khichdi by EatFit'),130.00, 145.00);

-- Paratha prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(8, (SELECT id FROM restaurants WHERE name='Makhan Maar Ke'),  120.00, 130.00),
(8, (SELECT id FROM restaurants WHERE name='Mom Hand Kitchen'),110.00, 120.00),
(8, (SELECT id FROM restaurants WHERE name='Paratha Express'), 115.00, 125.00);

-- Cake prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(9, (SELECT id FROM restaurants WHERE name='Merwans Cake'),     200.00, 220.00),
(9, (SELECT id FROM restaurants WHERE name='Monginis Cake Shop'),210.00, 230.00);

-- Ice Cream prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(10, (SELECT id FROM restaurants WHERE name='Bombay Ice Cream'),    100.00, 110.00),
(10, (SELECT id FROM restaurants WHERE name='Baskin Robbins'),       90.00, 100.00),
(10, (SELECT id FROM restaurants WHERE name='Bhagwati Restaurant'),  95.00, 105.00);

-- Dosa prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(11, (SELECT id FROM restaurants WHERE name='South Tiffin Box'),    80.00,  90.00),
(11, (SELECT id FROM restaurants WHERE name='South Delight'),        75.00,  85.00),
(11, (SELECT id FROM restaurants WHERE name='Sattva Veg Restaurant'),70.00,  80.00);

-- Vada Pav prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(12, (SELECT id FROM restaurants WHERE name='Bombay Bytes'),  50.00, 60.00),
(12, (SELECT id FROM restaurants WHERE name='MM Mithaiwala'), 45.00, 55.00),
(12, (SELECT id FROM restaurants WHERE name='Jain Sweets'),   40.00, 50.00);

-- Idli prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(13, (SELECT id FROM restaurants WHERE name='South Tiffin Box'),    60.00, 70.00),
(13, (SELECT id FROM restaurants WHERE name='South Delight'),        55.00, 65.00),
(13, (SELECT id FROM restaurants WHERE name='Sattva Veg Restaurant'),50.00, 60.00);

-- Sandwich prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(14, (SELECT id FROM restaurants WHERE name='Food Adda'),      120.00, 130.00),
(14, (SELECT id FROM restaurants WHERE name='Subway'),         115.00, 125.00),
(14, (SELECT id FROM restaurants WHERE name='Cafe Coffee Day'),130.00, 145.00);

-- Milkshake prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(15, (SELECT id FROM restaurants WHERE name='Frozen Bottle'), 150.00, 160.00),
(15, (SELECT id FROM restaurants WHERE name='Keventers'),     140.00, 150.00);

-- Fries prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(16, (SELECT id FROM restaurants WHERE name='Mc Donalds'), 80.00, 90.00),
(16, (SELECT id FROM restaurants WHERE name='UFO Fries'),  70.00, 80.00);

-- Juice prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(17, (SELECT id FROM restaurants WHERE name='SnakeyWakey'), 60.00, 70.00);

-- Cold Drink prices
INSERT INTO price_data (dish_id, restaurant_id, swiggy_price, zomato_price) VALUES
(18, (SELECT id FROM restaurants WHERE name='Dominos'),     50.00, 60.00),
(18, (SELECT id FROM restaurants WHERE name='Pizza Hut'),   45.00, 55.00),
(18, (SELECT id FROM restaurants WHERE name='Mc Donalds'),  40.00, 50.00),
(18, (SELECT id FROM restaurants WHERE name='Burger King'), 42.00, 52.00);

SELECT 'restaurantes1_data setup complete.' AS status;
SELECT CONCAT('Restaurants: ', COUNT(*)) AS info FROM restaurants;
SELECT CONCAT('Dishes: ',      COUNT(*)) AS info FROM dishes;
SELECT CONCAT('Price entries: ',COUNT(*)) AS info FROM price_data;
