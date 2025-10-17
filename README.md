# Training Scheduler Project

This repository contains a training scheduler application. The project includes multiple services and a client frontend app, providing a platform for managing user authentication, training sessions, notifications, and user interactions.

## System Overview

The system consists of three services:

1. **User Service**: Used for user registration, login, verification, and getting a token for authentication and authorization for further interactions with the system.

2. **Training Scheduling Service**: Allows clients to browse and search available training slots, and enables gym managers to input information about their gym.

3. **Notification Service**: Sends emails when a training session is successfully scheduled and sends reminders before the start of a training session, also sends emails for user verification in order to active account.

All communication between services is done asynchronously through a message broker, and an API gateway (Eureka) handles routing.

### Client Application

A client application is also provided, built with Next.js. It supports different functionalities for users, admins, and managers.

### Technologies Used

- **Spring Boot** for backend services
- **ActiveMQ** for messaging and notifications
- **Eureka** for service discovery and API gateway
- **Next.js** for the frontend application
