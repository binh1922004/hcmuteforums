-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hcmuteforums
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `topic_images`
--

DROP TABLE IF EXISTS `topic_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_images` (
  `id` varchar(255) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `topic_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh4itm5lb2uho9pvj6wgbq8w4b` (`topic_id`),
  CONSTRAINT `FKh4itm5lb2uho9pvj6wgbq8w4b` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_images`
--

LOCK TABLES `topic_images` WRITE;
/*!40000 ALTER TABLE `topic_images` DISABLE KEYS */;
INSERT INTO `topic_images` VALUES ('163ce280-d60a-4e63-b0e3-33b9f198081a','upload/topics/4fc3c2ad-65ea-47aa-b8ee-8106616e28a5-2b5c8587-819a-4324-b4b1-36b7589bd1df.jpg','4fc3c2ad-65ea-47aa-b8ee-8106616e28a5'),('1b31f453-88b6-4a5a-9cae-ff5db365fe07','upload/topics/a04c16da-15ac-422b-8c65-d62263cf57fb-0844ad92-8a1b-460b-95a9-ef34f84bf6b6.jpg','a04c16da-15ac-422b-8c65-d62263cf57fb'),('1dc7410f-a8a3-462c-8668-b9cb408d5fea','upload/topics/17fff9ba-a755-4af9-b163-54d2d53d0c14-21d63da9-9eda-4da3-8451-0cf8ea15f148.jpg','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('7b49b881-baf2-4dc3-999d-44e61112daa3','upload/topics/4fc3c2ad-65ea-47aa-b8ee-8106616e28a5-bc435204-f040-447f-85b1-2ecde9425051.jpg','4fc3c2ad-65ea-47aa-b8ee-8106616e28a5'),('c7ce672a-4825-492f-909f-f50c34470908','upload/topics/17fff9ba-a755-4af9-b163-54d2d53d0c14-df1abaef-2330-4910-a8f4-29d1b791b27d.jpg','17fff9ba-a755-4af9-b163-54d2d53d0c14');
/*!40000 ALTER TABLE `topic_images` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-16  0:29:51
