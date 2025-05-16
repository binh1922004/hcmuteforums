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
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` varchar(255) NOT NULL,
  `action_id` varchar(255) DEFAULT NULL,
  `content` tinyint DEFAULT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `is_read` bit(1) NOT NULL,
  `received_user_id` varchar(255) NOT NULL,
  `send_user_id` varchar(255) NOT NULL,
  `topic_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2nnxwl6q25fld9yteuidgkrdj` (`received_user_id`),
  KEY `FKtqoeck59eap50tikmh0ss2am8` (`send_user_id`),
  KEY `FK5lq263ys577nfejf15k63ljd6` (`topic_id`),
  CONSTRAINT `FK2nnxwl6q25fld9yteuidgkrdj` FOREIGN KEY (`received_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK5lq263ys577nfejf15k63ljd6` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`),
  CONSTRAINT `FKtqoeck59eap50tikmh0ss2am8` FOREIGN KEY (`send_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `notification_chk_1` CHECK ((`content` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES ('05424f2b-703d-40b8-b14e-780638181da5','cb194598-c606-46df-b5a6-33331f2c97a6',1,'2025-05-15 14:36:48.608000',_binary '\0','b689a10b-5559-47f3-92b9-612a447f589e','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','76e3afa8-55bd-4d11-8dc7-bc7ed5d7f35e'),('098b129f-a56b-42da-a10c-c342a731d9bf','a1258896-0e4f-40ce-9173-eef6a6da3555',0,'2025-05-14 22:56:23.585000',_binary '\0','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','8324230f-26ed-42e1-aca5-5c223fb385c8','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('21a858a9-e701-4244-bdd5-5f3bda758303','46151410-5b04-416b-bdcb-0650d43e480f',1,'2025-05-14 22:55:31.513000',_binary '\0','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','8324230f-26ed-42e1-aca5-5c223fb385c8','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('520a7172-022c-42f9-a0d7-ad8be4ee4e94','feb76c2b-61e7-4c26-b1d2-d3101857b32e',0,'2025-05-14 22:59:31.159000',_binary '\0','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','13ef5abf-65c5-4210-a59e-9537e2aa6d99','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('58104ae9-3645-426f-99bc-c94dfd17f2fe','c54f1da2-c96b-429b-90af-81d2a7febc2f',1,'2025-05-15 14:36:14.693000',_binary '\0','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','b689a10b-5559-47f3-92b9-612a447f589e','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('a52b8f33-66ad-4c6f-8a80-163d6275aa31','615fb251-07f5-431e-bd85-fed1fc41d90b',2,'2025-05-14 23:01:54.959000',_binary '\0','13ef5abf-65c5-4210-a59e-9537e2aa6d99','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('c27c753e-d748-40be-91cd-cc4e4953e540','809f8575-6511-4cd5-9428-1903827c59c1',1,'2025-05-14 22:59:09.916000',_binary '\0','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','13ef5abf-65c5-4210-a59e-9537e2aa6d99','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('e144a1b8-c0ed-4718-8337-769e8e56e127','72fdbfb0-32ee-46fd-8aa0-5bd450072823',0,'2025-05-15 15:02:25.435000',_binary '\0','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','b689a10b-5559-47f3-92b9-612a447f589e','17fff9ba-a755-4af9-b163-54d2d53d0c14'),('f0378acc-318c-4d79-b05c-f2868222085a','90462904-46b0-4a98-bbcd-ae16714b87c3',0,'2025-05-14 22:57:34.039000',_binary '\0','9d40a8e6-4667-4d9b-b315-d20f903ca3d5','13ef5abf-65c5-4210-a59e-9537e2aa6d99','17fff9ba-a755-4af9-b163-54d2d53d0c14');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
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
