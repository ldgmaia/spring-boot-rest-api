package com.example.api.domain.orders;

import com.example.api.domain.customers.Address;
import com.example.api.domain.customers.AddressRegisterDTO;
import com.example.api.domain.customers.Customer;
import com.example.api.domain.customers.CustomerRegisterDTO;
import com.example.api.infra.utils.JsonPrinter;
import com.example.api.repositories.CustomerRepository;
import com.example.api.repositories.OrderItemRepository;
import com.example.api.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final String REDACTED_TEXT = "REDACTED";

    private static final long REFRESH_INTERVAL = 5L * 60L * 1000L; // 5 minutes in milliseconds

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private EntityManager entityManager;

    @Value("${SHIPSTATION_API_USERNAME}")
    private String apiUsername;

    @Value("${SHIPSTATION_API_PASSWORD}")
    private String apiPassword;

    @Value("${SHIPSTATION_API_URL:https://ssapi.shipstation.com}")
    private String baseUrl;


//    public OrderInfoDTO getOrderByNumber(String orderNumber) {
//
//    }

//    public List<OrderInfoDTO> listOrdersFromLocalDatabase() {
//
//    }


    @Transactional
    @Scheduled(fixedRate = 100000) // 5 minutes
    public void updateOrdersFromShipstation() throws JsonProcessingException {

        ZonedDateTime twoHoursAgo = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(130);
        String modifyDateStart = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(twoHoursAgo);

//        var lastModifiedOrders = fetchFromShipStation("/orders?modifyDateStart=" + modifyDateStart, OrderListResponseDTO.class);
        var lastModifiedOrders = fetchFromShipStation("/orders?orderNumber=test5", OrderListResponseDTO.class);

        JsonPrinter.printJson(lastModifiedOrders);

        lastModifiedOrders.orders()
                .stream().filter(orderData -> !orderData.orderStatus().equals("shipped"))
                .forEach(orderData -> {

                    switch (orderData.orderStatus()) {
                        case "awaiting_shipment":

                            var orderTracking = fetchFromShipStation("/shipments?orderId=" + orderData.orderId(), OrderShipmentResponseDTO.class);
                            var localOrder = orderRepository.findByOrderId(orderData.orderId());
                            var localCustomer = customerRepository.findByCustomerId(orderData.customerId());

                            String trackingNumber = orderTracking.shipments().isEmpty() ? null : orderTracking.shipments().get(0).trackingNumber();

                            // Create or update Address
                            Address address = new Address(new AddressRegisterDTO(
                                    orderData.shipTo().street1(),
                                    orderData.shipTo().street2(),
                                    orderData.shipTo().street3(),
                                    orderData.shipTo().city(),
                                    orderData.shipTo().state(),
                                    orderData.shipTo().postalCode(),
                                    orderData.shipTo().country()
                            ));

                            // Create or update Customer
                            if (localCustomer == null) {
                                localCustomer = new Customer(new CustomerRegisterDTO(
                                        orderData.customerId(),
                                        orderData.shipTo().name(),
                                        orderData.shipTo().company(),
                                        orderData.customerEmail(),
                                        orderData.shipTo().phone(),
                                        orderData.customerUsername(),
                                        address
                                ));
                            } else {
                                if (!orderData.shipTo().name().contains(REDACTED_TEXT)) {
                                    localCustomer.setName(orderData.shipTo().name());
                                    localCustomer.setCompany(orderData.shipTo().company());
                                    localCustomer.setEmail(orderData.customerEmail());
                                    localCustomer.setPhone(orderData.shipTo().phone());
                                    localCustomer.setUsername(orderData.customerUsername());
                                    localCustomer.setAddress(address);
                                }
                            }

                            customerRepository.save(localCustomer);

                            Order updatedOrder = new Order(new OrderRegisterDTO(
                                    orderData.orderId(),
                                    orderData.orderNumber(),
                                    orderData.orderKey(),
                                    orderData.orderDate(),
                                    orderData.createDate(),
                                    orderData.modifyDate(),
                                    orderData.paymentDate(),
                                    orderData.shipByDate(),
                                    orderData.orderStatus(),
                                    orderData.orderTotal(),
                                    orderData.amountPaid(),
                                    orderData.taxAmount(),
                                    orderData.shippingAmount(),
                                    orderData.paymentMethod(),
                                    orderData.carrierCode(),
                                    trackingNumber,
                                    orderData.confirmation(),
                                    orderData.requestedShippingService(),
                                    orderData.serviceCode(),
                                    orderData.packageCode(),
                                    orderData.customerNotes(),
                                    orderData.internalNotes(),
                                    localCustomer
                            ));

                            if (localOrder == null) {
                                orderRepository.save(updatedOrder);
                            } else {
                                BeanUtils.copyProperties(updatedOrder, localOrder, "id", "createdAt");
                                updatedOrder = orderRepository.save(localOrder);
                            }

                            orderItemService.upsertOrderItems(orderData.items(), updatedOrder);
                            break;
                        case "shipped":
                            // change status of order, items, and components
                            break;
                        case "cancelled":
                            /**
                             * Remove items from “order checklist”. Discuss with Adolfo
                             * Where items should be located after cancelation? Discuss with Adolfo
                             * Change status of items and components
                             * Change status of order
                             * */
                            break;
                        case "on_hold":
                            // change status of order, items, and components
                            break;
                        default:
                            break;
                    }
                });
    }

    public <T> T fetchFromShipStation(String urlModule, Class<T> responseType) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            String credentials = apiUsername + ":" + apiPassword;
//            System.out.println((credentials));
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

            headers.set("Authorization", basicAuth);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<T> purchaseOrder = restTemplate.exchange(baseUrl + urlModule, HttpMethod.GET, entity, responseType);
            return purchaseOrder.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error while calling executeQuery :: " + e.getMessage());
        }
    }
}
