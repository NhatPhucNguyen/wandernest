# WanderNest - Smart Travel Planner

## Overview

WanderNest is a comprehensive travel planning application designed to simplify the process of organizing trips. It provides personalized itineraries, budget management, accommodation suggestions, dining options, and activity recommendations based on user preferences and location.

## Features

### Itinerary Management
- Create and customize travel itineraries
- Track trip status (Draft, Active, Completed)
- Manage travel dates and destinations
- Store trip preferences and requirements

### Budget Tracking
- Set total trip budget
- Allocate budget across different categories (accommodation, food, activities, transportation)
- Track expenses in real-time
- Get insights on spending patterns and remaining budget

### Place Recommendations
- **Accommodations**: Find hotels, hostels, and other lodging options
- **Restaurants**: Discover dining options with price ranges and ratings
- **Activities**: Explore tourist attractions and points of interest

### User Management
- Secure authentication system
- Personal profile management
- Trip history access

## Technology Stack

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot
- **API**: RESTful architecture
- **Security**: JWT-based authentication
- **Database**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven/Gradle

### Integration
- **Google Places API**: For location data and recommendations
- **External APIs**: For photo retrieval and place details

## Requirements

### Prerequisites
- Java 17 or later
- PostgreSQL 12 or later
- Maven/Gradle

### Environment Variables
- `JWT_SECRET`: Secret key for JWT token generation
- `SPRING_JWT_EXPIRATION`: Token expiration time in milliseconds
- `GOOGLE_KEY`: Google API key for Places API
- `GOOGLE_ACCESS_TOKEN`: Google API access token

## Setup and Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/wandernest.git
cd wandernest
```

2. Clone the frontend repository
```bash
git clone https://github.com/NhatPhucNguyen/wandernest-frontend.git
cd wandernest-frontend
```

3. Configure environment variables
```bash
# Example for Linux/Mac
export JWT_SECRET=your_secret_key
export SPRING_JWT_EXPIRATION=86400000
export GOOGLE_KEY=your_google_api_key
export GOOGLE_ACCESS_TOKEN=your_google_access_token
```

4. Build the application
```bash
./mvnw clean install
```

5. Run the application
```bash
./mvnw spring-boot:run
```

6. Access the API at `http://localhost:8080/api/`

7. Setup and run the frontend following the instructions in the frontend repository README

## API Documentation

### Authentication Endpoints
- `POST /api/auth/register`: Register a new user
- `POST /api/auth/login`: Authenticate and receive JWT token
- `POST /api/auth/logout`: Invalidate current token
- `GET /api/auth/validate-token`: Verify token validity

### Itinerary Endpoints
- `POST /api/itineraries/generate`: Create a new itinerary
- `GET /api/itineraries`: Get all itineraries for current user
- `DELETE /api/itineraries/{id}`: Delete an itinerary
- `PATCH /api/itineraries/{id}`: Update itinerary status

### Recommendations Endpoints
- `GET /api/accommodations`: Get accommodation recommendations
- `GET /api/restaurants`: Get restaurant recommendations
- `GET /api/activities`: Get activity recommendations

### Budget Endpoints
- `GET /api/budgetAllocations`: Get budget details
- `PUT /api/budgetAllocations`: Update budget allocation
- `GET /api/expenses`: Get all expenses
- `POST /api/expenses`: Add a new expense

## Testing

Run the automated tests:
```bash
./mvnw test
```

## License

[MIT License](LICENSE)

## Contributors

- [Your Name](https://github.com/yourusername)
