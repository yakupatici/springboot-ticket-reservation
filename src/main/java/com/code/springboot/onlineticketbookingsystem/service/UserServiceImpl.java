package com.code.springboot.onlineticketbookingsystem.service;

import com.code.springboot.onlineticketbookingsystem.model.Role;
import com.code.springboot.onlineticketbookingsystem.model.User;
import com.code.springboot.onlineticketbookingsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

// Bu sınıf, UserService arayüzünü uygular ve kullanıcı kayıt/login işlemlerini gerçekleştirir.
// @Service anotasyonu, bu sınıfın Spring tarafından servis olarak yönetilmesini sağlar.
// Bağımlılıklar constructor üzerinden enjekte edilir (dependency injection).
@Service
// Lombok'un @RequiredArgsConstructor anotasyonu, final alanlar için constructor oluşturur.
// Böylece manuel olarak constructor yazmamıza gerek kalmaz.
// Spring bu constructor'ı kullanarak bağımlılıkları (userRepository ve passwordEncoder) inject eder.
// Bu, @Autowired kullanmadan dependency injection yapmanın daha temiz ve güvenli yoludur.
public class UserServiceImpl implements UserService, UserDetailsService {

    // UserRepository, veritabanı işlemlerini gerçekleştirmek için kullanılır (dependency).
    private final UserRepository userRepository;
    // PasswordEncoder, şifreleri güvenli biçimde hash'lemek için kullanılır.
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerAdmin(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }
}
