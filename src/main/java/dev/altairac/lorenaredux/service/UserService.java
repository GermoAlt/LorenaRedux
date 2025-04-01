package dev.altairac.lorenaredux.service;

import dev.altairac.lorenaredux.model.User;
import dev.altairac.lorenaredux.repository.UserRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateUser(Member memberById) {
        User user = userRepository.findById(memberById.getIdLong()).orElse(new User().initFromMember(memberById));
        userRepository.save(user);
    }

    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
