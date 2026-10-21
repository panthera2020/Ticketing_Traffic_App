package com.lastma.controllers;

import com.lastma.dtos.requests.FindPaymentByIdRequest;
import com.lastma.dtos.requests.FindPaymentByReferenceRequest;
import com.lastma.dtos.requests.MakePaymentRequest;
import com.lastma.dtos.responses.ApiResponse;
import com.lastma.services.PaymentService;
import com.lastma.services.PaymentServiceImpl;

public class PaymentController {
    private PaymentService paymentService =  new PaymentServiceImpl();

    public ApiResponse makePayment(MakePaymentRequest request){
        try {
            return new ApiResponse(true, paymentService.makePayment(request));
        }
        catch (Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findPaymentById(FindPaymentByIdRequest idRequest){
        try {
            return new ApiResponse(true, paymentService.findPaymentBy(idRequest));
        }
        catch (Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findPaymentByReference(FindPaymentByReferenceRequest referenceRequest){
        try {
            return new ApiResponse(true, paymentService.findPaymentBy(referenceRequest));
        }
        catch (Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findAllPayments(){
        try {
            return new ApiResponse(true, paymentService.findAllPayments());
        } catch (Exception e) {
            return new ApiResponse(false, e.getMessage());
        }
    }
}
