package com.fakecompany.order_management.orders.infrastructure.persistence.mongodb;

import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderBuyerDetails;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.orders.domain.OrderSeat;
import com.fakecompany.order_management.orders.infrastructure.persistence.mongodb.model.OrderEntity;
import com.fakecompany.order_management.orders.infrastructure.persistence.mongodb.repository.SpringDataMongoDbOrderRepository;
import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.payments.domain.PaymentRepository;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MongoDbOrderRepository implements OrderRepository {

    private final SpringDataMongoDbOrderRepository springDataMongoDbOrderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Order create(Order order) {
        OrderEntity insert = springDataMongoDbOrderRepository.insert(mapOrderToOrderEntity(order));
        return mapOrderEntityToOrder(insert);
    }

    @Override
    public Order update(Order order) {
        OrderEntity insert = springDataMongoDbOrderRepository.save(mapOrderToOrderEntity(order));
        return mapOrderEntityToOrder(insert);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        Optional<OrderEntity> byId = springDataMongoDbOrderRepository.findById(id);
        return byId.map(this::mapOrderEntityToOrder);
    }

    private OrderEntity mapOrderToOrderEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setStatus(order.getStatus().getValue());
        orderEntity.setPaymentId(order.getPaymentDetails() != null ? order.getPaymentDetails().getId() : null);
        orderEntity.setProductIds(getProductIds(order.getProducts()));
        orderEntity.setBuyerDetails(mapBuyerDetailsToBuyerDetailsEntity(order.getBuyerDetails()));
        orderEntity.setUserId(order.getUserId());
        orderEntity.setTotalPrice(order.getTotalPrice());
        return orderEntity;
    }

    private OrderEntity.OrderBuyerDetailsEntity mapBuyerDetailsToBuyerDetailsEntity(OrderBuyerDetails buyerDetails) {
        if (buyerDetails == null) {
            return null;
        }
        OrderEntity.OrderBuyerDetailsEntity buyerDetailsEntity = new OrderEntity.OrderBuyerDetailsEntity();
        buyerDetailsEntity.setEmail(buyerDetails.getEmail());
        buyerDetailsEntity.setSeat(mapBuyerDetailsSeatToBuyerDetailsSeatEntity(buyerDetails.getSeat()));
        return buyerDetailsEntity;
    }

    private OrderEntity.OrderSeatEntity mapBuyerDetailsSeatToBuyerDetailsSeatEntity(OrderSeat seat) {
        if (seat == null) {
            return null;
        }
        OrderEntity.OrderSeatEntity orderSeatEntity = new OrderEntity.OrderSeatEntity();
        orderSeatEntity.setSeatLetter(seat.getSeatLetter());
        orderSeatEntity.setSeatNumber(seat.getSeatNumber());
        return orderSeatEntity;
    }

    private List<UUID> getProductIds(List<Product> products) {
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
        }
        return products.stream().map(Product::getId).collect(Collectors.toList());
    }

    private Order mapOrderEntityToOrder(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }
        return Order.builder()
                .id(orderEntity.getId())
                .userId(orderEntity.getUserId())
                .status(Order.OrderStatus.from(orderEntity.getStatus()))
                .products(findProducts(orderEntity.getProductIds()))
                .paymentDetails(getPaymentDetails(orderEntity.getPaymentId()))
                .buyerDetails(mapBuyerDetailsEntityToBuyerDetails(orderEntity.getBuyerDetails()))
                .totalPrice(orderEntity.getTotalPrice())
                .build();
    }

    private OrderBuyerDetails mapBuyerDetailsEntityToBuyerDetails(OrderEntity.OrderBuyerDetailsEntity buyerDetailsEntity) {
        if (buyerDetailsEntity == null) {
            return null;
        }
        return OrderBuyerDetails.builder()
                .email(buyerDetailsEntity.getEmail())
                .seat(mapSeatEntityToSeat(buyerDetailsEntity.getSeat()))
                .build();
    }

    private OrderSeat mapSeatEntityToSeat(OrderEntity.OrderSeatEntity seat) {
        if (seat == null) {
            return null;
        }
        return OrderSeat.builder()
                .seatLetter(seat.getSeatLetter())
                .seatNumber(seat.getSeatNumber())
                .build();
    }

    private List<Product> findProducts(List<UUID> productsIds) {
        if (CollectionUtils.isEmpty(productsIds)) {
            return Collections.emptyList();
        }
        return productsIds.stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Payment getPaymentDetails(UUID paymentId) {
        if (paymentId == null) {
            return null;
        }
        return paymentRepository.findById(paymentId).orElse(null);
    }
}
