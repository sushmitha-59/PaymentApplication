# PaymentApplication
This project is a microservices-based E-Wallet Payment System built using **Spring Boot**, **Kafka**, **Spring Security**, **Feign Clients** and **Eureka**. 
Instead of one big application doing everything, functionalities are split into independent microservices.

![image](https://github.com/user-attachments/assets/c6cefae9-e662-4d46-a66f-17111f580722)
## ⚙️ Microservices

### 1. **User Service**
- Endpoint: `/user/create`, Requires authorization
- Creates user with appropriate authorities and stores in the User database
- Publishes user creation events to Kafka (`user_topic`)

### 2. **Wallet Service**
- Subscribes to Kafka `user_topic` to create wallets with initial balance.
- Endpoint: `/wallet/update` , Requires authorization
- Updates sender and receiver wallet balances in the Wallet database

### 3. **Transaction Service**
- Endpoint: `/transaction/initiate`, Requires authorization
- Calls `/wallet/update` via Feign Client
- Publishes transaction result to Kafka (`transaction_topic`)
  
### 4. **Notification Service**
- Subscribes to `transaction_topic`
- Sends email notifications to sender and receiver based on transaction status.

## 🌐 Service Discovery

This project uses **Spring Cloud Netflix Eureka** for service discovery.

- All services (User, Wallet, Transaction, Notification) register themselves with the **Eureka Server** for service discovery via logical service names.
- Inter-service communication (e.g., via Feign Clients) uses service names instead of hardcoded URLs.
- Eureka ensures load balancing.
- Access it via: `http://localhost:8761`

## 🔒 Security

All major endpoints like `/user/create`, `/wallet/update`, and `/transaction/initiate` are **secured using Spring Security**.

- Basic Auth configuration
- Only authenticated users can perform transactions or wallet updates

## 🔌 Technologies Used

- Java
- Spring Boot
- Spring Security
- Kafka (Apache)
- Feign Client
- Java Mail Sender
- Eureka Service Discovery (if used)
- Lombok (optional)


## 📦 Running the Project

### Prerequisites:
- Java 17+
- Kafka and Zookeeper running
- MySQL database

### Steps:
1. Start Zookeeper and Kafka configuring their properties
2. Run all microservices:
   - Wallet Service
   - User Service
   - Transaction Service
   - Notification Service
3. Hit `/user/create` to create users.
4. Use `/transaction/initiate` to start a transaction.
5. Emails will be triggered from Notification Service to both payment sender and receiver based on Transaction.


## 💡 Notes

- Make sure the Mail App Password is correctly configured.
- If not using Eureka, specify Feign clients with direct URLs.


## 📬 Email Configuration (Gmail)

Update your `application.properties` 
```properties
spring.notification.from-username=youremail@gmail.com
spring.notification.from-password=your_app_password
