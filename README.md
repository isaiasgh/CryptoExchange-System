# CryptoExchange-System

## Table of Contents

1. [Project Overview](#project-overview)
2. [Use Cases Implemented](#use-cases-implemented)
3. [Constraints and Design Decisions](#constraints-and-design-decisions)
4. [Exchange System Configuration](#exchange-system-configuration)
5. [Project Structure](#project-structure)

### 1. **Project Overview**
   This project simulates a cryptocurrency exchange system developed in Java. It allows users to manage their wallet, place buy and sell orders for cryptocurrencies, and execute transactions within a controlled environment.

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

   - **Users Can Manage Their Active Orders**: Users have the ability to view and manage their active buy and sell orders. They can cancel orders that have not yet been matched, which will release any locked balances back into their wallet. This functionality provides users with flexibility in adjusting their trading strategies.

   - **Cryptocurrency Price Fluctuation Based on User-Selected Strategy**: The system offers flexibility in how cryptocurrency prices fluctuate, allowing the user to select between different fluctuation strategies and decide how frequently these price updates occur.

        **Fluctuation Strategies**:
        1. **Match-Based Price Fluctuation Strategy** (`MatchBasedPriceFluctuationStrategy`): This strategy adjusts the price of a cryptocurrency based on the results of order matches. For example, if a buy order is matched above the current market price, the price increases; if it’s matched below, the price decreases. Users can also decide how frequently the prices update, based on a specific number of matched orders.

        2. **Random Price Fluctuation Strategy** (`RandomPriceFluctuationStrategy`): This strategy changes prices at random intervals, mimicking the unpredictable nature of real-world cryptocurrency markets. Although the intervals are random, users can still set how frequently the price updates, based on the number of matched orders.

### 4. **Exchange System Configuration**

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

### 5. **Project Structure**

The project is organized into several packages and directories to maintain a clean and manageable codebase. Below is an overview of the project structure:

- **src/**: Contains the source code of the project.
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