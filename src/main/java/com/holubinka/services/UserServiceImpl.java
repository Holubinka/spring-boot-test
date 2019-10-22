package com.holubinka.services;

import com.holubinka.dao.UserDao;
import com.holubinka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<User> create(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return Optional.ofNullable(userDao.save(user));
    }

    @Override
    public Optional<List<User>> getAll() {
        return Optional.ofNullable(userDao.findAll());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDao.findByEmail(email)
                .map(this::toUserDetails)
                .orElseGet(org.springframework.security.core.userdetails.User.builder().disabled(true)::build);
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
