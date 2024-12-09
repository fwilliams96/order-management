package com.fakecompany.order_management.mappers;

import com.fakecompany.order_management.api.dto.*;
import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderBuyerDetails;
import com.fakecompany.order_management.orders.domain.OrderSeat;
import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductImage;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class OrderToOrderDtoMapper {

    public OrderDto map(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setStatus(OrderDto.StatusEnum.fromValue(order.getStatus().getValue()));
        orderDto.setBuyerDetails(mapBuyerDetailsToDto(order.getBuyerDetails()));
        orderDto.setPaymentDetails(mapPaymentDetailsToDto(order.getPaymentDetails()));
        orderDto.setProducts(mapProductsToDto(order.getProducts()));
        orderDto.setTotalPrice(order.getTotalPrice() != null ? order.getTotalPrice().doubleValue() : null);
        return orderDto;
    }

    private List<ProductDto> mapProductsToDto(List<Product> products) {
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
        }
        return products.stream().map(this::mapProductToDto).toList();
    }

    private ProductDto mapProductToDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice() != null ? product.getPrice().doubleValue() : null,
                mapCategoryToDto(product.getCategory()),
                mapProductImageToDto(product.getImage())
        );
    }

    private CategoryDto mapCategoryToDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                category.getName()
        );
        if (category.getParent() != null) {
            categoryDto.parent(mapCategoryToDto(category.getParent()));
        }
        return categoryDto;
    }

    private ProductImageDto mapProductImageToDto(ProductImage productImage) {
        if (productImage == null) {
            return null;
        }
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setUrl(productImage.getUrl());
        return productImageDto;
    }

    private OrderBuyerDetailsDto mapBuyerDetailsToDto(OrderBuyerDetails buyerDetails) {
        if ( buyerDetails == null ) {
            return null;
        }
        OrderBuyerDetailsDto buyerDetailsDto = new OrderBuyerDetailsDto();
        buyerDetailsDto.setEmail(buyerDetails.getEmail());
        buyerDetailsDto.setSeat(mapSeatToDto(buyerDetails.getSeat()));
        return buyerDetailsDto;
    }

    private OrderSeatDto mapSeatToDto(OrderSeat seat) {
        if (seat == null) {
            return null;
        }
        OrderSeatDto orderSeatDto = new OrderSeatDto();
        orderSeatDto.setSeatLetter(String.valueOf(seat.getSeatLetter()));
        orderSeatDto.setSeatNumber(seat.getSeatNumber());
        return orderSeatDto;
    }

    private PaymentDetailsDto mapPaymentDetailsToDto(Payment paymentDetails) {
        if (paymentDetails == null) {
            return null;
        }
        PaymentDetailsDto paymentDetailsDto = new PaymentDetailsDto();
        paymentDetailsDto.setCardToken(paymentDetails.getCardToken());
        paymentDetailsDto.setStatus(PaymentDetailsDto.StatusEnum.fromValue(paymentDetails.getStatus().getValue()));
        paymentDetailsDto.setGateway(PaymentDetailsDto.GatewayEnum.fromValue(paymentDetails.getGateway().getValue()));
        paymentDetailsDto.setTotalPrice(paymentDetails.getTotalPrice() != null ? paymentDetails.getTotalPrice().doubleValue() : null);
        paymentDetailsDto.setPaymentDate(paymentDetails.getPaymentDate());
        return paymentDetailsDto;
    }

}
