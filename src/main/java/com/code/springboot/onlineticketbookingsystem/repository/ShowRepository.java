package com.code.springboot.onlineticketbookingsystem.repository;

import com.code.springboot.onlineticketbookingsystem.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository  extends JpaRepository<Show, Long> {

}
