package com.example.api.controller;

import com.example.api.domain.orders.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@SecurityRequirement(name = "bearer-key")
public class OrderController {

    // I didn't check this file

    @Autowired
    private OrderService orderService;

//    @GetMapping("/{orderNumber}")
//    public ResponseEntity<OrderListResponseDTO> getOrderByNumber(@PathVariable String orderNumber) {
//        OrderListResponseDTO orderData = orderService.getOrderByOrderNumber(orderNumber);
//        return ResponseEntity.ok(orderData);
//    }

//    @GetMapping
//    public ResponseEntity<List<OrderInfoDTO>> listOrders() {
//        List<OrderInfoDTO> orders = orderService.listOrdersFromLocalDatabase();
//        return ResponseEntity.ok(orders);
//    }

//    @PostMapping("/sync")
//    public ResponseEntity<String> syncOrders() {
//        orderService.updateOrdersFromShipstation();
//        return ResponseEntity.ok("Order synchronization initiated");
//    }
}
