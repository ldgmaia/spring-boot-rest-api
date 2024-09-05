package com.example.api.domain.purchaseorderitems;

import com.example.api.repositories.PurchaseOrderItemRepository;
import com.example.api.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderItemService {

//    @Autowired
//    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

//    @Autowired
//    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public PurchaseOrderItemInfoDTO register(PurchaseOrderItemRegisterDTO data) {
//        validators.forEach(v -> v.validate(data));

        var purchaseOrderItem = new PurchaseOrderItem(data);

        purchaseOrderItemRepository.save(purchaseOrderItem);

        return new PurchaseOrderItemInfoDTO(purchaseOrderItem);
    }
}
