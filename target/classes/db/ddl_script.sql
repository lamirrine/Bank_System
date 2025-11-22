CREATE DATABASE  IF NOT EXISTS `bancodigital_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bancodigital_db`;
-- MySQL dump 10.13  Distrib 8.0.38, for Linux (x86_64)
--
-- Host: localhost    Database: bancodigital_db
-- ------------------------------------------------------
-- Server version	8.4.6-0ubuntu3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `account_number` varchar(20) NOT NULL,
  `account_type` enum('CORRENTE','POUPANCA') NOT NULL,
  `balance` decimal(15,2) NOT NULL DEFAULT '0.00',
  `open_date` datetime NOT NULL,
  `close_date` datetime DEFAULT NULL,
  `status` enum('ATIVA','SUSPENSA','FECHADA') NOT NULL,
  `owner_customer_id` int NOT NULL,
  `agency_id` int NOT NULL,
  `daily_withdraw_limit` decimal(15,2) NOT NULL,
  `daily_transfer_limit` decimal(15,2) NOT NULL,
  `transaction_pin_hash` varchar(255) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_number` (`account_number`),
  KEY `owner_customer_id` (`owner_customer_id`),
  KEY `agency_id` (`agency_id`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`owner_customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `account_ibfk_2` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`agency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'12345678','CORRENTE',9500.00,'2025-11-01 15:15:13',NULL,'ATIVA',9,1,15000.00,15000.00,'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ='),(2,'12345-6','CORRENTE',51500.00,'2025-11-10 06:57:37',NULL,'ATIVA',9,1,3250.00,10000.00,'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ='),(3,'21480626','CORRENTE',4100.00,'2025-11-12 16:43:33',NULL,'ATIVA',10,1,3250.00,10000.00,'wfMw0K/zHByHQD8eQ0e8whr/fBeZCHI1NfKzFyNwJSU='),(4,'76442569','POUPANCA',4500.00,'2025-11-12 18:02:09',NULL,'ATIVA',10,1,5000.00,20000.00,'wfMw0K/zHByHQD8eQ0e8whr/fBeZCHI1NfKzFyNwJSU='),(5,'42671846','POUPANCA',2500.00,'2025-11-14 12:40:11',NULL,'ATIVA',9,1,5000.00,20000.00,'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ='),(6,'67250864','CORRENTE',3000.00,'2025-11-15 12:42:36',NULL,'ATIVA',13,1,3250.00,10000.00,'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ='),(7,'59835530','POUPANCA',0.00,'2025-11-15 12:52:51',NULL,'ATIVA',9,1,5000.00,20000.00,'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agency`
--

