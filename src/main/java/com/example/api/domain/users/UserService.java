package com.example.api.domain.users;

import com.example.api.domain.ValidationException;
import com.example.api.domain.storage.StorageService;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import com.example.api.repositories.StorageLevelRepository;
import com.example.api.repositories.StorageLocationRepository;
import com.example.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageLevelRepository storageLevelRepository;

    @Autowired
    private StorageLocationRepository storageLocationRepository;

    public User register(UserRegisterDTO data) {
        if (userRepository.findByUsername(data.username()) != null) {
            throw new ValidationException("User already registered");
        }

        var user = new User(data);

        var storageLocation = storageLocationRepository.findByName("Technicians");

        var storageLevel = new StorageLevel(user.getUsername(), storageLocation);
        System.out.println("level " + storageLevel.getCreatedBy());
        storageLevelRepository.save(storageLevel);

        user.setStorageLevel(storageLevel);
        userRepository.save(user);

        return user;
    }

//    public void deactivateUser(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getStorageLevel() != null) {
//            storageService.deactivateStorageLevel(user.getStorageLevel());
//        }
//        user.setEnabled(false);
//        userRepository.save(user);
//    }
}
