/*This SQL script is for creating a simple database for storing your passwords. 
The password manager currently requires two .txt files to function, but will be 
moved over to use a SQL database for storage when finished. This is currently for testing*/

CREATE SCHEMA passwordManager;

CREATE TABLE passwordManager.Users (
    id text PRIMARY KEY, 
    hashedKey text NOT NULL,
    salt text NOT NULL)

CREATE TABLE passwordManager.Passwords (
    mainUser text REFERNCES Users(id) NOT NULL
    userInfo text NOT NULL
    passwordEncrypted text NOT NULL
    iv text NOT NULL
    dateAdded date getdate()

    CONSTRAINT password_pk PRIMARY KEY (mainUser,userInfo)
);