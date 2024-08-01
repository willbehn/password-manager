# password-manager

## About
This is a Java-based password manager for storing and managing passwords. It uses encryption to keep password information safe and offers features like multiple user support and password search functionality for ease of use.

## Table of Contents

1. [Features](#features)
2. [Technologies Used](#technologies-used)
3. [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
4. [Gallery](#gallery)

## Features

- **AES Symmetric Encryption:** All passwords are encrypted using the Advanced Encryption Standard (AES) in CBC mode, enhancing the security of stored passwords.
- **Password Management:** Enables easy search functionality among stored passwords, and the ability to delete and change existing passwords.
- **Multi-User Support:** Supports multiple users, allowing each user to manage their set of passwords securely.
- **Timer:** The user will be logged out after 90 minutes of inactivity.

## Technologies Used

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![AES Encryption](https://img.shields.io/badge/AES%20Encryption-6DB33F?style=for-the-badge&logo=aes&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=swing&logoColor=white)

## Getting Started

### Prerequisites

- Java 11 or higher

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/willbehn/password-manager.git
    cd password-manager
    ```

2. **Compile the project**:
    ```bash
    javac -d bin src/**/*.java
    ```

3. **Run the application**:
    ```bash
    java -cp bin Main
    ```

### Running the Application

Once the application is running, you can interact with it through the user interface.

## Gallery

Login window. If either the password or username does not match any existing user, a message will be displayed to the user that either the password or username is incorrect.  
![Screenshot 2024-01-25 at 12 08 59](https://github.com/willbehn/password-manager/assets/71493303/39c94f41-4f75-406b-b0f9-9108ee86fab9)

Option to add new users to the password manager.  
![Screenshot 2024-01-25 at 12 09 15](https://github.com/willbehn/password-manager/assets/71493303/5b65e4cc-149a-4d9f-bceb-0fd37778f74c)

Overview of the password manager, "copy password" copies the password to the clipboard for easy use. "Details" lets the user edit and remove passwords, and shows the password in plain-text.  
![Screenshot 2024-01-25 at 12 10 09](https://github.com/willbehn/password-manager/assets/71493303/efd4fc03-84ce-429d-acb8-fa5b43384658)

The search bar allows the user to easily find passwords among different services.  
![Screenshot 2024-01-25 at 12 10 27](https://github.com/willbehn/password-manager/assets/71493303/1c5260c4-0ca7-40aa-987f-09ca89c6ab4e)

An example of what the details window looks like. The detail window gives you the option to change the password, or remove it from the password manager.  
![Screenshot 2024-01-25 at 12 11 14](https://github.com/willbehn/password-manager/assets/71493303/34faa710-aa8f-4bff-8a2f-423d31a3fa93)

Example of how the data is stored in the `.txt` file. All passwords are stored encrypted using AES encryption in CBC mode, and the master password is stored hashed using SHA-256.  
![Screenshot 2024-01-15 at 23 03 11](https://github.com/willbehn/password-manager/assets/71493303/126f4270-6bcc-4643-804a-fdc3791cc8f3)

![Screenshot 2024-01-15 at 23 03 30](https://github.com/willbehn/password-manager/assets/71493303/b6e77d45-abc7-4d65-a75a-dc52af8bf380)
