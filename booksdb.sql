-- MySQL dump 10.16  Distrib 10.1.16-MariaDB, for osx10.6 (i386)
--
-- Host: localhost    Database: booksdb
-- ------------------------------------------------------
-- Server version	10.1.16-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authorISBN`
--

DROP TABLE IF EXISTS `authorISBN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorISBN` (
  `authorID` int(11) NOT NULL,
  `isbn` char(10) NOT NULL,
  KEY `authorID` (`authorID`),
  KEY `isbn` (`isbn`),
  CONSTRAINT `authorisbn_ibfk_1` FOREIGN KEY (`authorID`) REFERENCES `authors` (`authorID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `authorisbn_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `titles` (`isbn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorISBN`
--

LOCK TABLES `authorISBN` WRITE;
/*!40000 ALTER TABLE `authorISBN` DISABLE KEYS */;
INSERT INTO `authorISBN` VALUES (1,'1501129740'),(1,'150112787X'),(2,'0345533488'),(3,'0321579364'),(4,'0439708184'),(4,'0439064872'),(4,'0439136369'),(4,'0439358078'),(6,'1987817990'),(7,'0486280616'),(7,'1509828001'),(8,'0571056865'),(9,'0316769533');
/*!40000 ALTER TABLE `authorISBN` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors` (
  `authorID` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` char(20) NOT NULL,
  `lastName` char(20) NOT NULL,
  PRIMARY KEY (`authorID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'Stephen','King'),(2,'George R.R.','Martin'),(3,'Mike','Cohn'),(4,'J.K.','Rowling'),(6,'F Scott','Fitzgerald'),(7,'Mark','Twain'),(8,'William','Golding'),(9,' J. D.','Salinger');
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publishers` (
  `publisherID` int(11) NOT NULL AUTO_INCREMENT,
  `publisherName` char(100) NOT NULL,
  PRIMARY KEY (`publisherID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publishers`
--

LOCK TABLES `publishers` WRITE;
/*!40000 ALTER TABLE `publishers` DISABLE KEYS */;
INSERT INTO `publishers` VALUES (1,'Scribner'),(2,'Pocket Books'),(3,'Bantam'),(4,'Addison-Wesley Professional'),(5,'Pottermore'),(6,'Scholastic Paperbacks'),(9,'Charles Scribners Sons'),(10,'Dover Publications'),(11,'Macmillan Collectors Library'),(12,'Faber and Faber'),(13,'Little Brown and Company');
/*!40000 ALTER TABLE `publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `titles`
--

DROP TABLE IF EXISTS `titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `titles` (
  `isbn` char(10) NOT NULL,
  `editionNumber` int(11) NOT NULL,
  `Year` char(4) NOT NULL,
  `publisherID` int(11) NOT NULL,
  `price` decimal(8,2) NOT NULL,
  `title` varchar(500) NOT NULL,
  PRIMARY KEY (`isbn`),
  KEY `publisherID` (`publisherID`),
  CONSTRAINT `titles_ibfk_1` FOREIGN KEY (`publisherID`) REFERENCES `publishers` (`publisherID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `titles`
--

LOCK TABLES `titles` WRITE;
/*!40000 ALTER TABLE `titles` DISABLE KEYS */;
INSERT INTO `titles` VALUES ('0316769533',1,'1951',13,16.87,'The Catcher in the Rye'),('0321579364',1,'2009',4,33.73,'Succeeding with Agile: Software Development Using Scrum'),('0345533488',1,'2015',3,20.19,'A Knight of the Seven Kingdoms (A Song of Ice and Fire)'),('0439064872',1,'2000',6,19.95,'Harry Potter And The Chamber Of Secrets'),('0439136369',1,'2015',5,15.24,'Harry Potter and the Prisoner of Azkaban'),('0439358078',1,'2015',5,29.95,'Harry Potter and the Order of the Phoenix'),('0439708184',1,'2015',5,12.70,'Harry Potter and the Sorcerers Stone'),('0486280616',1,'1994',10,8.92,'Adventures of Huckleberry Finn'),('0571056865',1,'1954',12,18.30,'Lord of the Flies'),('150112787X',1,'2016',2,19.19,'The Bazaar of Bad Dreams'),('1501129740',1,'2016',1,18.00,'End of Watch'),('1509828001',1,'1876',11,9.27,'The Adventures of Tom Sawyer'),('1987817990',1,'1925',9,12.95,'The Great Gatsby');
/*!40000 ALTER TABLE `titles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-05 23:12:07
