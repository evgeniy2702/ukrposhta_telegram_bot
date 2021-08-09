package com.telegram_bot.Service;

import com.telegram_bot.model.User;
import com.telegram_bot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    public User findUserByChatId(long chatId){
        return userRepository.findByChatId(chatId);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> findNewUsers(){
        List<User> users = userRepository.findNewUsers();

        users.forEach(user -> user.setNotified(true));
        return users;
    }

    @Transactional
    public void addUser(User user){
        user.setRegistration(true);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user){
        userRepository.save(user);
    }

}
