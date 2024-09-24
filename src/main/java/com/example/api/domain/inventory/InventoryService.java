package com.example.api.domain.inventory;

import com.example.api.domain.categories.Category;
import com.example.api.domain.categories.CategoryInfoDTO;
import com.example.api.domain.inventory.validations.InventoryValidator;
import com.example.api.repositories.CategoryRepository;
import com.example.api.repositories.InventoryRepository;
import com.example.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private List<InventoryValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public InventoryInfoDTO register(InventoryRequestDTO data) {

//        System.out.println("data on register service: " + data);
        validators.forEach(v -> v.validate(data));

        // Fetch the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //var currentUser = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        var currentUser = userRepository.findByUsername(username);
        var inventory = new Inventory(new InventoryRegisterDTO(
                data.inventoryId(),
                data.categoryId(),
                data.modelId(),
                data.mpnId(),
                data.conditionId(),
                data.receivingItemId(),
                currentUser,
                data.post(),
                data.byQuantity(),
                data.quantity(),
                data.serialNumber()
        ), currentUser);

        System.out.println("inventory CategoryId on register service: " + inventory.getCategoryId());
        System.out.println("inventory ModelId on register service: " + inventory.getModelId());
        System.out.println("inventory MpnId on register service: " + inventory.getMpnId());

        inventoryRepository.save(inventory);

        return new InventoryInfoDTO(inventory);
    }

    public boolean deleteInventoryItem(Long id) throws IOException {
        // Find the file in the database
        var inventoryItemToDelete = inventoryRepository.findById(id).orElse(null);
        if (inventoryItemToDelete == null) {
            System.err.println("Inventory item not found in the database");
            return false; // Inventory item not found in the database
        } else {
            inventoryRepository.delete(inventoryItemToDelete);
            System.out.println("Inventory item found and Deleted from the database");
        }
        return true;
    }

    public List<CategoryInfoDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CategoryInfoDTO convertToDto(Category category) {
        return new CategoryInfoDTO(category);
    }


//    public InventoryInfoDTO show(Long id) {
//        var inventoryById = receivingRepository.findById(id).orElseThrow(() -> new ValidationException("Receiving not found"));
//        return new ReceivingInfoDTO(receivingById);
//    }

}
