package com.group3.mBaaS.user;

import com.group3.mBaaS.authentication.PBKDF2Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * This is just an example, you can load the user from the database from the repository.
 *
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    public Flux<Mbuser> findByUsername(String username) {
        return userRepository.findAllByUsername(username);
    }

    //create User
    public Mono<Void> createUser (Mbuser mbUser){
        Flux<Mbuser> usernameDuplicate = userRepository.findAllByUsername(mbUser.getUsername());
        LinkedList<Mbuser> users = new LinkedList<>();
        usernameDuplicate.filter(user -> user.getProjectid() == mbUser.getProjectid()).map(user -> users.add(user)).subscribe();
        if(users.size()!= 0){
            return Mono.error(new Exception("The user with the specified project already exists"));
        }
        mbUser.setPassword(passwordEncoder.encode(mbUser.getPassword()));

        try {
            userRepository.save(mbUser).subscribe();
        }catch (Exception e){
            return Mono.error(new Exception("Could not add user"));
        }
        return Mono.empty();

    }


    //findById
    public Mono<Mbuser> findById(Integer id) {
        return userRepository.findById(id);

    }

    //get all users
    public Flux<Mbuser> getAllUsers(Optional<Integer> projectId) {
        return (projectId.isPresent()) ? userRepository.findAllByProjectid(projectId.get()) : userRepository.findAll();
    }
    //updateUser
    public Mono<Void> updateUser(Integer userId, Mbuser newUser){

        try{
            Mono<Mbuser> oldUser = userRepository.findById(userId);
            oldUser.subscribe(mbUser -> {
                mbUser.setUsername(newUser.getUsername());
                mbUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                mbUser.setEnabled(newUser.isEnabled());
                mbUser.setRoles(newUser.getRole());
                userRepository.save(mbUser).subscribe();
            });
            return Mono.empty();
        }catch(Exception e){
            return Mono.error(e);
        }
    }

    public Mono<Void> deleteUserById(Integer id) {
        return userRepository.deleteById(id)
                .then();
    }

    public Mono<Void> deleteUserByUsername(String username) {
        return userRepository.deleteByUsername(username)
                .then();
    }

    //deleteAllUsers
    public Mono<Void> deleteAllUsers() {
        return userRepository.deleteAll().then();

    }
}

