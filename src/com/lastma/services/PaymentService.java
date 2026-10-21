package com.lastma.services;

import com.lastma.dtos.requests.FindPaymentByIdRequest;
import com.lastma.dtos.requests.FindPaymentByReferenceRequest;
import com.lastma.dtos.requests.MakePaymentRequest;
import com.lastma.dtos.responses.FindAllPaymentsResponse;
import com.lastma.dtos.responses.FindPaymentByIdResponse;
import com.lastma.dtos.responses.FindPaymentByReferenceResponse;
import com.lastma.dtos.responses.MakePaymentResponse;

import java.util.List;

public interface PaymentService {
    MakePaymentResponse makePayment(MakePaymentRequest request);

    FindPaymentByIdResponse findPaymentBy(FindPaymentByIdRequest request);

    FindPaymentByReferenceResponse findPaymentBy(FindPaymentByReferenceRequest request);

    List<FindAllPaymentsResponse> findAllPayments();
}
