package com.lastma.utils;

import com.lastma.data.models.Payment;
import com.lastma.dtos.requests.MakePaymentRequest;
import com.lastma.dtos.responses.FindAllPaymentsResponse;
import com.lastma.dtos.responses.FindPaymentByIdResponse;
import com.lastma.dtos.responses.FindPaymentByReferenceResponse;
import com.lastma.dtos.responses.MakePaymentResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentServiceMapper {
    public static Payment map(MakePaymentRequest request) {
        Payment payment = new Payment();
        payment.setTicketId(request.getTicketId());
        payment.setPaymentReference(request.getPaymentReference());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(LocalDate.now());
        return payment;
    }

    public static MakePaymentResponse mapMade(Payment payment) {
        MakePaymentResponse response = new MakePaymentResponse();
        response.setId(payment.getId());
        response.setTicketId(payment.getTicketId());
        response.setPaymentReference(payment.getPaymentReference());
        response.setAmount(payment.getAmount());
        response.setPaymentDate(payment.getPaymentDate());
        return response;
    }

    public static FindPaymentByIdResponse mapId(Payment foundPayment) {
        FindPaymentByIdResponse response = new FindPaymentByIdResponse();
        response.setId(foundPayment.getId());
        response.setTicketId(foundPayment.getTicketId());
        response.setPaymentReference(foundPayment.getPaymentReference());
        response.setAmount(foundPayment.getAmount());
        response.setPaymentDate(foundPayment.getPaymentDate());
        return response;
    }

    public static FindPaymentByReferenceResponse mapReference(Payment foundPayment) {
        FindPaymentByReferenceResponse response = new FindPaymentByReferenceResponse();
        response.setId(foundPayment.getId());
        response.setTicketId(foundPayment.getTicketId());
        response.setPaymentReference(foundPayment.getPaymentReference());
        response.setAmount(foundPayment.getAmount());
        response.setPaymentDate(foundPayment.getPaymentDate());
        return response;
    }

    public static List<FindAllPaymentsResponse> mapListOf(List<Payment> foundPayments) {
        List<FindAllPaymentsResponse> responseList = new ArrayList<>();
        for (Payment payment : foundPayments) {
            FindAllPaymentsResponse response = new FindAllPaymentsResponse();
            response.setId(payment.getId());
            response.setTicketId(payment.getTicketId());
            response.setPaymentReference(payment.getPaymentReference());
            response.setAmount(payment.getAmount());
            response.setPaymentDate(payment.getPaymentDate());
            responseList.add(response);
        }
        return responseList;
    }
}
