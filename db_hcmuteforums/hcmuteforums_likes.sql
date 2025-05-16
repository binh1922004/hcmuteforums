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
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `id` varchar(255) NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `topic_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhklwekhy0lasxveqxmbtkjqpn` (`topic_id`),
  KEY `FKnvx9seeqqyy71bij291pwiwrg` (`user_id`),
  CONSTRAINT `FKhklwekhy0lasxveqxmbtkjqpn` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`),
  CONSTRAINT `FKnvx9seeqqyy71bij291pwiwrg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES ('46151410-5b04-416b-bdcb-0650d43e480f','2025-05-14 22:55:31.470000','17fff9ba-a755-4af9-b163-54d2d53d0c14','8324230f-26ed-42e1-aca5-5c223fb385c8'),('57fd10fb-11cf-49fe-a796-6777f52787c9','2025-05-06 00:45:43.713000','4fc3c2ad-65ea-47aa-b8ee-8106616e28a5','0e1bcbd4-9ccf-4e93-a30c-33b19853a5f1'),('809f8575-6511-4cd5-9428-1903827c59c1','2025-05-14 22:59:09.899000','17fff9ba-a755-4af9-b163-54d2d53d0c14','13ef5abf-65c5-4210-a59e-9537e2aa6d99'),('c54f1da2-c96b-429b-90af-81d2a7febc2f','2025-05-15 14:36:14.598000','17fff9ba-a755-4af9-b163-54d2d53d0c14','b689a10b-5559-47f3-92b9-612a447f589e'),('cb194598-c606-46df-b5a6-33331f2c97a6','2025-05-15 14:36:48.592000','76e3afa8-55bd-4d11-8dc7-bc7ed5d7f35e','9d40a8e6-4667-4d9b-b315-d20f903ca3d5'),('d04e9295-ec46-4e94-a7b7-59ba2df8fe69','2025-05-14 23:01:13.802000','17fff9ba-a755-4af9-b163-54d2d53d0c14','9d40a8e6-4667-4d9b-b315-d20f903ca3d5');
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
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
