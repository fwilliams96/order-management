package com.fakecompany.order_management.mappers;

import com.fakecompany.order_management.api.dto.OrderBuyerDetailsDto;
import com.fakecompany.order_management.api.dto.OrderDto;
import com.fakecompany.order_management.api.dto.OrderSeatDto;
import com.fakecompany.order_management.api.dto.PaymentDetailsDto;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderBuyerDetails;
import com.fakecompany.order_management.orders.domain.OrderSeat;
import com.fakecompany.order_management.payments.domain.Payment;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoToOrderMapper {

    public OrderDto map(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setStatus(OrderDto.StatusEnum.fromValue(order.getStatus().getValue()));
        orderDto.setBuyerDetails(mapBuyerDetailsToDto(order.getBuyerDetails()));
        orderDto.setPaymentDetails(mapPaymentDetailsToDto(order.getPaymentDetails()));
        return orderDto;
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
