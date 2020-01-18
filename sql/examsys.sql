use examsystem;

-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: examsystem
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `exam`
--

DROP TABLE IF EXISTS `exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(256) DEFAULT NULL,
  `date` datetime NOT NULL,
  `timeLimit` int NOT NULL,
  `deleted` varchar(45) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam`
--

LOCK TABLES `exam` WRITE;
/*!40000 ALTER TABLE `exam` DISABLE KEYS */;
INSERT INTO `exam` VALUES (120,'Grade 3 test 1','2020-01-17 00:00:00',1200,'0'),(121,'Demo Test','2020-01-17 00:00:00',3600,'0');
/*!40000 ALTER TABLE `exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_problem`
--

DROP TABLE IF EXISTS `exam_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_problem` (
  `examid` int NOT NULL,
  `problemid` int NOT NULL,
  `score` int NOT NULL DEFAULT '2',
  `idx` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`examid`,`problemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_problem`
--

LOCK TABLES `exam_problem` WRITE;
/*!40000 ALTER TABLE `exam_problem` DISABLE KEYS */;
INSERT INTO `exam_problem` VALUES (120,333,20,0),(120,334,20,1),(120,335,30,2),(120,336,30,3),(121,333,25,0),(121,334,25,1),(121,335,25,2),(121,336,25,3);
/*!40000 ALTER TABLE `exam_problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `examreport_view`
--

DROP TABLE IF EXISTS `examreport_view`;
/*!50001 DROP VIEW IF EXISTS `examreport_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `examreport_view` AS SELECT 
 1 AS `id`,
 1 AS `description`,
 1 AS `date`,
 1 AS `timeLimit`,
 1 AS `deleted`,
 1 AS `reportid`,
 1 AS `studentid`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `problem`
--

DROP TABLE IF EXISTS `problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problem` (
  `id` int NOT NULL AUTO_INCREMENT,
  `answer` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `deleted` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problem`
--

LOCK TABLES `problem` WRITE;
/*!40000 ALTER TABLE `problem` DISABLE KEYS */;
INSERT INTO `problem` VALUES (333,66,'Simple Multiplication',0),(334,66,'Simple Addition',0),(335,68,'Calculate(1)',0),(336,67,'Calculate(2)',0);
/*!40000 ALTER TABLE `problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problemsection`
--

DROP TABLE IF EXISTS `problemsection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `problemsection` (
  `text` varchar(1024) DEFAULT NULL,
  `picpath` varchar(256) DEFAULT NULL,
  `problemid` int NOT NULL,
  `sectionindex` int NOT NULL,
  PRIMARY KEY (`sectionindex`,`problemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `problemsection`
--

LOCK TABLES `problemsection` WRITE;
/*!40000 ALTER TABLE `problemsection` DISABLE KEYS */;
INSERT INTO `problemsection` VALUES ('Choose the correct answer.',NULL,333,0),('Choose the right answer',NULL,334,0),('Choose the right answer.',NULL,335,0),('Choose the right answer.',NULL,336,0),('3x5=?',NULL,333,1),('147+171=?',NULL,334,1),('398-435+102=?',NULL,335,1),('73x471+471x27=?',NULL,336,1),('(A) 13     (B)15     (C)17     (D) 11 ',NULL,333,2),('(A) 300     (B) 318     (C) 337     (D) 298',NULL,334,2),('(A) 55     (B) 75     (C) 45     (D) 65',NULL,335,2),('(A) 47136     (B) 54719     (C) 47100     (D) 41743',NULL,336,2);
/*!40000 ALTER TABLE `problemsection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `examid` int NOT NULL,
  `student` varchar(64) NOT NULL,
  `date` datetime NOT NULL,
  `deleted` varchar(45) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reportscore`
--

DROP TABLE IF EXISTS `reportscore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reportscore` (
  `idx` int NOT NULL,
  `report` int NOT NULL,
  `score` int NOT NULL,
  `answer` int NOT NULL DEFAULT '-1',
  PRIMARY KEY (`idx`,`report`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportscore`
--

LOCK TABLES `reportscore` WRITE;
/*!40000 ALTER TABLE `reportscore` DISABLE KEYS */;
/*!40000 ALTER TABLE `reportscore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `username` varchar(64) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `role` int NOT NULL DEFAULT '2',
  `deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`username`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('admin','admin','Super User',0,0),('Student1','Student1','Andy Hou',2,0),('Student2','Student2','Snow Huang',2,0),('Teacher1','Teacher1','Mr. Google',1,0),('Teacher2','Teacher2','Ms. Bluenile',1,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'examsystem'
--

--
-- Final view structure for view `examreport_view`
--

/*!50001 DROP VIEW IF EXISTS `examreport_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`examsys`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `examreport_view` AS select `exam`.`id` AS `id`,`exam`.`description` AS `description`,`exam`.`date` AS `date`,`exam`.`timeLimit` AS `timeLimit`,`exam`.`deleted` AS `deleted`,`report`.`id` AS `reportid`,`report`.`student` AS `studentid` from (`exam` left join `report` on(((`exam`.`deleted` = 0) and (`report`.`examid` = `exam`.`id`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-17 22:37:20
