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
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles` (
  `id` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `cover_url` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4ixsj6aqve5pxrbw2u0oyk8bb` (`user_id`),
  CONSTRAINT `FK410q61iev7klncmpqfuo85ivh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES ('491c70cc-8d08-4f39-8b5f-6d1606a1a02e','upload/avatars/img_avatar.png',NULL,'upload/covers/img_cover.png','227f66a9-5caf-49f4-bf9c-76ed38cb63b8'),('50227bad-9e9d-486a-856c-1e4642374149','upload/avatars/9d40a8e6-4667-4d9b-b315-d20f903ca3d5-a57b5bea-f6d3-42d1-a76c-9916fd5a669c.jpg',NULL,'upload/covers/9d40a8e6-4667-4d9b-b315-d20f903ca3d5-06c6bcde-cc8e-4188-a3f3-e5f6ed935156.jpg','9d40a8e6-4667-4d9b-b315-d20f903ca3d5'),('59c1ee66-2564-4cfc-a77a-0e5974a4f809','upload/avatars/img_avatar.png',NULL,'upload/covers/8324230f-26ed-42e1-aca5-5c223fb385c8-72a495e6-cac3-4833-8d3b-24a35a2c4e29.jpg','8324230f-26ed-42e1-aca5-5c223fb385c8'),('59c774d9-af73-40ea-afa3-3e548117523a','upload/avatars/img_avatar.png',NULL,'upload/covers/img_cover.png','903c96f2-691f-485e-91fa-1b9cfc3df178'),('6e7b5560-6bea-4f83-91cb-a1cbe04f447a','upload/avatars/img_avatar.png',NULL,'upload/covers/img_cover.png','e2494ffc-c1b5-4fdf-9125-addd158274fe'),('8c4f1e1c-3c9a-4a89-9a2d-2f10b2f083d7','upload/avatars/img_avatar.png','','upload/covers/img_cover.png','0e1bcbd4-9ccf-4e93-a30c-33b19853a5f1'),('ae7e0e6a-120a-49e7-b610-e0c5cf1e1791','upload/avatars/b689a10b-5559-47f3-92b9-612a447f589e-0471645f-01c9-41cd-ae17-958cec50c524.jpg','','upload/covers/b689a10b-5559-47f3-92b9-612a447f589e-5661e5fe-9248-4286-8770-708d48d8f9af.jpg','b689a10b-5559-47f3-92b9-612a447f589e'),('b9b6f3d2-54c7-49e3-8a62-b2a7f0adbe2e','upload/avatars/img_avatar.png','','upload/covers/img_cover.png','6d7cc351-8932-46f8-a791-d1cdda715619'),('be1084af-986b-4c2a-985d-8025e7394e2c','upload/avatars/img_avatar.png',NULL,'upload/covers/img_cover.png','40c4f11a-c2de-4cc4-a031-d6c63fc8c53b'),('d7ba2506-fedc-4263-b456-22bf84c71428','upload/avatars/13ef5abf-65c5-4210-a59e-9537e2aa6d99-288099c8-40c4-4a19-a29b-6f00b2de62da.jpg',NULL,'upload/covers/13ef5abf-65c5-4210-a59e-9537e2aa6d99-f57bc505-867e-43fa-bfbc-f8b94d7c5fdf.jpg','13ef5abf-65c5-4210-a59e-9537e2aa6d99'),('dab1b4eb-d7e0-4787-b5e5-9dc6216f9d11','upload/avatars/img_avatar.png',NULL,'upload/covers/img_cover.png','e16cb972-f237-4235-b83a-6f73c63c5bdc'),('e304ca93-42ef-4082-a888-0de72cb6e50f','upload/avatars/img_avatar.png',NULL,'upload/covers/img_cover.png','1ec28015-ae39-4ecd-a700-22e698ee9b5f'),('ffcec78f-db46-46cf-bac1-8fabc1912d10','upload/avatars/img_avatar.png',NULL,'upload/covers/img_cover.png','10963fa5-bdaf-44f6-a144-4fa079a763fd');
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
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
