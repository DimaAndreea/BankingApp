# Java Banking App

A simple Java banking management system built for the **OOP2 university course**.  
The project follows solid OOP principles, implements data persistence using JDBC,
and adopts a modular, service-based architecture.

---

## Features
- Manage **clients** (create, update, delete, login)
- Manage **accounts** (checking & savings with interest)
- Perform **money transfers** between accounts
- View **transaction history** for each account
- **Audit logging** in CSV for every performed action
- **PostgreSQL persistence** via JDBC

---

## Models List

1. **User** – Base class for system users (id, username, password)
2. **Admin** – Administrator with elevated privileges
3. **Client** – Represents a bank client with name, email, phone, and address
4. **Address** – Embedded value object for client address data
5. **Account** – Represents a standard bank account
6. **SavingsAccount** – Specialized account with interest rate
7. **Transaction** – Represents a transfer between two accounts
8. **AccountRequest** – Represents a pending account creation request (admin side)

---

## Actions List

1. **create_client** – Registers a new client
2. **login_client** – Authenticates a client using email and password
3. **create_account** – Creates a new checking or savings account
4. **get_accounts_by_client_id** – Lists all accounts for a specific client
5. **update_balance** – Deposits or withdraws funds from an account
6. **transfer** – Transfers money between two accounts
7. **get_transactions_by_account** – Displays transaction history for an account
8. **delete_account** – Removes an account from the system
9. **delete_client** – Deletes a client and their accounts
10. **apply_interest_savings** – Applies interest to all savings accounts
11. **update_admin** – Updates admin credentials
12. **update_client** – Updates client info from admin panel

Each action is logged automatically in the `audit.csv` file with timestamp and action name.

---

## Technologies Used
- Java 21
- PostgreSQL
- JDBC
- IntelliJ IDEA
- Docker for database containerization