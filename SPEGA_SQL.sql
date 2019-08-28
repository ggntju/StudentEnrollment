create database admissiondb;
use admissiondb;
CREATE TABLE `course` (
   `courseID` varchar(11) NOT NULL,
   `coursename` varchar(45) DEFAULT NULL,
   `fees` float NOT NULL,
   PRIMARY KEY (`courseID`)
 );
 
 CREATE TABLE `student` (
   `studentID` varchar(5) NOT NULL,
   `firstname` varchar(45) DEFAULT NULL,
   `lastname` varchar(45) DEFAULT NULL,
   `gradelevel` int(1) DEFAULT NULL,
   `tutition` float DEFAULT NULL,
   PRIMARY KEY (`studentID`),
   UNIQUE KEY `StudentID_UNIQUE` (`studentID`)
 );
 
 create table `studentCourse` (
	`studentID` varchar(5) NOT NULL,
    `courseID` varchar(11) NOT NULL
);