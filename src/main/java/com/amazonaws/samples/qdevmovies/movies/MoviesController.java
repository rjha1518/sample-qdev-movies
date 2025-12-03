package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "id", required = false) Long id,
                           @RequestParam(value = "genre", required = false) String genre) {
        logger.info("Ahoy! Fetchin' movies with search parameters - name: {}, id: {}, genre: {}", name, id, genre);
        
        List<Movie> movies;
        boolean isSearch = (name != null && !name.trim().isEmpty()) || 
                          (id != null && id > 0) || 
                          (genre != null && !genre.trim().isEmpty());
        
        if (isSearch) {
            movies = movieService.searchMovies(name, id, genre);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            if (movies.isEmpty()) {
                model.addAttribute("noResults", true);
                model.addAttribute("pirateMessage", "Arrr! No treasure found in these waters, matey! Try adjustin' yer search criteria.");
            } else {
                model.addAttribute("pirateMessage", "Ahoy! Found " + movies.size() + " pieces of cinematic treasure for ye!");
            }
        } else {
            movies = movieService.getAllMovies();
            model.addAttribute("searchPerformed", false);
            model.addAttribute("pirateMessage", "Welcome to our treasure chest of movies, ye landlubber!");
        }
        
        model.addAttribute("movies", movies);
        model.addAttribute("allGenres", movieService.getAllGenres());
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }

    /**
     * REST API endpoint for movie search, arrr!
     * Returns JSON response for API consumers who prefer their treasure in JSON format.
     * 
     * @param name Movie name to search for
     * @param id Specific movie ID
     * @param genre Genre to filter by
     * @return ResponseEntity with search results and pirate messages
     */
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMoviesApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("Ahoy! API search request with name: {}, id: {}, genre: {}", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate that at least one search parameter is provided
            boolean hasSearchCriteria = (name != null && !name.trim().isEmpty()) || 
                                       (id != null && id > 0) || 
                                       (genre != null && !genre.trim().isEmpty());
            
            if (!hasSearchCriteria) {
                response.put("success", false);
                response.put("message", "Arrr! Ye need to provide at least one search parameter, ye scurvy dog!");
                response.put("pirateMessage", "Batten down the hatches! No search criteria provided!");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                response.put("success", false);
                response.put("message", "Arrr! Movie ID must be a positive number, matey!");
                response.put("pirateMessage", "That be no valid treasure map number!");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            response.put("success", true);
            response.put("movies", searchResults);
            response.put("totalResults", searchResults.size());
            response.put("searchCriteria", Map.of(
                "name", name != null ? name : "",
                "id", id != null ? id : "",
                "genre", genre != null ? genre : ""
            ));
            
            if (searchResults.isEmpty()) {
                response.put("message", "No movies found matching your search criteria");
                response.put("pirateMessage", "Arrr! No treasure found in these waters, matey! The sea be empty of yer desired films!");
            } else {
                response.put("message", "Found " + searchResults.size() + " movies");
                response.put("pirateMessage", "Ahoy! Discovered " + searchResults.size() + " pieces of cinematic treasure for ye, me hearty!");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Blimey! Error during movie search: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "An error occurred while searching for movies");
            response.put("pirateMessage", "Shiver me timbers! A kraken attacked our search! Try again later, ye brave soul!");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}