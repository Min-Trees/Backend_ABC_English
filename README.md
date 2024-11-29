**# ABC-ENGLISH**
**## Overview**
This project is an e-learning platform built using Spring Boot with a client-server architecture. The system provides functionalities for user registration, managing courses, tracking progress, and interactive features like messaging and forums.
**## Architecture**
### Client-Server Model
- Client: Handles user interactions and sends requests to the backend.
- Server (Spring Boot): Processes business logic, manages data storage, and provides RESTful APIs.
**## Features**
### User Registration and Login
The registration process includes a two-step verification mechanism to ensure account security and authenticity. Here's how it works:
- The user fills out the registration form with their email and other required information.
- The system validates the submitted details and saves the user as "unverified" in the database.
- ![Login](screenshoot/login.png)  
#### Verification Email
- After the user submits the registration form, the system sends an email to the provided address.
- The email contains a verification link with a unique token
### Profile Management
Users can update personal information and track their learning progress.
### Course List
Displays available courses categorized by:
        + Level: Beginner, Intermediate, Advanced.
        + Topics: Grammar, Vocabulary, Pronunciation.
### Diverse Learning Materials
Includes teaching videos, PDF documents, online exercises, and quizzes.
### Assignments and Tests
Enables users to practice with assignments and tests after each chapter.
### Discussion Forum
Provides a space for students and teachers to discuss and resolve queries.
### Messaging
Allows students to directly message teachers for advice or questions.
### Automated Grading
Automatically evaluates grammar-based text questions.
### Notifications and Reminders
Sends alerts for new lessons and reminders for pending assignments.
