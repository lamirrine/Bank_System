-- SCRIPT DDL (Data Definition Language) para criação das tabelas no MySQL

-- Tabela 1: User (Base para Cliente e Funcionário)
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    pass_hash VARCHAR(255) NOT NULL, -- Senha criptografada
    registration_date DATETIME NOT NULL,
    user_type ENUM('CUSTOMER', 'EMPLOYEE') NOT NULL
);

-- Tabela 2: Customer (Herda de User)
CREATE TABLE customer (
    customer_id INT PRIMARY KEY,
    bi_number VARCHAR(20) UNIQUE,
    nuit VARCHAR(10) UNIQUE,
    passport_number VARCHAR(20) UNIQUE,
    FOREIGN KEY (customer_id) REFERENCES user(user_id)
);

-- Tabela 3: Employee (Herda de User)
CREATE TABLE employee (
    employee_id INT PRIMARY KEY,
    access_level ENUM('STAFF', 'MANAGER', 'ADMIN') NOT NULL,
    is_supervisor BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (employee_id) REFERENCES user(user_id)
);

-- Tabela 4: Agency
CREATE TABLE agency (
    agency_id INT PRIMARY KEY AUTO_INCREMENT,
    bank_code VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    manager_employee_id INT,
    open_time TIME,
    close_time TIME,
    FOREIGN KEY (manager_employee_id) REFERENCES employee(employee_id)
);

-- Tabela 5: Account
CREATE TABLE account (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type ENUM('CORRENTE', 'POUPANCA') NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    open_date DATETIME NOT NULL,
    close_date DATETIME,
    status ENUM('ATIVA', 'SUSPENSA', 'FECHADA') NOT NULL,
    owner_customer_id INT NOT NULL,
    agency_id INT NOT NULL,
    daily_withdraw_limit DECIMAL(15, 2) NOT NULL,
    daily_transfer_limit DECIMAL(15, 2) NOT NULL,
    transaction_pin_hash VARCHAR(255) NOT NULL, -- PIN de transação criptografado
    FOREIGN KEY (owner_customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (agency_id) REFERENCES agency(agency_id)
);

-- Tabela 6: Transaction
CREATE TABLE transaction (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('DEPOSITO', 'LEVANTAMENTO', 'TRANSFERENCIA', 'PAGAMENTO') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    timestamp DATETIME NOT NULL,
    status ENUM('CONCLUIDA', 'PENDENTE', 'REJEITADA') NOT NULL,
    description VARCHAR(255),
    source_account_id INT NOT NULL,
    destination_account_id INT,
    resulting_balance DECIMAL(15, 2) NOT NULL,
    fee_amount DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (source_account_id) REFERENCES account(account_id)
);

-- Tabela 7: Bank (Tabela de Configurações Globais)
CREATE TABLE bank (
    bank_code VARCHAR(10) PRIMARY KEY,
    bank_name VARCHAR(100) NOT NULL,
    transfer_fee_rate DECIMAL(5, 4) NOT NULL,
    support_phone VARCHAR(20),
    emergency_phone VARCHAR(20)
);