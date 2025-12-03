package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "The Prison Escape", "John Director", 1994, "Drama", "Test description", 142, 5.0),
                    new Movie(2L, "The Family Boss", "Michael Filmmaker", 1972, "Crime/Drama", "Test description", 175, 5.0),
                    new Movie(3L, "The Masked Hero", "Chris Moviemaker", 2008, "Action/Crime", "Test description", 152, 5.0)
                );
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == 1L) {
                    return Optional.of(new Movie(1L, "The Prison Escape", "John Director", 1994, "Drama", "Test description", 142, 5.0));
                }
                return Optional.empty();
            }
            
            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> allMovies = getAllMovies();
                List<Movie> results = new ArrayList<>();
                
                for (Movie movie : allMovies) {
                    boolean matches = true;
                    
                    if (id != null && id > 0) {
                        matches = matches && movie.getId() == id;
                    }
                    
                    if (name != null && !name.trim().isEmpty()) {
                        matches = matches && movie.getMovieName().toLowerCase().contains(name.toLowerCase());
                    }
                    
                    if (genre != null && !genre.trim().isEmpty()) {
                        matches = matches && movie.getGenre().toLowerCase().contains(genre.toLowerCase());
                    }
                    
                    if (matches) {
                        results.add(movie);
                    }
                }
                
                return results;
            }
            
            @Override
            public List<String> getAllGenres() {
                return Arrays.asList("Drama", "Crime/Drama", "Action/Crime");
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    public void testGetMoviesWithoutSearch() {
        String result = moviesController.getMovies(model, null, null, null);
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(3, movies.size());
        assertEquals(false, model.getAttribute("searchPerformed"));
        assertNotNull(model.getAttribute("pirateMessage"));
    }

    @Test
    public void testGetMoviesWithNameSearch() {
        String result = moviesController.getMovies(model, "Prison", null, null);
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("The Prison Escape", movies.get(0).getMovieName());
        assertEquals(true, model.getAttribute("searchPerformed"));
        assertEquals("Prison", model.getAttribute("searchName"));
    }

    @Test
    public void testGetMoviesWithIdSearch() {
        String result = moviesController.getMovies(model, null, 2L, null);
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("The Family Boss", movies.get(0).getMovieName());
        assertEquals(true, model.getAttribute("searchPerformed"));
        assertEquals(2L, model.getAttribute("searchId"));
    }

    @Test
    public void testGetMoviesWithGenreSearch() {
        String result = moviesController.getMovies(model, null, null, "Drama");
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(2, movies.size()); // Drama and Crime/Drama
        assertEquals(true, model.getAttribute("searchPerformed"));
        assertEquals("Drama", model.getAttribute("searchGenre"));
    }

    @Test
    public void testGetMoviesWithNoResults() {
        String result = moviesController.getMovies(model, "NonExistentMovie", null, null);
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(0, movies.size());
        assertEquals(true, model.getAttribute("noResults"));
        assertNotNull(model.getAttribute("pirateMessage"));
        assertTrue(((String) model.getAttribute("pirateMessage")).contains("Arrr! No treasure found"));
    }

    @Test
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
    }

    @Test
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
    }

    // REST API Tests
    @Test
    public void testSearchMoviesApiSuccess() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("Prison", null, null);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(true, body.get("success"));
        assertEquals(1, body.get("totalResults"));
        assertNotNull(body.get("pirateMessage"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size());
        assertEquals("The Prison Escape", movies.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesApiNoResults() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("NonExistent", null, null);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(true, body.get("success"));
        assertEquals(0, body.get("totalResults"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(0, movies.size());
        
        String pirateMessage = (String) body.get("pirateMessage");
        assertTrue(pirateMessage.contains("Arrr! No treasure found"));
    }

    @Test
    public void testSearchMoviesApiNoCriteria() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, null, null);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(false, body.get("success"));
        
        String message = (String) body.get("message");
        assertTrue(message.contains("Arrr! Ye need to provide at least one search parameter"));
    }

    @Test
    public void testSearchMoviesApiEmptyStrings() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("", null, "");
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(false, body.get("success"));
    }

    @Test
    public void testSearchMoviesApiInvalidId() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, -1L, null);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(false, body.get("success"));
        
        String message = (String) body.get("message");
        assertTrue(message.contains("Arrr! Movie ID must be a positive number"));
    }

    @Test
    public void testSearchMoviesApiWithMultipleCriteria() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("Family", 2L, "Crime");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(true, body.get("success"));
        assertEquals(1, body.get("totalResults"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size());
        assertEquals("The Family Boss", movies.get(0).getMovieName());
    }

    @Test
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(3, movies.size());
        assertEquals("The Prison Escape", movies.get(0).getMovieName());
    }
}
