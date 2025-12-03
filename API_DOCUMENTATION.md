# 🏴‍☠️ Pirate's Movie Search API Documentation

Ahoy, fellow pirates! This document be yer treasure map to navigating our movie search API. Whether ye be a seasoned developer or a landlubber just starting out, this guide will help ye integrate with our cinematic treasure chest.

## 🌊 Base URL

```
http://localhost:8080
```

## 🔍 Search Endpoints

### 1. HTML Search Interface

**Endpoint:** `GET /movies`

**Description:** Returns an HTML page with search form and movie results. Perfect for web browsers and human pirates!

**Query Parameters (all optional):**

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `name` | String | Movie name to search for (partial matches, case-insensitive) | `Prison`, `Family`, `Hero` |
| `id` | Long | Specific movie ID (1-12) | `1`, `2`, `12` |
| `genre` | String | Genre to filter by (partial matches, case-insensitive) | `Drama`, `Crime`, `Action` |

**Examples:**
```bash
# All movies (treasure chest overview)
GET /movies

# Search by name
GET /movies?name=Prison

# Search by ID
GET /movies?id=2

# Filter by genre
GET /movies?genre=Drama

# Combined search
GET /movies?name=Family&genre=Crime
```

**Response:** HTML page with pirate-themed interface

---

### 2. JSON Search API

**Endpoint:** `GET /movies/search`

**Description:** Returns JSON response with search results and pirate messages. Perfect for API integration, ye scurvy developers!

**Query Parameters (at least one required):**

| Parameter | Type | Required | Validation | Description |
|-----------|------|----------|------------|-------------|
| `name` | String | No | Non-empty after trim | Movie name to search for |
| `id` | Long | No | Must be positive (> 0) | Specific movie ID |
| `genre` | String | No | Non-empty after trim | Genre to filter by |

**Success Response (200 OK):**
```json
{
  "success": true,
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🎬"
    }
  ],
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

**No Results Response (200 OK):**
```json
{
  "success": true,
  "movies": [],
  "totalResults": 0,
  "message": "No movies found matching your search criteria",
  "pirateMessage": "Arrr! No treasure found in these waters, matey! The sea be empty of yer desired films!",
  "searchCriteria": {
    "name": "NonExistent",
    "id": "",
    "genre": ""
  }
}
```

**Error Response - No Criteria (400 Bad Request):**
```json
{
  "success": false,
  "message": "Arrr! Ye need to provide at least one search parameter, ye scurvy dog!",
  "pirateMessage": "Batten down the hatches! No search criteria provided!"
}
```

**Error Response - Invalid ID (400 Bad Request):**
```json
{
  "success": false,
  "message": "Arrr! Movie ID must be a positive number, matey!",
  "pirateMessage": "That be no valid treasure map number!"
}
```

**Error Response - Server Error (500 Internal Server Error):**
```json
{
  "success": false,
  "message": "An error occurred while searching for movies",
  "pirateMessage": "Shiver me timbers! A kraken attacked our search! Try again later, ye brave soul!"
}
```

## 🎬 Movie Data Structure

Each movie in the response contains the following fields:

| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Unique movie identifier (1-12) |
| `movieName` | String | Full movie title |
| `director` | String | Movie director name |
| `year` | Integer | Release year |
| `genre` | String | Movie genre(s) |
| `description` | String | Movie plot description |
| `duration` | Integer | Movie length in minutes |
| `imdbRating` | Double | Rating out of 5.0 |
| `icon` | String | Movie emoji icon |

## 🔍 Search Behavior

### Name Search
- **Case-insensitive**: "prison" matches "The Prison Escape"
- **Partial matching**: "Prison" matches "The Prison Escape"
- **Whitespace handling**: Leading/trailing spaces are trimmed
- **Empty strings**: Treated as no search criteria

### ID Search
- **Exact matching**: Only returns movie with exact ID
- **Validation**: Must be positive integer (> 0)
- **Invalid values**: 0, negative numbers, or null treated as no criteria

### Genre Search
- **Case-insensitive**: "drama" matches "Drama" and "Crime/Drama"
- **Partial matching**: "Crime" matches "Crime/Drama" and "Action/Crime"
- **Whitespace handling**: Leading/trailing spaces are trimmed
- **Empty strings**: Treated as no search criteria

### Combined Search
When multiple criteria are provided, ALL must match (AND logic):
- Movie must contain the name substring
- AND have the exact ID
- AND contain the genre substring

## 📊 Available Movies

The treasure chest currently contains 12 movies:

| ID | Movie Name | Director | Year | Genre | Duration | Rating |
|----|------------|----------|------|-------|----------|--------|
| 1 | The Prison Escape | John Director | 1994 | Drama | 142 | 5.0 |
| 2 | The Family Boss | Michael Filmmaker | 1972 | Crime/Drama | 175 | 5.0 |
| 3 | The Masked Hero | Chris Moviemaker | 2008 | Action/Crime | 152 | 5.0 |
| 4 | Urban Stories | Quinn Director | 1994 | Crime/Drama | 154 | 4.5 |
| 5 | Life Journey | Robert Filmmaker | 1994 | Drama/Romance | 142 | 4.0 |
| 6 | Dream Heist | Chris Moviemaker | 2010 | Action/Sci-Fi | 148 | 4.5 |
| 7 | The Virtual World | Alex Director | 1999 | Action/Sci-Fi | 136 | 4.5 |
| 8 | The Wise Guys | Martin Filmmaker | 1990 | Crime/Drama | 146 | 4.5 |
| 9 | The Quest for the Ring | Peter Moviemaker | 2001 | Adventure/Fantasy | 178 | 4.5 |
| 10 | Space Wars: The Beginning | George Director | 1977 | Adventure/Sci-Fi | 121 | 4.0 |
| 11 | The Factory Owner | Steven Filmmaker | 1993 | Drama/History | 195 | 4.5 |
| 12 | Underground Club | David Moviemaker | 1999 | Drama/Thriller | 139 | 4.5 |

## 🧪 Testing Examples

### cURL Examples

```bash
# Basic name search
curl "http://localhost:8080/movies/search?name=Prison"

