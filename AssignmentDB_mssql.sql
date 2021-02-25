USE master;
GO

IF EXISTS(select * from sys.databases where name='AssignmentDB')
	DROP DATABASE AssignmentDB

CREATE DATABASE AssignmentDB;
GO

USE AssignmentDB;
GO

CREATE TABLE Tasks (
	TaskID INT IDENTITY 
		CONSTRAINT TaskID_PK PRIMARY KEY(TaskID),
	TaskName VARCHAR(30) NOT NULL
		CONSTRAINT TaskNev_U UNIQUE(TaskName),
	TaskDescription VARCHAR(100)
);

CREATE TABLE Assignments (
	AssignmentID INT IDENTITY
		CONSTRAINT AssignmentID_PK PRIMARY KEY(AssignmentID),
	uuid VARCHAR(90) CONSTRAINT uuid_u UNIQUE(uuid),
	AssignmentName VARCHAR(30),
	StartDate DATE,
	FinishDate DATE,
	Task INT
		CONSTRAINT Task_FK FOREIGN KEY(Task) REFERENCES Tasks(TaskID)
);

CREATE TABLE Employees (
	EmployeeID INT IDENTITY
		CONSTRAINT EmployeeID_PK PRIMARY KEY(EmployeeID),
	firstname VARCHAR(45),
	lastname VARCHAR(45),
	Assignment INT 
		CONSTRAINT Assingment_FK FOREIGN KEY(Assignment) REFERENCES Assignments(AssignmentID)
)