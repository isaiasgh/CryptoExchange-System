# CryptoExchange-System

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Use Cases Implemented](#2-use-cases-implemented)
3. [Constraints and Design Decisions](#3-constraints-and-design-decisions)
4. [Design Patterns Used](#4-design-patterns-used)
5. [Exchange System Configuration](#5-exchange-system-configuration)
6. [Initial Data and Testing Instructions](#6-initial-data-and-testing-instructions)
7. [UML Diagrams](#7-uml-diagrams)
8. [Project Structure](#8-project-structure)

### 1. **Project Overview**
   This project simulates a cryptocurrency exchange system developed in Java. It allows users to manage their wallet, place buy and sell orders for cryptocurrencies,and purchase cryptos from the system.

### 2. **Use Cases Implemented**
   - **User Registration**
   - **User Login**
   - **User Logout**
   - **Depositing Funds**
   - **Buy Cryptocurrencies from the Exchange**
   - **Placing Buy and Sell Orders**

   *(Note: Detailed descriptions of the use cases are provided in the project documentation. This README focuses on implementation specifics and additional constraints.)*

### 3. **Constraints and Design Decisions**
   - **Locked Balance in Active Orders**: When a user places a buy or sell order, the corresponding fiat or cryptocurrency balance is locked. This means the committed funds or cryptocurrency are reserved exclusively for that order and cannot be used in other transactions until the order is either fulfilled or cancelled.

   - **Users Can Manage Their Active Orders**: Users are able to view and manage their active buy and sell orders. They can cancel orders that have not yet been matched, which will release any locked balances back into their wallet.

   - **Cryptocurrency Price Fluctuation Based on User-Selected Strategy**: The system offers flexibility in how cryptocurrency prices fluctuate, allowing the user to select between different fluctuation strategies and decide how frequently these price updates occur.

      ### Fluctuation Strategies:
      Both strategies are designed to operate based on the last *n* order matches for each cryptocurrency. This means that if the average value of the last *n* orders for a specific crypto is higher than its current market value, the new price will increase. Alternatively, if the average value is lower, its market price will decrease.

        1. **Match-Based Price Fluctuation Strategy** (`MatchBasedPriceFluctuationStrategy`): This strategy adjusts the market price of a cryptocurrency based on the average value of the last *n* matched orders.

        2. **Random Price Fluctuation Strategy** (`RandomPriceFluctuationStrategy`): This strategy changes prices at random intervals.

      The user can configure the number of order matches required to update a cryptocurrency's market price.

### 5. **Exchange System Configuration**
Users can set up the `ExchangeSystem` to choose the fluctuation strategy and decide how often prices are updated. This is configured in the `ExchangeApplication` class before running the system.

**How to Change the Fluctuation Strategy**:

To switch strategies and set the update frequency, modify the `ExchangeApplication` class. Here’s an example with the `MatchBasedPriceFluctuationStrategy`:

```java
public static void main(String[] args) {
    MainView view = new MainView();
    RootController rootController = new RootController(view);

    // Configure system with a match-based price fluctuation strategy
    rootController.configureSystem(new MatchBasedPriceFluctuationStrategy(), 2);

    // Run the system
    rootController.run();
    view.close();
}
```
In this example, the `configureSystem` method uses `MatchBasedPriceFluctuationStrategy`, updating prices after every 2 matched orders.

To use the `RandomPriceFluctuationStrategy` instead, update the configuration like this:

```java
rootController.configureSystem(new RandomPriceFluctuationStrategy(), 2);
```

This allows for random price changes while still setting the frequency of updates.

### 4. **Design Patterns Used**

- **Observer Pattern**: 
  - **Purpose**: Used for the order matching process.
  - **Components**:
    - **Subject**: `OrderBook` - Manages the list of orders and notifies observers when changes occur.
    - **Observer**: `OrderMatchingService` - Reacts to changes in the `OrderBook` and performs order matching.

- **Strategy Pattern**: 
  - **Purpose**: Defines different strategies for cryptocurrency price fluctuation.
  - **Components**: 
    - **Strategies**: `MatchBasedPriceFluctuationStrategy` and `RandomPriceFluctuationStrategy` - Implement different algorithms for price changes based on user configuration.

- **Singleton Pattern**:
  - **Purpose**: Ensures a single instance of a class is created and used throughout the application.
  - **Components**:
    - **`ExchangeSystem`**: Manages the global state of the exchange, including users and cryptocurrencies.
    - **`OrderMatchingService`**: Ensures only one instance manages the order matching process. *(Note: This is less central compared to the others.)*


### 6. **Initial data and testing instructions**
The data is saved in a serialized file named `system.ser`, which includes all the user and system information.

#### Data Storage

- The application stores all user and system data in a file named `system.ser`.
- If you wish to reset the data, simply delete the `system.ser` file. Upon restarting the application, a new `system.ser` file will be generated automatically, containing only the system information (e.g., cryptocurrencies, their quantities, and prices) without any user data.

#### Preloaded Data Details
The `system.ser` file currently includes the following users and data:

##### **User 1: Juan**
- **Name:** Juan
- **Email:** juan@gmail.com
- **Password:** juan1234
- **Cryptocurrency Balances:**
  - **1.5 BTC Total:** 0.5 BTC committed to a sell order with a minimum price of $20000.
  - **15 DOGE Total:** 2 DOGE committed to a sell order with a total minimum price of $10000.
- **Fiat Money Balance:**
  - **$71250 Total:** $2500 committed to a buy order for 1 ETH and $68750 available for purchases.

##### **User 2: Ángel**
- **Name:** Ángel
- **Email:** angel@gmail.com
- **Password:** angel1234
- **Cryptocurrency Balances:**
  - **10 ETH Total:** 3 ETH committed to a sell order with a minimum price of $10500.
- **Fiat Money Balance:**
  - **$40,000 Total:** $30000 committed to a buy order for 1 BTC.

#### System Behavior and Initial Conditions

- So far, only transactions between users and the ExchangeSystem have been generated. 
- No transactions have occurred between users, meaning no buy-sell order matches have been made. As a result, cryptocurrency values remain at their initial prices.
- Additionally, the system is configured to trigger price fluctuations every 5 matched orders using the `MatchBasedPriceFluctuationStrategy`.

### 7. **UML Diagrams**

#### Model UML Class Diagram
The model class diagram provides a detailed view of the core entities and their relationships within the system.

- **Model Class Diagram**: ![Model UML Class Diagram](docs/UML%20Diagrams/model-diagram.jpeg)

#### Other Diagrams
Additional UML diagrams for the service and controller layers are also available for reference.

- **Service Class Diagram**: ![Service UML Class Diagram](docs/UML%20Diagrams/service-diagram.jpeg)
- **Controller Class Diagram**: ![Controller UML Class Diagram](docs/UML%20Diagrams/controller-diagram.jpeg)

### 8. **Project Structure**
The project is organized into several packages and directories to maintain a clean and manageable codebase. Below is an overview of the project structure:

- **FinalProject-CryptoExchangeSystem/src/**: Contains the source code of the project.
  - **com.globant.model/**: Contains the model classes representing the core entities of the system.
    - `finance/`: Classes related to financial operations.
    - `orders/`: Classes related to orders and order management.
    - `system/`: Classes representing the system and its components.
  - **com.globant.service/**: Contains service classes that handle business logic and interactions.
  - **com.globant.controller/**: Contains controller classes managing the application flow and user interactions.
  - **com.globant.view/**: Contains view classes for user interface elements.

- **docs/**: Contains documentation files.
  - `UML Diagrams/`: Directory for UML class diagrams.
    - `model-diagram.jpg`: UML diagram for the model classes.
    - `service-diagram.jpg`: UML diagram for the service classes.
    - `controller-diagram.jpg`: UML diagram for the controller classes.
