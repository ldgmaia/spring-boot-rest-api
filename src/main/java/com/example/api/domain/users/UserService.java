package com.example.api.domain.users;

import com.example.api.domain.ValidationException;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import com.example.api.repositories.StorageLevelRepository;
import com.example.api.repositories.StorageLocationRepository;
import com.example.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private StorageService storageService;

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
        storageLevelRepository.save(storageLevel);

        user.setStorageLevel(storageLevel);
        userRepository.save(user);

        return user;
    }

    public List<UserInfoDTO> list() {
        return userRepository.findByEnabledTrue().stream()
                .map(UserInfoDTO::new)
                .toList();
    }

    public UserInfoDTO getById(Long id) {
        return new UserInfoDTO(userRepository.findByIdAndEnabledTrue(id));
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
