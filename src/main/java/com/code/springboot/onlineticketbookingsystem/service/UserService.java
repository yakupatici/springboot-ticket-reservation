package com.code.springboot.onlineticketbookingsystem.service;

import com.code.springboot.onlineticketbookingsystem.model.User;
import jakarta.transaction.Transactional;

public interface UserService {
    User registerUser(User user);
    User registerAdmin(User user);
    User findByEmail(String email);


}
