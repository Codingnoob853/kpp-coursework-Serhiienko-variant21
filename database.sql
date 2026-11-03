-- 1. Видалення та повторне створення бази даних (для повторюваності)
DROP DATABASE IF EXISTS delivery_db;
CREATE DATABASE delivery_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE delivery_db;

-- 2. Створення незалежних (батьківських) таблиць

-- Таблиця клієнтів
CREATE TABLE client (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        full_name VARCHAR(100) NOT NULL,
                        phone VARCHAR(20) NOT NULL,
                        email VARCHAR(100),
                        address VARCHAR(255)
);

-- Таблиця відправлень (посилок)
CREATE TABLE shipment (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          tracking_number VARCHAR(50) NOT NULL,
                          weight DOUBLE NOT NULL,
                          declared_value DOUBLE NOT NULL,
                          description VARCHAR(255)
);

-- Таблиця кур'єрів
CREATE TABLE courier (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         full_name VARCHAR(100) NOT NULL,
                         phone VARCHAR(20) NOT NULL,
                         vehicle_type VARCHAR(50),
                         status VARCHAR(30)
);

-- 3. Створення залежної таблиці замовлень (delivery_order) з підтримкою інтегрального показника та життєвого циклу
CREATE TABLE delivery_order (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                shipment_id BIGINT NOT NULL,
                                client_id BIGINT NOT NULL,
                                courier_id BIGINT NOT NULL,
                                order_date VARCHAR(50) NOT NULL,
                                status VARCHAR(30) NOT NULL,
                                total_cost DOUBLE NOT NULL,
                                f1 DOUBLE NOT NULL,
                                f2 DOUBLE NOT NULL,
                                f3 DOUBLE NOT NULL,
                                risk_index DOUBLE NOT NULL,
                                FOREIGN KEY (shipment_id) REFERENCES shipment(id),
                                FOREIGN KEY (client_id) REFERENCES client(id),
                                FOREIGN KEY (courier_id) REFERENCES courier(id)
);

-- 4. Початкові демонстраційні записи (INSERT INTO)

-- Додаємо клієнтів
INSERT INTO client (full_name, phone, email, address) VALUES
                                                          ('Коваленко Олексій Іванович', '+380501112233', 'oleksiy@gmail.com', 'м. Полтава, вул. Соборності, 15'),
                                                          ('Шевченко Марія Сергіївна', '+380974445566', 'mariya@gmail.com', 'м. Полтава, вул. Європейська, 42');

-- Додаємо відправлення
INSERT INTO shipment (tracking_number, weight, declared_value, description) VALUES
                                                                                ('TRK-1001-UA', 2.5, 1500.0, 'Ноутбучні аксесуари'),
                                                                                ('TRK-1002-UA', 0.8, 550.0, 'Документи та канцелярські товари');

-- Додаємо кур'єрів
INSERT INTO courier (full_name, phone, vehicle_type, status) VALUES
                                                                 ('Мельник Дмитро Володимирович', '+380637778899', 'Піший (Bolt Food)', 'АКТИВНИЙ'),
                                                                 ('Бондаренко Андрій Петрович', '+380509990011', 'Електросамокат', 'АКТИВНИЙ');

-- Додаємо замовлення доставок (з урахуванням розрахованих факторів та інтегрального показника)
INSERT INTO delivery_order (shipment_id, client_id, courier_id, order_date, status, total_cost, f1, f2, f3, risk_index) VALUES
                                                                                                                            (1, 1, 1, '2026-11-01 12:00:00', 'CREATED', 1500.0, 52.0, 60.0, 70.0, 59.3),
                                                                                                                            (2, 2, 2, '2026-11-01 14:00:00', 'CREATED', 550.0, 30.0, 40.0, 50.0, 39.5);