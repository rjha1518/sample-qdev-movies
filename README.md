# 🏴‍☠️ Pirate's Movie Treasure Chest - Spring Boot Demo Application

Ahoy, matey! Welcome to the most swashbuckling movie catalog web application on the seven seas! Built with Spring Boot and featuring our legendary pirate-themed movie search functionality.

## ⚓ Features

- **🎬 Movie Treasure Chest**: Browse 12 classic cinematic treasures with detailed information
- **🔍 Pirate Search Engine**: Hunt for movies by name, ID, or genre with our advanced search functionality
- **📋 Movie Details**: View comprehensive information including captain (director), year discovered, adventure type (genre), journey length (duration), and description
- **⭐ Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **📱 Responsive Design**: Mobile-first design that works on all devices, from ship to shore
- **🌊 Modern UI**: Dark theme with gradient backgrounds, smooth animations, and pirate-themed styling
- **🏴‍☠️ REST API**: JSON endpoints for fellow pirates (developers) to integrate with

## 🛠️ Technology Stack

- **Java 8** - The foundation of our ship
- **Spring Boot 2.7.18** - Our trusty sailing framework
- **Maven** for dependency management
- **Thymeleaf** for HTML templating
- **Log4j 2** for logging our adventures
- **JUnit 5.8.2** for testing our treasure maps

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **🏴‍☠️ Pirate's Movie Treasure Chest**: http://localhost:8080/movies
- **🔍 Search for Treasure**: Use the search form on the main page
- **📋 Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **⚓ REST API Search**: http://localhost:8080/movies/search?name={movieName}&id={movieId}&genre={genre}

## 🔍 Search Functionality - Hunt for Cinematic Treasure!

### Web Interface Search

Navigate to `/movies` and use the pirate-themed search form:

- **Movie Name**: Search by partial or full movie name (case-insensitive)
- **Movie ID**: Search by specific treasure map number (1-12)
- **Genre**: Filter by adventure type using the dropdown

**Example Searches:**
- Search for "Prison" to find "The Prison Escape"
- Search by ID "2" to find "The Family Boss"
- Filter by genre "Drama" to find all dramatic adventures

### REST API Search

For fellow pirates (developers), use our JSON API:

```bash
# Search by movie name
curl "http://localhost:8080/movies/search?name=Prison"

# Search by movie ID
curl "http://localhost:8080/movies/search?id=2"

# Search by genre
curl "http://localhost:8080/movies/search?genre=Drama"

# Combined search (all criteria must match)
curl "http://localhost:8080/movies/search?name=Family&id=2&genre=Crime"
```

**API Response Format:**
```json
{
  "success": true,
  "movies": [...],
  "totalResults": 1,
  "message": "Found 1 movies",
  "pirateMessage": "Ahoy! Discovered 1 pieces of cinematic treasure for ye, me hearty!",
  "searchCriteria": {
    "name": "Prison",
    "id": "",
    "genre": ""
  }
}
```

**Error Handling:**
- **400 Bad Request**: No search criteria provided or invalid ID
- **500 Internal Server Error**: Unexpected errors (with pirate-themed messages)

## 🏗️ Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Business logic with search functionality
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review business logic
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie treasure data
│       ├── mock-reviews.json                 # Mock review data
│       ├── log4j2.xml                        # Logging configuration
│       ├── static/css/
│       │   └── movies.css                    # Pirate-themed styling
│       └── templates/
│           ├── movies.html                   # Main treasure chest page with search
│           └── movie-details.html            # Individual movie details
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MoviesControllerTest.java     # Controller tests with search functionality
            ├── MovieServiceTest.java         # Service tests for search methods
            └── MovieTest.java                # Model tests
```

## 🌊 API Endpoints

### Get All Movies (with Search)
```
GET /movies?name={movieName}&id={movieId}&genre={genre}
```
Returns an HTML page displaying movies. If search parameters are provided, filters results accordingly.

**Query Parameters (all optional):**
- `name`: Movie name to search for (partial matches allowed)
- `id`: Specific movie ID (1-12)
- `genre`: Genre to filter by

**Examples:**
```
http://localhost:8080/movies                    # All movies
http://localhost:8080/movies?name=Prison        # Search by name
http://localhost:8080/movies?id=2               # Search by ID
http://localhost:8080/movies?genre=Drama        # Filter by genre
```

### Search Movies API (JSON)
```
GET /movies/search?name={movieName}&id={movieId}&genre={genre}
```
Returns JSON response with search results and pirate messages.

**Query Parameters (at least one required):**
- `name`: Movie name to search for (partial matches allowed)
- `id`: Specific movie ID (must be positive)
- `genre`: Genre to filter by

**Response:**
- **200 OK**: Successful search (even if no results found)
- **400 Bad Request**: No search criteria provided or invalid parameters
- **500 Internal Server Error**: Unexpected server errors

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

## 🧪 Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest

# Run with coverage
mvn test jacoco:report
```

**Test Coverage:**
- **MovieServiceTest**: Tests all search functionality, edge cases, and error handling
- **MoviesControllerTest**: Tests both HTML and REST API endpoints
- **MovieTest**: Tests the Movie model

## 🔧 Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

Check the logs for pirate messages:
```bash
tail -f logs/application.log
```

## 🏴‍☠️ Pirate Language Features

Our application includes authentic pirate language throughout:

- **Search Messages**: "Ahoy! Found X pieces of cinematic treasure for ye!"
- **Error Messages**: "Arrr! No treasure found in these waters, matey!"
- **API Responses**: Include both standard and pirate-themed messages
- **Logging**: Pirate-themed log messages for debugging adventures
- **UI Labels**: "Captain (Director)", "Year Discovered", "Adventure Type (Genre)"

## 🤝 Contributing

This project welcomes contributions from fellow pirates! Feel free to:
- Add more movies to the treasure chest
- Enhance the search functionality
- Improve the pirate-themed UI/UX
- Add new features like advanced filtering
- Enhance the responsive design
- Add more pirate language and themes

## 📜 License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*"Arrr! May yer code be bug-free and yer deployments smooth sailing, me hearty!"* 🏴‍☠️
