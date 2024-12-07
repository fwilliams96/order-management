package com.fakecompany.order_management.orders.application.create;

import com.fakecompany.order_management.orders.domain.NewOrder;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderBuyerDetails;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductNotFoundError;
import com.fakecompany.order_management.products.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCreator {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order create(NewOrder order) {
        List<Product> orderProducts;
        if (CollectionUtils.isEmpty(order.getProductsIds())) {
            orderProducts = Collections.emptyList();
        }
        else {
            orderProducts = order.getProductsIds().stream()
                    .map(id -> productRepository.findById(id)
                            .orElseThrow(() -> new ProductNotFoundError(id)))
                    .toList();
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
                  .build()
        );
    }

}
