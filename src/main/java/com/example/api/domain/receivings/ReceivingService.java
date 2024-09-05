package com.example.api.domain.receivings;

import com.example.api.repositories.PurchaseOrderRepository;
import com.example.api.repositories.ReceivingRepository;
import com.example.api.repositories.SupplierRepository;
import com.example.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReceivingService {

    @Autowired
    private ReceivingRepository receivingRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private UserRepository userRepository;

    public ReceivingInfoDTO register(ReceivingRequestDTO data) {

        System.out.println("data " + data);

        System.out.println("purchase order id: " + data.purchaseOrderId());

        // Fetch the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        var receiving = new Receiving(new ReceivingRegisterDTO(
                data.trackingCode(),
                data.type(),
                data.identifier(),
                supplierRepository.getReferenceById(data.supplierId()),
                purchaseOrderRepository.getReferenceById(data.purchaseOrderId()),
                data.carrier(),
                data.notes()
        ), currentUser);
        receivingRepository.save(receiving);

        // create the receiving record to get its ID
        System.out.println("receiving ID " + receiving.getId());


        // create the receiving items record


        return new ReceivingInfoDTO(receiving);
    }
}
