package com.code.springboot.onlineticketbookingsystem.controller;

import com.code.springboot.onlineticketbookingsystem.model.Show;
import com.code.springboot.onlineticketbookingsystem.repository.ShowRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@SecurityRequirement(name = "basicAuth")
public class ShowController {
    @Autowired
    // ShowRepository: Repository interface for accessing Show entities in the database
    // It extends JpaRepository to provide basic CRUD operations
    private final ShowRepository showRepository;

    public ShowController(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }
    // @GetMapping: Handles HTTP GET requests to fetch all shows
    // This method returns a list of all Show entities in the system
    // No authentication or authorization is required to access this endpoint
    @GetMapping
    public ResponseEntity<List<Show>> getAllShows(){
        return ResponseEntity.ok(showRepository.findAll());
    }

    // @PostMapping: Maps HTTP POST requests to this method
    // Used to create a new Show entity
    @PostMapping
    // @PreAuthorize: Only users with the 'ADMIN' role can access this endpoint
  //  @PreAuthorize("hasRole('ADMIN')")

    // @Valid: Ensures that the input Show object is validated according to its constraints
    // @RequestBody: Maps the HTTP request body to the Show object
    public ResponseEntity<Show> createShow(@Valid @RequestBody Show show) {
        return ResponseEntity.ok(showRepository.save(show));
    }


}
