package com.fakecompany.order_management.orders.application.create;

import com.fakecompany.order_management.orders.domain.NewOrder;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderBuyerDetails;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.products.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreator {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order create(NewOrder order) {
        List<Product> orderProducts;
        if (CollectionUtils.isEmpty(order.getProductIds())) {
            orderProducts = Collections.emptyList();
        }
        else {
            orderProducts = order.getProductIds().stream()
                    .map(id -> productRepository.findById(id)
                            .orElseThrow(() -> new ProductNotFoundError(id)))
                    .collect(Collectors.toList());

            // Check stock
            Map<Product, Integer> productDemandMap = orderProducts.stream()
                    .collect(Collectors.groupingBy(
                            product -> product,
                            Collectors.summingInt(product -> 1)
                    ));

            Map<Product, Integer> insufficientStockMap = productDemandMap.entrySet().stream()
                    .filter(entry -> {
                        Product product = entry.getKey();
                        int totalDemanded = entry.getValue();
                        int availableStock = product.getStock();
                        return totalDemanded > availableStock;
                    })
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));

            if (!insufficientStockMap.isEmpty()) {
                log.debug("Products with insufficient stock: {}", insufficientStockMap);
                ProductStockNotEnoughError notEnoughError = new ProductStockNotEnoughError();
                insufficientStockMap.forEach((key, value) -> notEnoughError.addProduct(
                        key.getId(),
                        key.getStock(),
                        value
                ));
                throw notEnoughError;
            }

        }
        return orderRepository.create(
          Order.builder()
                  .id(UUID.randomUUID())
                  .status(Order.OrderStatus.OPEN)
                  .paymentDetails(null)
                  .buyerDetails(
                          OrderBuyerDetails.builder()
                                  .seat(order.getSeat())
                                  .build()
                  )
                  .products(orderProducts)
                  .totalPrice(
                          BigDecimal.valueOf(
                                  orderProducts.stream()
                                          .map(Product::getPrice)
                                          .mapToDouble(BigDecimal::doubleValue)
                                          .sum()
                          )
                  )
                  .userId(order.getUserId())
                  .build()
        );
    }

}
