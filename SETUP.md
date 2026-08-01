# Cravenet - Setup Guide

## Step 1: Set Up MySQL Databases

Run these two SQL files **in order** in MySQL Workbench or the MySQL command line:

### 1a. Login/Registration Database
```
File: cravenet_database.sql
```
This creates `cravenet_database` with the `users` table.

### 1b. Restaurant/Dish/Price Database
```
File: restaurantes1_data.sql
```
This creates `restaurantes1_data` with ALL tables:
- `restaurants` (36 restaurants)
- `dishes` (18 dishes with image paths)
- `restaurant_dish` (which dish each restaurant serves)
- `price_data` (Swiggy + Zomato prices for every dish-restaurant pair)
- `orders` (stores placed orders)

> **Note:** After running `restaurantes1_data.sql`, the `dishes.image_path` column will contain paths to your machine (`C:/Users/dell/...`). This is automatically set by the `@base` variable at the top of the file — no manual edit needed.

---

## Step 2: Configure JavaFX in Your IDE (Eclipse/IntelliJ)

Use these VM arguments (from `javafxvm argument.txt`):
```
--module-path "C:\Program Files\Java\openjfx-23_windows-x64_bin-sdk (1)\javafx-sdk-23\lib" --add-modules javafx.controls,javafx.graphics,javafx.fxml
```
Adjust the path to wherever your JavaFX SDK is installed.

---

## Step 3: Run the App

Run `CravenetMain.java` as the main class.

**Login flow:**
1. Register a new account (User ID + Password + Email)
2. Enter OTP sent to your email *(requires Gmail App Password in `Registration.java`)*
3. Login → Select Location (Mumbai) → Search → Browse Dishes or Restaurants

---

## Step 4 (Optional): Configure OTP Email

In `Registration.java`, fill in real credentials:
```java
final String username = "your.gmail@gmail.com";
final String password = "your-16-char-app-password";
```
Get an App Password from: https://myaccount.google.com/apppasswords

---

## Database Credentials (default)

| Setting | Value |
|---------|-------|
| Host | localhost:3306 |
| Username | root |
| Password | 518815 |

To change the password, update it in all Java files that contain `518815`.

---

## Project Structure

```
Cravenet/
├── cravenet_database.sql       ← NEW: User auth database
├── restaurantes1_data.sql      ← NEW: Restaurant/dish/price/orders database
└── Cravenet/
    ├── Location Image/          ← App images (logo, dish photos, icons)
    ├── Restaurant/              ← Restaurant photos + swiggy/zomato logos
    ├── icons/                   ← Eye open/closed icons for password field
    └── Cravenet/
        └── src/Cravenet/        ← All Java source files
```
