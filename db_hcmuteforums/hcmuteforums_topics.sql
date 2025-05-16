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
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topics` (
  `id` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoc3papwmjontq89fcia02ag1h` (`user_id`),
  CONSTRAINT `FKoc3papwmjontq89fcia02ag1h` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics`
--

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;
INSERT INTO `topics` VALUES ('17fff9ba-a755-4af9-b163-54d2d53d0c14','','2025-05-14 21:40:50.536000','Chao cac cau','9d40a8e6-4667-4d9b-b315-d20f903ca3d5'),('2e9f7c1b-7031-4965-a34c-53745482eae3','Huhu','2025-05-06 00:44:15.574000','HU','b689a10b-5559-47f3-92b9-612a447f589e'),('4fc3c2ad-65ea-47aa-b8ee-8106616e28a5','Chao cac cau','2025-05-06 00:45:33.850000','Hello','b689a10b-5559-47f3-92b9-612a447f589e'),('76e3afa8-55bd-4d11-8dc7-bc7ed5d7f35e','Ngủ nghê gì chưa người đẹp','2025-05-14 22:48:24.709000','Tôi cảm thấy buồn ngủ','b689a10b-5559-47f3-92b9-612a447f589e'),('a04c16da-15ac-422b-8c65-d62263cf57fb','Cần tìm bạn gái (Muốn có bồ quá đi)','2025-05-14 21:43:15.397000','Hello anh em tui là Hào Sữa','227f66a9-5caf-49f4-bf9c-76ed38cb63b8'),('febe3307-6e18-415b-80dc-55d7fdd29b7a','Chao may cung','2025-05-06 00:40:36.979000','Hello','b689a10b-5559-47f3-92b9-612a447f589e');
/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
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
