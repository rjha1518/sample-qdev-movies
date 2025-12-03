package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MovieService, focusing on the new search functionality, arrr!
 * These tests ensure our treasure hunting methods work ship shape.
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        assertTrue(movies.size() > 0);
    }

    @Test
    public void testGetMovieById() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent());
        assertEquals(1L, movie.get().getId());
        assertEquals("The Prison Escape", movie.get().getMovieName());
    }

    @Test
    public void testGetMovieByIdNotFound() {
        Optional<Movie> movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent());
    }

    @Test
    public void testGetMovieByIdNull() {
        Optional<Movie> movie = movieService.getMovieById(null);
        assertFalse(movie.isPresent());
    }

    @Test
    public void testGetMovieByIdZero() {
        Optional<Movie> movie = movieService.getMovieById(0L);
        assertFalse(movie.isPresent());
    }

    @Test
    public void testGetMovieByIdNegative() {
        Optional<Movie> movie = movieService.getMovieById(-1L);
        assertFalse(movie.isPresent());
    }

    // Search functionality tests - Arrr, the treasure hunting begins!
    
    @Test
    public void testSearchMoviesByNameExact() {
        List<Movie> results = movieService.searchMovies("The Prison Escape", null, null);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByNamePartial() {
        List<Movie> results = movieService.searchMovies("Prison", null, null);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByNameCaseInsensitive() {
        List<Movie> results = movieService.searchMovies("prison", null, null);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByNameNotFound() {
        List<Movie> results = movieService.searchMovies("NonExistentMovie", null, null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesByNameEmpty() {
        List<Movie> results = movieService.searchMovies("", null, null);
        // Should return all movies when name is empty
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesByNameNull() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        // Should return all movies when name is null
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesByNameWhitespace() {
        List<Movie> results = movieService.searchMovies("   ", null, null);
        // Should return all movies when name is only whitespace
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesById() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByIdNotFound() {
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesByIdNull() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        // Should return all movies when id is null
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesByIdZero() {
        List<Movie> results = movieService.searchMovies(null, 0L, null);
        // Should return all movies when id is 0 (invalid)
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesByIdNegative() {
        List<Movie> results = movieService.searchMovies(null, -1L, null);
        // Should return all movies when id is negative (invalid)
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesByGenreExact() {
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        assertFalse(results.isEmpty());
        // Should find movies with "Drama" genre (including "Crime/Drama")
        assertTrue(results.stream().anyMatch(movie -> movie.getGenre().contains("Drama")));
    }

    @Test
    public void testSearchMoviesByGenrePartial() {
        List<Movie> results = movieService.searchMovies(null, null, "Crime");
        assertFalse(results.isEmpty());
        // Should find movies with "Crime" in genre
        assertTrue(results.stream().allMatch(movie -> movie.getGenre().toLowerCase().contains("crime")));
    }

    @Test
    public void testSearchMoviesByGenreCaseInsensitive() {
        List<Movie> results = movieService.searchMovies(null, null, "drama");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(movie -> movie.getGenre().toLowerCase().contains("drama")));
    }

    @Test
    public void testSearchMoviesByGenreNotFound() {
        List<Movie> results = movieService.searchMovies(null, null, "NonExistentGenre");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesByGenreEmpty() {
        List<Movie> results = movieService.searchMovies(null, null, "");
        // Should return all movies when genre is empty
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesByGenreNull() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        // Should return all movies when genre is null
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesByGenreWhitespace() {
        List<Movie> results = movieService.searchMovies(null, null, "   ");
        // Should return all movies when genre is only whitespace
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    // Combined search criteria tests - Multiple treasure map coordinates!
    
    @Test
    public void testSearchMoviesWithMultipleCriteria() {
        List<Movie> results = movieService.searchMovies("Family", 2L, "Crime");
        assertEquals(1, results.size());
        Movie movie = results.get(0);
        assertEquals(2L, movie.getId());
        assertTrue(movie.getMovieName().contains("Family"));
        assertTrue(movie.getGenre().contains("Crime"));
    }

    @Test
    public void testSearchMoviesWithConflictingCriteria() {
        // Search for ID 1 but name that doesn't match ID 1
        List<Movie> results = movieService.searchMovies("Family", 1L, null);
        assertTrue(results.isEmpty()); // Should find no matches
    }

    @Test
    public void testSearchMoviesWithPartialMatches() {
        List<Movie> results = movieService.searchMovies("The", null, "Drama");
        assertFalse(results.isEmpty());
        // Should find movies that have "The" in name AND "Drama" in genre
        assertTrue(results.stream().allMatch(movie -> 
            movie.getMovieName().contains("The") && 
            movie.getGenre().toLowerCase().contains("drama")));
    }

    // Genre listing tests
    
    @Test
    public void testGetAllGenres() {
        List<String> genres = movieService.getAllGenres();
        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        
        // Should be sorted and unique
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i).compareTo(genres.get(i-1)) >= 0, 
                "Genres should be sorted alphabetically");
        }
        
        // Should contain expected genres from the test data
        assertTrue(genres.contains("Drama"));
        assertTrue(genres.contains("Crime/Drama"));
        assertTrue(genres.contains("Action/Crime"));
    }

    @Test
    public void testGetAllGenresUnique() {
        List<String> genres = movieService.getAllGenres();
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(genres.size(), uniqueCount, "All genres should be unique");
    }

    // Edge case tests - Testing the stormy seas!
    
    @Test
    public void testSearchMoviesAllParametersNull() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesAllParametersEmpty() {
        List<Movie> results = movieService.searchMovies("", null, "");
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    public void testSearchMoviesSpecialCharacters() {
        List<Movie> results = movieService.searchMovies("@#$%", null, null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesVeryLongString() {
        String longString = "a".repeat(1000);
        List<Movie> results = movieService.searchMovies(longString, null, null);
        assertTrue(results.isEmpty());
    }
}