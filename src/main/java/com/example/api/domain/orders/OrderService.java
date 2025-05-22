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
    private EntityManager entityManager;

    @Value("${SHIPSTATION_API_USERNAME}")
    private String apiUsername;

    @Value("${SHIPSTATION_API_PASSWORD}")
    private String apiPassword;

    @Value("${SHIPSTATION_API_URL:https://ssapi.shipstation.com}")
    private String baseUrl;

    public OrderListResponseDTO getOrderByOrderNumber(String orderNumber) {
        OrderListResponseDTO orderData = fetchFromShipStation("/orders?orderNumber=" + orderNumber, OrderListResponseDTO.class);
        return orderData;
    }

//    public OrderInfoDTO getOrderByNumber(String orderNumber) {
//        Optional<Order> localOrder = orderRepository.findByOrderNumber(orderNumber);
//
//        if (localOrder.isPresent()) {
//            return mapToOrderInfoDTO(localOrder.get());
//        }
//
//        try {
//            String response = shipstationService.getOrderById(orderNumber);
//            JsonNode orderJson = objectMapper.readTree(response);
//
//            if (orderJson != null && !orderJson.isEmpty()) {
//                Order savedOrder = saveOrderFromShipstationJson(orderJson);
//                if (savedOrder != null) {
//                    return mapToOrderInfoDTO(savedOrder);
//                }
//            }
//        } catch (IOException e) {
//            logger.error("Error fetching order from ShipStation: {}", e.getMessage());
//        }
//
//        return null;
//    }

//    public List<OrderInfoDTO> listOrdersFromLocalDatabase() {
//        try {
//            List<Order> orders = orderRepository.findAll();
//
//            List<OrderInfoDTO> result = new ArrayList<>();
//            for (Order order : orders) {
//                try {
//                    OrderInfoDTO dto = mapToOrderInfoDTO(order);
//                    result.add(dto);
//                } catch (Exception e) {
//                    // Log error but continue processing other orders
//                    logger.error("Error mapping order ID {} to DTO: {}", order.getId(), e.getMessage());
//                }
//            }
//
//            return result;
//        } catch (Exception e) {
//            logger.error("Error listing orders from database: {}", e.getMessage());
//            return new ArrayList<>(); // Return empty list instead of null
//        }
//    }


