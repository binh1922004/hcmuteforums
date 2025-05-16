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
-- Table structure for table `replies`
--

DROP TABLE IF EXISTS `replies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `replies` (
  `id` varchar(255) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `parent_reply_id` varchar(255) DEFAULT NULL,
  `topic_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `target_user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlej8qoe1wpmuh15xsdsyhl70m` (`topic_id`),
  KEY `FKn60t7po8l0rllye52xx25q4xx` (`user_id`),
  CONSTRAINT `FKlej8qoe1wpmuh15xsdsyhl70m` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`),
  CONSTRAINT `FKn60t7po8l0rllye52xx25q4xx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `replies`
--

LOCK TABLES `replies` WRITE;
/*!40000 ALTER TABLE `replies` DISABLE KEYS */;
INSERT INTO `replies` VALUES ('1938d938-1590-438a-936b-5d675393ab3a','siuuu','2025-05-15 15:01:35.967000',NULL,'17fff9ba-a755-4af9-b163-54d2d53d0c14','9d40a8e6-4667-4d9b-b315-d20f903ca3d5',NULL),('3abd9bf0-e2d1-42a8-95c6-4d92cc2ec23b','Hello','2025-05-06 00:46:18.357000',NULL,'4fc3c2ad-65ea-47aa-b8ee-8106616e28a5','0e1bcbd4-9ccf-4e93-a30c-33b19853a5f1',NULL),('615fb251-07f5-431e-bd85-fed1fc41d90b','Sao người đẹp','2025-05-14 23:01:54.920000','90462904-46b0-4a98-bbcd-ae16714b87c3','17fff9ba-a755-4af9-b163-54d2d53d0c14','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','ngoctram'),('72fdbfb0-32ee-46fd-8aa0-5bd450072823','chao cau','2025-05-15 15:02:25.393000',NULL,'17fff9ba-a755-4af9-b163-54d2d53d0c14','b689a10b-5559-47f3-92b9-612a447f589e',NULL),('90462904-46b0-4a98-bbcd-ae16714b87c3','Vậy đó hả','2025-05-14 22:57:34.016000',NULL,'17fff9ba-a755-4af9-b163-54d2d53d0c14','13ef5abf-65c5-4210-a59e-9537e2aa6d99',NULL),('a1258896-0e4f-40ce-9173-eef6a6da3555','cảm xúc ác','2025-05-14 22:56:23.519000',NULL,'17fff9ba-a755-4af9-b163-54d2d53d0c14','8324230f-26ed-42e1-aca5-5c223fb385c8',NULL),('c1f0cb62-fdfc-4fc2-a77b-f826c1ed0657','helo reply','2025-05-06 00:46:29.257000','3abd9bf0-e2d1-42a8-95c6-4d92cc2ec23b','4fc3c2ad-65ea-47aa-b8ee-8106616e28a5','0e1bcbd4-9ccf-4e93-a30c-33b19853a5f1','Kiz'),('d2b0d335-ff73-40fb-8562-01521ddbf1a6','Hello chu','2025-05-06 00:47:14.518000','3abd9bf0-e2d1-42a8-95c6-4d92cc2ec23b','4fc3c2ad-65ea-47aa-b8ee-8106616e28a5','0e1bcbd4-9ccf-4e93-a30c-33b19853a5f1','Kiz'),('dcf1e643-f65b-43e0-ba1c-e098a909a6ab','khong biet nua','2025-05-06 00:51:35.760000','3abd9bf0-e2d1-42a8-95c6-4d92cc2ec23b','4fc3c2ad-65ea-47aa-b8ee-8106616e28a5','0e1bcbd4-9ccf-4e93-a30c-33b19853a5f1','Kiz'),('fcd3c287-2d31-4f18-9a32-f56a3bd7d3be','hello','2025-05-15 15:01:04.946000',NULL,'17fff9ba-a755-4af9-b163-54d2d53d0c14','9d40a8e6-4667-4d9b-b315-d20f903ca3d5',NULL),('feb76c2b-61e7-4c26-b1d2-d3101857b32e','=)))','2025-05-14 22:59:31.134000',NULL,'17fff9ba-a755-4af9-b163-54d2d53d0c14','13ef5abf-65c5-4210-a59e-9537e2aa6d99',NULL);
/*!40000 ALTER TABLE `replies` ENABLE KEYS */;
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
