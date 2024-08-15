package com.codeWithHimanshu.journalApp.service;

import com.codeWithHimanshu.journalApp.entity.JournalEntry;
import com.codeWithHimanshu.journalApp.entity.User;
import com.codeWithHimanshu.journalApp.repository.JournalEntryRepository;
import com.codeWithHimanshu.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {


    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveEntry(User user){
        userRepository.save(user);
    }

    public void saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        }
        catch (Exception e){
            System.out.println("Exception : "+e);
            log.error("Error occurred for {}",user.getUserName());
            log.trace("Error occurred for {}",user.getUserName());
            log.info("Error occurred for {}",user.getUserName());
            log.debug("Error occurred for {}",user.getUserName());
        }
    }

    public void saveNewAdminUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }
    public Optional<User> getById(ObjectId myId){
        return userRepository.findById(myId);
    }
    public void deleteById(ObjectId myId){
        userRepository.deleteById(myId);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