# ID search
curl "http://localhost:8080/movies/search?id=1"

# Genre search
curl "http://localhost:8080/movies/search?genre=Drama"

# Combined search
curl "http://localhost:8080/movies/search?name=Family&id=2&genre=Crime"

# Case-insensitive search
curl "http://localhost:8080/movies/search?name=prison"

# Partial genre match
curl "http://localhost:8080/movies/search?genre=Sci"

# Error case - no parameters
curl "http://localhost:8080/movies/search"

# Error case - invalid ID
curl "http://localhost:8080/movies/search?id=-1"
```

### JavaScript Examples

```javascript
// Basic search function
async function searchMovies(name, id, genre) {
    const params = new URLSearchParams();
    if (name) params.append('name', name);
    if (id) params.append('id', id);
    if (genre) params.append('genre', genre);
    
    try {
        const response = await fetch(`/movies/search?${params}`);
        const data = await response.json();
        
        if (data.success) {
            console.log(data.pirateMessage);
            return data.movies;
        } else {
            console.error(data.pirateMessage);
            return [];
        }
    } catch (error) {
        console.error('Search failed:', error);
        return [];
    }
}

// Usage examples
searchMovies('Prison', null, null);
searchMovies(null, 2, null);
searchMovies(null, null, 'Drama');
searchMovies('Family', 2, 'Crime');
```

### Python Examples

```python
import requests

def search_movies(name=None, id=None, genre=None):
    """Search for movies using the pirate API"""
    params = {}
    if name:
        params['name'] = name
    if id:
        params['id'] = id
    if genre:
        params['genre'] = genre
    
    try:
        response = requests.get('http://localhost:8080/movies/search', params=params)
        data = response.json()
        
        if data['success']:
            print(data['pirateMessage'])
            return data['movies']
        else:
            print(f"Error: {data['pirateMessage']}")
            return []
    except Exception as e:
        print(f"Search failed: {e}")
        return []

# Usage examples
movies = search_movies(name='Prison')
movies = search_movies(id=2)
movies = search_movies(genre='Drama')
movies = search_movies(name='Family', id=2, genre='Crime')
```

## 🚨 Error Handling Best Practices

1. **Always check the `success` field** in JSON responses
2. **Handle both standard and pirate messages** for user-friendly errors
3. **Validate parameters client-side** before making requests
4. **Implement retry logic** for 500 errors (kraken attacks!)
5. **Use appropriate HTTP status code handling**

## 🔒 Rate Limiting

Currently, there be no rate limiting on our API, but don't abuse it, ye scallywag! Future versions may implement rate limiting to keep the seas calm.

## 🏴‍☠️ Pirate Language Guide

Our API includes both standard and pirate-themed messages:

| Standard Term | Pirate Translation |
|---------------|-------------------|
| Movies | Cinematic treasure |
| Search | Hunt for treasure |
| Results | Treasure found |
| Error | Kraken attack |
| Parameters | Search criteria |
| Database | Treasure chest |
| Server | Ship |
| User | Matey/Landlubber |

## 📞 Support

If ye be having trouble with the API, check the application logs for detailed pirate messages:

```bash
tail -f logs/application.log
```

Look for messages starting with "Ahoy!", "Arrr!", or "Blimey!" for search-related information.

---

*"May yer API calls be swift and yer JSON responses be treasure-filled, me hearty!"* 🏴‍☠️