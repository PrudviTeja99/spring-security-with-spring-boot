package com.teja.securedapis.service.impl;

import com.teja.securedapis.entity.UserEntity;
import com.teja.securedapis.model.User;
import com.teja.securedapis.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        User userModel = null;
        if(optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();
            userModel = new com.teja.securedapis.model.User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(),userEntity.get);
        }
        else{
            throw new UsernameNotFoundException("Unable to find username");
        }
        return userModel;
    }

    public UserEntity saveUserDetails(UserEntity userEntity){
        Optional<UserEntity> optionalUser = userRepository.findByUsername(userEntity.getUsername());
        if(optionalUser.isEmpty()){
            return userRepository.save(userEntity);
        }
        else{
            throw new RuntimeException("Conflict");
        }

    }
}