//    public List<OrderInfoDTO> listOrdersFromLocalDatabase() {
//        List<Order> orders = orderRepository.findAll();
//        return orders.stream()
//                .map(this::mapToOrderInfoDTO)
//                .toList();
//    }

    @Transactional
    @Scheduled(fixedRate = 100000) // 5 minutes
    public void updateOrdersFromShipstation() throws JsonProcessingException {

//        try {

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
                            //create or update
                            var orderTracking = fetchFromShipStation("/shipments?orderId=" + orderData.orderId(), OrderShipmentResponseDTO.class);
                            String trackingNumber = orderTracking.shipments().isEmpty() ? null : orderTracking.shipments().get(0).trackingNumber();

                            var localOrder = orderRepository.findByOrderId(orderData.orderId());


//                            var customerData = fetchFromShipStation("/customers/" + orderData.customerId(), CustomerResponseDTO.class);
                            var localCustomer = customerRepository.findByCustomerId(orderData.customerId());

//                            // Validate marketplace usernames
//                            String marketplaceUsername = customerData.marketplaceUsernames() != null && !customerData.marketplaceUsernames().isEmpty()
//                                    ? customerData.marketplaceUsernames().get(0).username()
//                                    : null;

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
                                localCustomer.setName(orderData.shipTo().name());
                                localCustomer.setCompany(orderData.shipTo().company());
                                localCustomer.setEmail(orderData.customerEmail());
                                localCustomer.setPhone(orderData.shipTo().phone());
                                localCustomer.setUsername(orderData.customerUsername());
                                localCustomer.setAddress(address);
                            }

                            customerRepository.save(localCustomer);

                            var neworder = new Order(new OrderRegisterDTO(
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
                                orderRepository.save(neworder);
                            } else {
                                // Copy all properties from the updated order to the existing one
                                BeanUtils.copyProperties(neworder, localOrder, "id", "createdAt");

                                orderRepository.save(localOrder);
                            }
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


//            return clientFactory.call();
//
//
//
//            String response = shipstationService.getOrdersModifiedInLast2Hours();
//            JsonNode json = objectMapper.readTree(response);
//            JsonNode orders = json.get("orders");
//
//            if (orders != null && orders.isArray()) {
//                int newOrdersCount = 0;
//                int updatedOrdersCount = 0;
//                int skippedRedactedCount = 0;
//                int errorCount = 0;
//
//                for (JsonNode orderJson : orders) {
//                    try {
//                        // Process each order in a separate transaction
//                        String orderNumber = orderJson.get("orderNumber").asText();
//                        Optional<Order> existingOrder = orderRepository.findByOrderNumber(orderNumber);
//
//                        // Use a separate method for transaction isolation
//                        Order order = processSingleOrder(orderJson);
//
//                        if (order != null) {
//                            if (existingOrder.isPresent()) {
//                                updatedOrdersCount++;
//                            } else {
//                                newOrdersCount++;
//                            }
//                        } else {
//                            if (orderJson.has("shipTo") &&
//                                    isDataRedacted(orderJson.path("shipTo").path("name").asText(""))) {
//                                skippedRedactedCount++;
//                            } else {
//                                errorCount++;
//                            }
//                        }
//                    } catch (Exception e) {
//                        logger.error("Error processing order: {}", e.getMessage());
//                        errorCount++;
//                    }
//                }
//
//                logger.info("ShipStation sync completed: {} new orders, {} updated, {} skipped due to redacted info, {} errors",
//                        newOrdersCount, updatedOrdersCount, skippedRedactedCount, errorCount);
//            }
//        } catch (IOException e) {
//            logger.error("Error updating orders from ShipStation: {}", e.getMessage());
//        }
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public Order processSingleOrder(JsonNode orderJson) {
//        return saveOrderFromShipstationJson(orderJson);
//    }

//    @Transactional(rollbackFor = Exception.class)
//    public Order saveOrderFromShipstationJson(JsonNode orderJson) {
//        try {
//            // Extract customer info
//            JsonNode shipTo = orderJson.get("shipTo");
//
//            String customerName = shipTo.get("name").asText("");
//            String streetAddress = shipTo.get("street1").asText("") + " " + shipTo.get("street2").asText("");
//            String phone = shipTo.get("phone").asText("");
//            String email = orderJson.path("customerEmail").asText("");
//            String username = orderJson.path("customerUsername").asText("");
//            String giftMessage = orderJson.path("giftMessage").asText("");
//
//            if (isDataRedacted(customerName) ||
//                    isDataRedacted(streetAddress) ||
//                    isDataRedacted(phone) ||
//                    isDataRedacted(email) ||
//                    isDataRedacted(username) ||
//                    isDataRedacted(giftMessage)) {
//
//                String orderNumber = orderJson.get("orderNumber").asText();
//                Optional<Order> existingOrder = orderRepository.findByOrderNumber(orderNumber);
//
//                if (existingOrder.isPresent()) {
//                    Order order = existingOrder.get();
//                    if (order.getCustomer() != null && !isDataRedacted(order.getCustomer().getName()) &&
//                            order.getCustomer().getAddress() != null && !isDataRedacted(order.getCustomer().getAddress().toString()) &&
//                            !isDataRedacted(order.getCustomer().getPhone())) {
//                        return order;
//                    }
//                }
//
//                logger.info("Skipping order with redacted data: {}", orderJson.get("orderNumber").asText());
//                return null;
//            }
//
//            // Create or get customer (in a separate transaction)
//            Customer customer;
//            try {
//                customer = getOrCreateCustomer(username, customerName, email, phone);
//                if (customer == null || customer.getId() == null) {
//                    logger.error("Failed to create or retrieve a valid customer");
//                    return null;
//                }
//            } catch (Exception e) {
//                logger.error("Customer creation failed: {}", e.getMessage());
//                return null;
//            }
//
//            // Process order
//            String orderNumber = orderJson.get("orderNumber").asText();
//            Optional<Order> existingOrderOpt = orderRepository.findByOrderNumber(orderNumber);
//            Order order;
//
//            if (existingOrderOpt.isPresent()) {
//                order = existingOrderOpt.get();
//                logger.info("Updating existing order: {}", orderNumber);
//            } else {
//                order = new Order();
//                order.setOrderNumber(orderNumber);
//                logger.info("Creating new order: {}", orderNumber);
//            }
//
//            // Set basic order properties
//            order.setOrderId(orderJson.get("orderId").asText());
//            order.setOrderKey(orderJson.path("orderKey").asText(""));
//
//            // Parse dates with improved handling
//            try {
//                if (orderJson.has("orderDate") && !orderJson.get("orderDate").isNull()) {
//                    String orderDateStr = orderJson.get("orderDate").asText();
//                    LocalDateTime orderDate = parseShipstationDateTime(orderDateStr);
//                    order.setOrderDate(orderDate);
//                    logger.debug("Parsed orderDate: {} -> {}", orderDateStr, orderDate);
//                }
//
//                if (orderJson.has("createDate") && !orderJson.get("createDate").isNull()) {
//                    String createDateStr = orderJson.get("createDate").asText();
//                    LocalDateTime createDate = parseShipstationDateTime(createDateStr);
//                    order.setCreateDate(createDate);
//                    logger.debug("Parsed createDate: {} -> {}", createDateStr, createDate);
//                }
//
//                if (orderJson.has("modifyDate") && !orderJson.get("modifyDate").isNull()) {
//                    String modifyDateStr = orderJson.get("modifyDate").asText();
//                    LocalDateTime modifyDate = parseShipstationDateTime(modifyDateStr);
//                    order.setModifyDate(modifyDate);
//                    logger.debug("Parsed modifyDate: {} -> {}", modifyDateStr, modifyDate);
//                }
//
//                if (orderJson.has("paymentDate") && !orderJson.get("paymentDate").isNull()) {
//                    String paymentDateStr = orderJson.get("paymentDate").asText();
//                    LocalDateTime paymentDate = parseShipstationDateTime(paymentDateStr);
//                    order.setPaymentDate(paymentDate);
//                    logger.debug("Parsed paymentDate: {} -> {}", paymentDateStr, paymentDate);
//                }
//
//                if (orderJson.has("shipByDate") && !orderJson.get("shipByDate").isNull()) {
//                    String shipByDateStr = orderJson.get("shipByDate").asText();
//                    LocalDateTime shipByDate = parseShipstationDateTime(shipByDateStr);
//                    order.setShipByDate(shipByDate);
//                    logger.debug("Parsed shipByDate: {} -> {}", shipByDateStr, shipByDate);
//                }
//            } catch (Exception e) {
//                logger.warn("Error parsing dates, continuing with order processing: {}", e.getMessage());
//                // Print the date that caused the exception for debugging
//                if (orderJson.has("orderDate"))
//                    logger.debug("orderDate raw value: {}", orderJson.get("orderDate").asText());
//                if (orderJson.has("createDate"))
//                    logger.debug("createDate raw value: {}", orderJson.get("createDate").asText());
//                if (orderJson.has("modifyDate"))
//                    logger.debug("modifyDate raw value: {}", orderJson.get("modifyDate").asText());
//                if (orderJson.has("paymentDate"))
//                    logger.debug("paymentDate raw value: {}", orderJson.get("paymentDate").asText());
//                if (orderJson.has("shipByDate"))
//                    logger.debug("shipByDate raw value: {}", orderJson.get("shipByDate").asText());
//            }
//
//            order.setOrderStatus(orderJson.path("orderStatus").asText(""));
//            order.setCustomer(customer);
//            order.setBillToCustomer(customer);
//            order.setShipToCustomer(customer);
//
//            try {
//                if (orderJson.has("orderTotal")) {
//                    order.setOrderTotal(new BigDecimal(orderJson.get("orderTotal").asText("0")));
//                }
//                if (orderJson.has("amountPaid")) {
//                    order.setAmountPaid(new BigDecimal(orderJson.get("amountPaid").asText("0")));
//                }
//                if (orderJson.has("taxAmount")) {
//                    order.setTaxAmount(new BigDecimal(orderJson.get("taxAmount").asText("0")));
//                }
//                if (orderJson.has("shippingAmount")) {
//                    order.setShippingAmount(new BigDecimal(orderJson.get("shippingAmount").asText("0")));
//                }
//            } catch (Exception e) {
//                logger.warn("Error parsing financial data, continuing: {}", e.getMessage());
//            }
//
//            order.setPaymentMethod(orderJson.path("paymentMethod").asText(""));
//            order.setRequestedShippingService(orderJson.path("requestedShippingService").asText(""));
//            order.setCarrierCode(orderJson.path("carrierCode").asText(""));
//            order.setServiceCode(orderJson.path("serviceCode").asText(""));
//            order.setPackageCode(orderJson.path("packageCode").asText(""));
//            order.setConfirmation(orderJson.path("confirmation").asText(""));
//
//            order.setCustomerNotes(orderJson.path("customerNotes").asText(""));
//            order.setInternalNotes(orderJson.path("internalNotes").asText(""));
//
//            order.setTrackingNumber(orderJson.path("trackingNumber").asText(""));
//
//            // Save the order first to get an ID
//            Order savedOrder = orderRepository.save(order);
//            logger.info("Basic order saved: {} with ID: {}", savedOrder.getOrderNumber(), savedOrder.getId());
//
//            // Process related entities in separate try-catch blocks to avoid rollback
//            boolean hasError = false;
//
//            try {
//                processOrderItems(savedOrder, orderJson.path("items"));
//            } catch (Exception e) {
//                logger.error("Error processing order items: {}", e.getMessage());
//                hasError = true;
//            }
//
//            try {
//                processInsuranceOption(savedOrder, orderJson.path("insuranceOptions"));
//            } catch (Exception e) {
//                logger.error("Error processing insurance options: {}", e.getMessage());
//                hasError = true;
//            }
//
//            try {
//                processInternationalOption(savedOrder, orderJson.path("internationalOptions"));
//            } catch (Exception e) {
//                logger.error("Error processing international options: {}", e.getMessage());
//                hasError = true;
//            }
//
//            // Final save
//            Order finalOrder = orderRepository.save(savedOrder);
//
//            if (hasError) {
//                logger.warn("Order saved but with some related entity errors: {}", orderNumber);
//            } else {
//                logger.info("Order and all related entities saved successfully: {}", orderNumber);
//            }
//
//            return finalOrder;
//        } catch (Exception e) {
//            logger.error("Error saving order from JSON: {}", e.getMessage(), e);
//            return null;
//        }
//    }

//    private LocalDateTime parseShipstationDateTime(String dateTimeString) {
//        if (dateTimeString == null || dateTimeString.isEmpty()) {
//            return null;
//        }
//
//        logger.debug("Attempting to parse date: {}", dateTimeString);
//
//        try {
//            // First attempt: Standard ISO format
//            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
//        } catch (Exception e1) {
//            logger.debug("Failed standard ISO parse: {}", e1.getMessage());
//
//            try {
//                // Second attempt: Handle fractional seconds
//                if (dateTimeString.contains(".")) {
//                    String[] parts = dateTimeString.split("\\.");
//                    if (parts.length == 2) {
//                        // Normalize fractional seconds to 3 digits
//                        String datePart = parts[0];
//                        String millisPart = parts[1];
//
//                        // Handle potential 'Z' timezone marker
//                        boolean hasZ = millisPart.endsWith("Z");
//                        if (hasZ) {
//                            millisPart = millisPart.substring(0, millisPart.length() - 1);
//                        }
//
//                        // Trim or pad milliseconds to exactly 3 digits
//                        if (millisPart.length() > 3) {
//                            millisPart = millisPart.substring(0, 3);
//                        } else while (millisPart.length() < 3) {
//                            millisPart += "0";
//                        }
//
//                        // Reconstruct the date string
//                        String normalizedDateTime = datePart + "." + millisPart + (hasZ ? "Z" : "");
//                        logger.debug("Normalized date string: {}", normalizedDateTime);
//                        return LocalDateTime.parse(normalizedDateTime, DateTimeFormatter.ISO_DATE_TIME);
//                    }
//                }
//
//                // Third attempt: Try just the date part without time
//                if (dateTimeString.length() >= 19) {
//                    String simplifiedDateTime = dateTimeString.substring(0, 19); // Get just YYYY-MM-DDThh:mm:ss
//                    logger.debug("Trying simplified date: {}", simplifiedDateTime);
//                    return LocalDateTime.parse(simplifiedDateTime);
//                }
//
//                // Fourth attempt: For dates with timezone offset (not Z)
//                if (dateTimeString.contains("+") || (dateTimeString.contains("-") && dateTimeString.indexOf('-') > 8)) {
//                    // Extract the date time without the offset
//                    int offsetIndex = Math.max(dateTimeString.lastIndexOf('+'), dateTimeString.substring(8).lastIndexOf('-') + 8);
//                    if (offsetIndex > 0) {
//                        String dateWithoutOffset = dateTimeString.substring(0, offsetIndex);
//                        logger.debug("Trying without offset: {}", dateWithoutOffset);
//                        if (dateWithoutOffset.length() >= 19) {
//                            return LocalDateTime.parse(dateWithoutOffset.substring(0, 19));
//                        }
//                    }
//                }
//
//                // Fifth attempt: Try with specific format patterns
//                DateTimeFormatter[] formatters = {
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
//                };
//
//                for (DateTimeFormatter formatter : formatters) {
//                    try {
//                        logger.debug("Trying with formatter: {}", formatter);
//                        return LocalDateTime.parse(dateTimeString, formatter);
//                    } catch (Exception ignored) {
//                        // Try next formatter
//                    }
//                }
//
//                // If we get here, we couldn't parse the date
//                logger.warn("Unable to parse date after multiple attempts: {}", dateTimeString);
//                throw new IllegalArgumentException("Could not parse date: " + dateTimeString);
//
//            } catch (Exception e2) {
//                logger.warn("Date parsing failed: {}", e2.getMessage());
//                throw new IllegalArgumentException("Could not parse date: " + dateTimeString, e2);
//            }
//        }
//    }

//    private Customer getOrCreateCustomer(String username, String name, String email, String phone) {
//        Customer customer = null;
//
//        try {
//            // Try to find customer by username
//            if (username != null && !username.isEmpty()) {
//                customer = customerRepository.findByUsername(username).orElse(null);
//            }
//
//            // Try to find by email if not found by username
//            if (customer == null && email != null && !email.isEmpty()) {
//                customer = customerRepository.findByEmail(email).orElse(null);
//            }
//        } catch (Exception e) {
//            logger.warn("Error searching for customer in database: {}", e.getMessage());
//        }
//
//        // If no customer found or error occurred, create a new one
//        if (customer == null) {
//            try {
//                customer = new Customer();
//
//                // Generate a customerId
//                long customerId = System.currentTimeMillis();
//                customer.setCustomerId(customerId);
//
//                // Set the customer details from what we have
//                customer.setUsername(username != null ? username : "");
//                customer.setName(name != null ? name : "");
//                customer.setEmail(email != null ? email : "");
//                customer.setPhone(phone != null ? phone : "");
//
//                // Save immediately to ensure we have a valid customer with an ID
//                customer = customerRepository.save(customer);
//
//                logger.info("Created new customer with ID: {}, Name: {}, Email: {}",
//                        customer.getId(), customer.getName(), customer.getEmail());
//
//                return customer;
//            } catch (Exception e) {
//                logger.error("Failed to create new customer: {}", e.getMessage());
//                throw new IllegalStateException("Could not create customer record", e);
//            }
//        } else {
//            // Update existing customer with any new information
//            try {
//                boolean updated = false;
//
//                if (name != null && !name.isEmpty() && !name.equals(customer.getName())) {
//                    customer.setName(name);
//                    updated = true;
//                }
//
//                if (email != null && !email.isEmpty() && !email.equals(customer.getEmail())) {
//                    customer.setEmail(email);
//                    updated = true;
//                }
//
//                if (phone != null && !phone.isEmpty() && !phone.equals(customer.getPhone())) {
//                    customer.setPhone(phone);
//                    updated = true;
//                }
//
//                if (username != null && !username.isEmpty() && !username.equals(customer.getUsername())) {
//                    customer.setUsername(username);
//                    updated = true;
//                }
//
//                if (updated) {
//                    customer = customerRepository.save(customer);
//                    logger.info("Updated existing customer information for ID: {}", customer.getId());
//                }
//
//                return customer;
//            } catch (Exception e) {
//                logger.error("Failed to update existing customer: {}", e.getMessage());
//                throw new IllegalStateException("Could not update customer record", e);
//            }
//        }
//    }


//    // Helper method to search for customers in ShipStation
//    private JsonNode searchCustomerInShipStation(String searchTerm) throws IOException {
//        String response = shipstationService.searchCustomers(searchTerm);
//        return objectMapper.readTree(response);
//    }

    // Helper method to extract a customer ID from search results
//    private String extractCustomerIdFromSearchResults(JsonNode searchResults, String searchTerm) {
//        if (searchResults != null && searchResults.has("customers") && searchResults.get("customers").isArray()) {
//            JsonNode customers = searchResults.get("customers");
//            for (JsonNode customer : customers) {
//                // Check if this customer matches our search term
//                if ((customer.has("email") && customer.get("email").asText().equalsIgnoreCase(searchTerm)) ||
//                        (customer.has("username") && customer.get("username").asText().equalsIgnoreCase(searchTerm))) {
//                    return customer.path("customerId").asText();
//                }
//
//                // Check marketplace usernames
//                JsonNode marketplaceUsernames = customer.path("marketplaceUsernames");
//                if (marketplaceUsernames != null && marketplaceUsernames.isArray()) {
//                    for (JsonNode marketplaceUser : marketplaceUsernames) {
//                        if (marketplaceUser.has("username") &&
//                                marketplaceUser.get("username").asText().equalsIgnoreCase(searchTerm)) {
//                            return customer.path("customerId").asText();
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }


//    private boolean isDataRedacted(String data) {
//        if (data == null) return false;
//        boolean isRedacted = data.contains(REDACTED_TEXT) || data.equals("REDACTED (Amazon)");
//        if (isRedacted) {
//            logger.debug("Found redacted data: '{}'", data);
//        }
//        return isRedacted;
//    }
//
//
//    private void processOrderItems(Order order, JsonNode itemsJson) {
//        // Clear existing order items
//        if (order.getId() != null) {
//            try {
//                // Use JPA repository to delete directly
//                orderItemRepository.deleteByOrderId(order.getId());
//            } catch (Exception e) {
//                logger.warn("Could not delete existing order items: {}", e.getMessage());
//                // Don't throw the exception
//            }
//        }
//
//        if (itemsJson != null && itemsJson.isArray()) {
//            List<OrderItem> orderItems = new ArrayList<>();
//            for (JsonNode itemJson : itemsJson) {
//                OrderItem item = new OrderItem();
//                item.setOrder(order);
//                item.setSku(itemJson.path("sku").asText(""));
//                item.setName(itemJson.path("name").asText(""));
//
//                try {
//                    item.setQuantity(itemJson.path("quantity").asInt(0));
//                } catch (Exception e) {
//                    item.setQuantity(0);
//                    logger.warn("Invalid quantity format: {}", e.getMessage());
//                }
//
//                if (itemJson.has("unitPrice")) {
//                    try {
//                        item.setUnitPrice(new BigDecimal(itemJson.get("unitPrice").asText("0")));
//                    } catch (NumberFormatException e) {
//                        item.setUnitPrice(BigDecimal.ZERO);
//                        logger.warn("Invalid unit price format: {}", e.getMessage());
//                    }
//                }
//                orderItems.add(item);
//            }
//            order.setOrderItems(orderItems);
//        }
//    }
//
//
//    private void processInsuranceOption(Order order, JsonNode insuranceJson) {
//        // Clear existing insurance options
//        if (order.getId() != null) {
//            try {
//                // Use JPA repository to delete directly
//                insuranceOptionRepository.deleteByOrderId(order.getId());
//            } catch (Exception e) {
//                logger.warn("Could not delete existing insurance options: {}", e.getMessage());
//                // Don't throw the exception - this avoids marking transaction as rollback-only
//            }
//        }
//
//        if (insuranceJson != null && !insuranceJson.isEmpty()) {
//            InsuranceOption insurance = new InsuranceOption();
//            insurance.setOrder(order);
//            insurance.setProvider(insuranceJson.path("provider").asText(""));
//
//            if (insuranceJson.has("insureAmount")) {
//                try {
//                    insurance.setInsureAmount(new BigDecimal(insuranceJson.get("insureAmount").asText("0")));
//                } catch (NumberFormatException e) {
//                    insurance.setInsureAmount(BigDecimal.ZERO);
//                    logger.warn("Invalid insure amount format: {}", e.getMessage());
//                }
//            }
//
//            order.setInsuranceOption(insurance);
//        }
//    }
//
//    private void processInternationalOption(Order order, JsonNode internationalJson) {
//
//        if (internationalJson == null || internationalJson.isEmpty()) {
//            return;
//        }
//
//        try {
//            InternationalOption international = null;
//
//            if (order.getId() != null && order.getInternationalOption() != null) {
//                international = order.getInternationalOption();
//
//                international.setContents(internationalJson.path("contents").asText(""));
//                international.setNonDelivery(internationalJson.path("nonDelivery").asText(""));
//                logger.debug("Updated existing international option for order ID: {}", order.getId());
//                return;
//            }
//
//            if (order.getId() != null) {
//                List<InternationalOption> existingOptions = internationalOptionRepository.findByOrderId(order.getId());
//
//                if (!existingOptions.isEmpty()) {
//                    international = existingOptions.get(0);
//                    international.setContents(internationalJson.path("contents").asText(""));
//                    international.setNonDelivery(internationalJson.path("nonDelivery").asText(""));
//
//                    if (existingOptions.size() > 1) {
//                        for (int i = 1; i < existingOptions.size(); i++) {
//                            internationalOptionRepository.delete(existingOptions.get(i));
//                        }
//                        logger.debug("Cleaned up {} extra international options for order ID: {}",
//                                existingOptions.size() - 1, order.getId());
//                    }
//
//                    order.setInternationalOption(international);
//                    logger.debug("Reused existing international option for order ID: {}", order.getId());
//                    return;
//                }
//            }
//
//            international = new InternationalOption();
//            international.setOrder(order);
//            international.setContents(internationalJson.path("contents").asText(""));
//            international.setNonDelivery(internationalJson.path("nonDelivery").asText(""));
//            order.setInternationalOption(international);
//            logger.debug("Created new international option for order");
//
//        } catch (Exception e) {
//            logger.warn("Error processing international option: {}", e.getMessage());
//        }
//
//    }
//
//    private OrderInfoDTO mapToOrderInfoDTO(Order order) {
//
//        List<OrderItemInfoDTO> orderItemDTOs = order.getOrderItems().stream().map(OrderItemInfoDTO::new).toList();
//        InsuranceOptionInfoDTO insuranceDTO = order.getInsuranceOption() != null ? new InsuranceOptionInfoDTO(order.getInsuranceOption()) : null;
//        InternationalOptionInfoDTO internationalDTO = order.getInternationalOption() != null ? new InternationalOptionInfoDTO(order.getInternationalOption()) : null;
//
//        CustomerAddressDTO billTo = null;
//        if (order.getBillToCustomer() != null) {
//            billTo = new CustomerAddressDTO(
//                    order.getBillToCustomer().getName(),
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    order.getBillToCustomer().getPhone(),
//                    null,
//                    null
//            );
//        }
//
//        // Create shipTo address
//        CustomerAddressDTO shipTo = null;
//        if (order.getShipToCustomer() != null) {
//            shipTo = new CustomerAddressDTO(
//                    order.getShipToCustomer().getName(),
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    order.getShipToCustomer().getPhone(),
//                    null,
//                    null
//            );
//        }
//
//        return new OrderInfoDTO(
//                order.getId(),
//                order.getOrderNumber(),
//                order.getOrderKey(),
//                order.getOrderDate(),
//                order.getCreatedAt(),
//                order.getUpdatedAt(),
//                order.getPaymentDate(),
//                order.getShipByDate(),
//                order.getOrderStatus(),
//                order.getCustomer() != null ? order.getCustomer().getId() : null,
//                order.getCustomer() != null ? order.getCustomer().getUsername() : null,
//                order.getCustomer() != null ? order.getCustomer().getEmail() : null,
//                billTo, // New field for billTo
//                shipTo, // New field for shipTo
//                order.getOrderTotal(),
//                order.getAmountPaid(),
//                order.getTaxAmount(),
//                order.getShippingAmount(),
//                order.getPaymentMethod(),
//                order.getRequestedShippingService(),
//                order.getCarrierCode(),
//                order.getServiceCode(),
//                order.getPackageCode(),
//                order.getConfirmation(),
//                null,
//                orderItemDTOs,
//                insuranceDTO,
//                internationalDTO
//        );
//    }

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
