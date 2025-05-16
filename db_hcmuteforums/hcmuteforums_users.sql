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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `mssv` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('0e1bcbd4-9ccf-4e93-a30c-33b19853a5f1','An Giang, Phu Tan','2004-09-16','cogeki5735@lesotica.com','Leona Fiora Kiz','Không muốn tiết lộ',NULL,'$2a$10$PCoYXo3HC8i/SxYmgSz1VeepVCqk9oW4tS4cJr69BYF6dhK0pDPqS','','Kiz'),('10963fa5-bdaf-44f6-a144-4fa079a763fd',NULL,'2004-07-17','locust01378@driftz.net','Hoa Pham','Nam',NULL,'$2a$10$NOXPAlruNYWMI4CPXXsRSOw.4hfaZNeNJznrPlI4mFs7iWaiUJ4Hq',NULL,'hoapham'),('13ef5abf-65c5-4210-a59e-9537e2aa6d99',NULL,'2004-02-20','xatuxa@polkaroad.net','Ngoc Tram','Nữ',NULL,'$2a$10$WCtnPaH1OEu7neYw9Grfy.hhNDy.GXUlMX4omUSIZGltzgqGahpkG',NULL,'ngoctram'),('1ec28015-ae39-4ecd-a700-22e698ee9b5f',NULL,'2003-05-14','mopsevoltu@gufum.com','Hoang Kiet','Nam',NULL,'$2a$10$R3MujSJWcauTbkZLyHveO.gpDFwqBQw1xQvvGCxt1.HwrLyYkNixq',NULL,'kietlac'),('227f66a9-5caf-49f4-bf9c-76ed38cb63b8',NULL,'2008-05-12','4747glynnis@chefalicious.com','Phu Hao','Nam',NULL,'$2a$10$/m6U6btSNIuqLbcqxxsy3unZniHvNHsQ7G8DyRFUfgLh9VDLnO202',NULL,'phuhao'),('40c4f11a-c2de-4cc4-a031-d6c63fc8c53b',NULL,'2004-04-05','cod07190@mixzu.net','Tat duy','Nam',NULL,'$2a$10$jn1MmZgOB7nX/67Ax8OvjeoAeqGPPUDEL3jR404tpEFWhF5EhLtdm',NULL,'duytt'),('6d7cc351-8932-46f8-a791-d1cdda715619','Hà Nội, Việt Nam','2025-04-03','ttnghia204@gmail.com','Tran Trong Nghia','MALE',NULL,'$2a$10$YferlO4I3ff8oORrXhjfE.ph7Jn.ZDjp3jOmIo7OwL3WfCUTo.jEe','123456789','nghia'),('8324230f-26ed-42e1-aca5-5c223fb385c8',NULL,'2007-04-29','1lhti@chefalicious.com','Trieu Nguyen','Nam',NULL,'$2a$10$oAMfJvRcH8ybkZ7.kSheOeHzpIaZkM9PkrjwGlokreto/21nTS0Sy',NULL,'trieudz'),('903c96f2-691f-485e-91fa-1b9cfc3df178',NULL,'2007-05-12','intact.jaguar.vwvt@letterprotect.com','Binh Ho','Nam',NULL,'$2a$10$yXCef3u1RZNolgvC44LFLuE1y1Q7hxVxFn36LGKBiAiOO/68ko3Gu',NULL,'binhho'),('9d40a8e6-4667-4d9b-b315-d20f903ca3d5',NULL,'2008-05-12','cp9hmow8fw@osxofulk.com','Nghia Tran','Nam',NULL,'$2a$10$vm.pXs/3VlExfDWSKv7/Ue2Nld0dA3Mp8gn2if4XxelhoLPy9/FAK',NULL,'nghiatran'),('b689a10b-5559-47f3-92b9-612a447f589e',NULL,'2004-05-12','racacip375@inkight.com','Tran Tien Dat','Nam',NULL,'$2a$10$mHzyiXpnVepsGlG9aeIe2OzuSzi8BDJqUV5Od7fjDogjRafro3UUK',NULL,'dat09'),('e16cb972-f237-4235-b83a-6f73c63c5bdc','So1 VVN','2007-04-11','javire3016@harinv.com','Oh Siba javire','Nữ',NULL,'$2a$10$ZpuxPfXulwaWPUPnO2nJUuARhd709fSSb2U97aeHIlwbSz8V6Dfw2',NULL,'javire'),('e2494ffc-c1b5-4fdf-9125-addd158274fe',NULL,'2000-01-01','mewap36221@ingitel.com','Nguyen Van A','Male',NULL,'$2a$10$GRf.veF.tTT3fp.lhlmRnOE3NNIyJ6C3YkWq8ic6cthBbW1BUABoO',NULL,'exampleUser');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-16  0:29:52
