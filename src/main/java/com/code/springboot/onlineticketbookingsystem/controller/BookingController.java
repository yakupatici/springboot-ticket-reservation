package com.code.springboot.onlineticketbookingsystem.controller;

import com.code.springboot.onlineticketbookingsystem.model.Booking;
import com.code.springboot.onlineticketbookingsystem.model.Show;
import com.code.springboot.onlineticketbookingsystem.model.User;
import com.code.springboot.onlineticketbookingsystem.repository.BookingRepository;
import com.code.springboot.onlineticketbookingsystem.repository.ShowRepository;
import com.code.springboot.onlineticketbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(bookingRepository.findByUser(user));
    }

    // 🧾 Book a ticket for a show
    @PostMapping("/show/{showId}")
    @Transactional
    public ResponseEntity<?> bookShow(
            @PathVariable Long showId,
            @RequestParam int seats,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
            Show show = showRepository.findById(showId).orElseThrow();

            if (seats <= 0) {
                return ResponseEntity.badRequest().body("Koltuk sayısı 0'dan büyük olmalı.");
            }

            if (seats > show.getTotalSeats()) {
                return ResponseEntity.badRequest().body("Yeterli koltuk yok.");
            }

            Booking booking = Booking.builder()
                    .user(user)
                    .show(show)
                    .seatsBooked(seats)
                    .bookingTime(LocalDateTime.now())
                    .build();

            show.setTotalSeats(show.getTotalSeats() - seats);
            showRepository.save(show);

            Booking savedBooking = bookingRepository.save(booking);

            return ResponseEntity.ok(savedBooking);
        } catch (ObjectOptimisticLockingFailureException e) {
            // Koltuk güncelleme çakışması oldu
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Koltuklar güncellendi, lütfen tekrar deneyin.");
        }
    }
}