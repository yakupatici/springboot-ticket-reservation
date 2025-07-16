package com.code.springboot.onlineticketbookingsystem.repository;

import com.code.springboot.onlineticketbookingsystem.model.Booking;
import com.code.springboot.onlineticketbookingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // get old reservation of user
    List<Booking> findByUser(User user);
}