DROP TABLE IF EXISTS `agency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agency` (
  `agency_id` int NOT NULL AUTO_INCREMENT,
  `bank_code` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `manager_employee_id` int DEFAULT NULL,
  `open_time` time DEFAULT NULL,
  `close_time` time DEFAULT NULL,
  PRIMARY KEY (`agency_id`),
  KEY `manager_employee_id` (`manager_employee_id`),
  CONSTRAINT `agency_ibfk_1` FOREIGN KEY (`manager_employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agency`
--

LOCK TABLES `agency` WRITE;
/*!40000 ALTER TABLE `agency` DISABLE KEYS */;
INSERT INTO `agency` VALUES (1,'1','gaza','gaza','33322',1,'15:15:13',NULL);
/*!40000 ALTER TABLE `agency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank`
--

DROP TABLE IF EXISTS `bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank` (
  `bank_code` varchar(10) NOT NULL,
  `bank_name` varchar(100) NOT NULL,
  `transfer_fee_rate` decimal(5,4) NOT NULL,
  `support_phone` varchar(20) DEFAULT NULL,
  `emergency_phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`bank_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank`
--

LOCK TABLES `bank` WRITE;
/*!40000 ALTER TABLE `bank` DISABLE KEYS */;
INSERT INTO `bank` VALUES ('1','Macia',2.0000,'11122','11222');
/*!40000 ALTER TABLE `bank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` int NOT NULL,
  `bi_number` varchar(20) DEFAULT NULL,
  `nuit` varchar(10) DEFAULT NULL,
  `passport_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `bi_number` (`bi_number`),
  UNIQUE KEY `nuit` (`nuit`),
  UNIQUE KEY `passport_number` (`passport_number`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (5,'999999999','000000000','P12345678'),(9,'123456789LA','123456789','AB123456'),(10,'12345678','123456','12345678'),(13,'12324555','111226555','111222');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `employee_id` int NOT NULL,
  `access_level` enum('STAFF','MANAGER','ADMIN') NOT NULL,
  `is_supervisor` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`employee_id`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'MANAGER',1),(11,'ADMIN',1),(14,'STAFF',0);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `type` enum('DEPOSITO','LEVANTAMENTO','TRANSFERENCIA','PAGAMENTO') NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `timestamp` datetime NOT NULL,
  `status` enum('CONCLUIDA','PENDENTE','REJEITADA') NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `source_account_id` int NOT NULL,
  `destination_account_id` int DEFAULT NULL,
  `resulting_balance` decimal(15,2) NOT NULL,
  `fee_amount` decimal(15,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`transaction_id`),
  KEY `source_account_id` (`source_account_id`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`source_account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,'PAGAMENTO',3000.00,'2025-11-01 15:15:13','CONCLUIDA',NULL,1,NULL,40000.00,0.00),(2,'DEPOSITO',5000.00,'2025-11-10 07:00:30','CONCLUIDA','Depósito inicial',2,NULL,5000.00,0.00),(3,'LEVANTAMENTO',-500.00,'2025-11-10 07:00:30','CONCLUIDA','Levantamento ATM',2,NULL,4500.00,0.00),(4,'DEPOSITO',500.00,'2025-11-10 12:47:57','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,18500.00,0.00),(5,'DEPOSITO',500.00,'2025-11-10 12:49:01','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,19000.00,0.00),(6,'DEPOSITO',1000.00,'2025-11-10 12:49:32','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,20000.00,0.00),(7,'DEPOSITO',1000.00,'2025-11-10 12:51:06','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,21000.00,0.00),(8,'DEPOSITO',2000.00,'2025-11-10 12:51:47','CONCLUIDA','Depósito em conta - Disponível imediatamente',2,0,29000.00,0.00),(9,'DEPOSITO',500.00,'2025-11-10 16:05:23','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,21500.00,0.00),(10,'LEVANTAMENTO',1000.00,'2025-11-10 16:33:14','CONCLUIDA','Levantamento em balcão/ATM',1,0,20500.00,0.00),(11,'LEVANTAMENTO',2000.00,'2025-11-10 16:33:56','CONCLUIDA','Levantamento em balcão/ATM',1,0,18500.00,0.00),(12,'LEVANTAMENTO',5000.00,'2025-11-10 16:35:38','CONCLUIDA','Levantamento em balcão/ATM',1,0,13500.00,0.00),(13,'TRANSFERENCIA',4000.00,'2025-11-10 17:30:53','CONCLUIDA','Transferência enviada para 12345678',2,1,25000.00,0.00),(14,'TRANSFERENCIA',4000.00,'2025-11-10 17:30:54','CONCLUIDA','Transferência recebida de 12345-6',1,2,17500.00,0.00),(15,'TRANSFERENCIA',2000.00,'2025-11-10 19:20:43','CONCLUIDA','Transferência enviada para 12345678',2,1,23000.00,0.00),(16,'TRANSFERENCIA',2000.00,'2025-11-10 19:20:45','CONCLUIDA','Transferência recebida de 12345-6',1,2,19500.00,0.00),(17,'LEVANTAMENTO',2000.00,'2025-11-10 19:21:16','CONCLUIDA','Levantamento em balcão/ATM',1,0,17500.00,0.00),(18,'DEPOSITO',1000.00,'2025-11-10 19:21:38','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,18500.00,0.00),(19,'TRANSFERENCIA',2000.00,'2025-11-10 19:42:31','CONCLUIDA','Transferência enviada para 12345-6',1,2,16500.00,0.00),(20,'TRANSFERENCIA',2000.00,'2025-11-10 19:42:32','CONCLUIDA','Transferência recebida de 12345678',2,1,25000.00,0.00),(21,'LEVANTAMENTO',2000.00,'2025-11-10 19:43:02','CONCLUIDA','Levantamento em balcão/ATM',1,0,14500.00,0.00),(22,'DEPOSITO',5000.00,'2025-11-10 19:43:30','CONCLUIDA','Depósito em conta - Disponível imediatamente',2,0,30000.00,0.00),(23,'DEPOSITO',10000.00,'2025-11-10 21:09:15','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,24500.00,0.00),(24,'DEPOSITO',2000.00,'2025-11-11 00:25:35','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,26500.00,0.00),(25,'DEPOSITO',3500.00,'2025-11-11 00:27:20','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,30000.00,0.00),(26,'DEPOSITO',2000.00,'2025-11-11 00:50:39','CONCLUIDA','Depósito em conta - Disponível imediatamente',2,0,32000.00,0.00),(27,'LEVANTAMENTO',3000.00,'2025-11-11 01:48:34','CONCLUIDA','Levantamento em balcão/ATM',1,0,27000.00,0.00),(28,'LEVANTAMENTO',5000.00,'2025-11-11 01:50:01','CONCLUIDA','Levantamento em balcão/ATM',1,0,22000.00,0.00),(29,'LEVANTAMENTO',5000.00,'2025-11-11 01:50:25','CONCLUIDA','Levantamento em balcão/ATM',1,0,17000.00,0.00),(30,'LEVANTAMENTO',5000.00,'2025-11-11 01:50:58','CONCLUIDA','Levantamento em balcão/ATM',1,0,12000.00,0.00),(31,'TRANSFERENCIA',5000.00,'2025-11-11 01:51:51','CONCLUIDA','Transferência enviada para 12345678',1,1,7000.00,0.00),(32,'TRANSFERENCIA',5000.00,'2025-11-11 01:51:51','CONCLUIDA','Transferência recebida de 12345678',1,1,17000.00,0.00),(33,'TRANSFERENCIA',10000.00,'2025-11-11 01:52:53','CONCLUIDA','Transferência enviada para 12345678',1,1,7000.00,0.00),(34,'TRANSFERENCIA',10000.00,'2025-11-11 01:52:54','CONCLUIDA','Transferência recebida de 12345678',1,1,27000.00,0.00),(35,'TRANSFERENCIA',10000.00,'2025-11-11 01:54:02','CONCLUIDA','Transferência enviada para 12345-6',1,2,17000.00,0.00),(36,'TRANSFERENCIA',10000.00,'2025-11-11 01:54:02','CONCLUIDA','Transferência recebida de 12345678',2,1,42000.00,0.00),(37,'TRANSFERENCIA',10000.00,'2025-11-11 01:54:36','CONCLUIDA','Transferência enviada para 12345-6',2,2,32000.00,0.00),(38,'TRANSFERENCIA',10000.00,'2025-11-11 01:54:36','CONCLUIDA','Transferência recebida de 12345-6',2,2,52000.00,0.00),(39,'LEVANTAMENTO',5000.00,'2025-11-11 01:55:00','CONCLUIDA','Levantamento em balcão/ATM',1,0,12000.00,0.00),(40,'LEVANTAMENTO',5000.00,'2025-11-11 01:55:16','CONCLUIDA','Levantamento em balcão/ATM',1,0,7000.00,0.00),(41,'LEVANTAMENTO',5000.00,'2025-11-11 01:55:39','CONCLUIDA','Levantamento em balcão/ATM',1,0,2000.00,0.00),(42,'TRANSFERENCIA',2000.00,'2025-11-11 01:56:38','CONCLUIDA','Transferência enviada para 12345-6',1,2,0.00,0.00),(43,'TRANSFERENCIA',2000.00,'2025-11-11 01:56:38','CONCLUIDA','Transferência recebida de 12345678',2,1,54000.00,0.00),(44,'LEVANTAMENTO',2000.00,'2025-11-11 11:00:09','CONCLUIDA','Levantamento em balcão/ATM',2,0,52000.00,0.00),(45,'TRANSFERENCIA',5000.00,'2025-11-11 11:01:04','CONCLUIDA','Transferência enviada para 12345678',2,1,47000.00,0.00),(46,'TRANSFERENCIA',5000.00,'2025-11-11 11:01:04','CONCLUIDA','Transferência recebida de 12345-6',1,2,5000.00,0.00),(47,'LEVANTAMENTO',2000.00,'2025-11-11 11:02:44','CONCLUIDA','Levantamento em balcão/ATM',1,0,3000.00,0.00),(48,'DEPOSITO',5000.00,'2025-11-11 13:22:07','CONCLUIDA','Depósito em conta - Disponível imediatamente',1,0,8000.00,0.00),(49,'LEVANTAMENTO',5000.00,'2025-11-11 13:23:18','CONCLUIDA','Levantamento em balcão/ATM',1,0,3000.00,0.00),(50,'TRANSFERENCIA',10000.00,'2025-11-11 16:24:08','CONCLUIDA','Transferência enviada para 12345-6',2,2,37000.00,0.00),(51,'TRANSFERENCIA',10000.00,'2025-11-11 16:24:09','CONCLUIDA','Transferência recebida de 12345-6',2,2,57000.00,0.00),(52,'TRANSFERENCIA',1000.00,'2025-11-11 16:25:26','CONCLUIDA','Transferência enviada para 12345-6',2,2,56000.00,0.00),(53,'TRANSFERENCIA',1000.00,'2025-11-11 16:25:26','CONCLUIDA','Transferência recebida de 12345-6',2,2,58000.00,0.00),(54,'TRANSFERENCIA',5000.00,'2025-11-11 16:26:06','CONCLUIDA','Transferência enviada para 12345678',2,1,53000.00,0.00),(55,'TRANSFERENCIA',5000.00,'2025-11-11 16:26:06','CONCLUIDA','Transferência recebida de 12345-6',1,2,8000.00,0.00),(56,'LEVANTAMENTO',2000.00,'2025-11-11 16:26:41','CONCLUIDA','Levantamento em balcão/ATM',2,0,51000.00,0.00),(57,'LEVANTAMENTO',1000.00,'2025-11-11 16:27:16','CONCLUIDA','Levantamento em balcão/ATM',1,0,7000.00,0.00),(58,'DEPOSITO',5000.00,'2025-11-12 16:45:09','CONCLUIDA','Depósito em conta - Disponível imediatamente',3,0,5000.00,0.00),(59,'LEVANTAMENTO',200.00,'2025-11-12 16:46:30','CONCLUIDA','Levantamento em balcão/ATM',3,0,4800.00,0.00),(60,'TRANSFERENCIA',500.00,'2025-11-12 16:47:56','CONCLUIDA','Transferência enviada para 12345-6',3,2,4300.00,0.00),(61,'TRANSFERENCIA',500.00,'2025-11-12 16:47:56','CONCLUIDA','Transferência recebida de 21480626',2,3,51500.00,0.00),(62,'DEPOSITO',5000.00,'2025-11-12 18:02:41','CONCLUIDA','Depósito em conta - Disponível imediatamente',4,0,5000.00,0.00),(63,'LEVANTAMENTO',200.00,'2025-11-12 18:32:13','CONCLUIDA','Levantamento em balcão/ATM',3,0,4100.00,0.00),(64,'LEVANTAMENTO',500.00,'2025-11-12 18:32:34','CONCLUIDA','Levantamento em balcão/ATM',4,0,4500.00,0.00),(65,'DEPOSITO',5000.00,'2025-11-14 12:42:05','CONCLUIDA','Depósito em conta - Disponível imediatamente',5,0,5000.00,0.00),(66,'DEPOSITO',5000.00,'2025-11-15 12:44:03','CONCLUIDA','Depósito em conta - Disponível imediatamente',6,0,5000.00,0.00),(67,'LEVANTAMENTO',2000.00,'2025-11-15 12:45:35','CONCLUIDA','Levantamento em balcão/ATM',6,0,3000.00,0.00),(68,'TRANSFERENCIA',2500.00,'2025-11-15 12:49:05','CONCLUIDA','Transferência enviada para 12345678',5,1,2500.00,0.00),(69,'TRANSFERENCIA',2500.00,'2025-11-15 12:49:05','CONCLUIDA','Transferência recebida de 42671846',1,5,9500.00,0.00);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `pass_hash` varchar(255) NOT NULL,
  `registration_date` datetime NOT NULL,
  `user_type` enum('CUSTOMER','EMPLOYEE') NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Lewis','MIrrine','lewis@email.com','847835732','Maputo','A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=','2025-11-01 15:15:13','EMPLOYEE'),(4,'Joao','Silva','joao.silva@banco.com','841234567',NULL,'20gQG7R8y/2R0vX6jJ1UqE7/w7/u7+9y4V8H2nQzW/0=','2025-10-31 09:24:22','CUSTOMER'),(5,'Cliente','Teste','cliente.teste.novo@banco.mz','849876543','Avenida da Liberdade, 10','kRLRQqHgsl2cYzItE8CENHxN7XMAprvY1JcAxDU4Fvg=','2025-10-31 10:33:25','CUSTOMER'),(9,'João','Alves','joao@email.com','841234567','Maputo','jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=','2025-11-01 15:15:13','CUSTOMER'),(10,'Luis','Mirrine','lui@email.com','85031028','Maputo','neZZm+3uRD2G9nKssGSxjPyRRffrx1M7mRL82eBkIZE=','2025-11-12 16:43:32','CUSTOMER'),(11,'Admin','Sistema','admin@banco.com','+258841234567','Matola','A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=','2025-11-15 00:43:06','EMPLOYEE'),(13,'Alex','Sitoie','eu@email.com','84788953','','jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=','2025-11-15 12:42:35','CUSTOMER'),(14,'Lewis','Sobrenome','lewis@empresa.com','123456789','Endereço','VaXp54IHtN+GmdYIhvoHAHlGNUewldGgW8cZu05s0lE=','2025-11-19 21:59:26','EMPLOYEE');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-19 22:45:20
