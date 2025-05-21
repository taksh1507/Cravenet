show databases;
create database cravenet_database;
use cravenet_database ;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    User_ID VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL
);
ALTER TABLE users
ADD COLUMN email VARCHAR(100) NOT NULL UNIQUE;

describe users;
select * from users;
TRUNCATE TABLE users;












