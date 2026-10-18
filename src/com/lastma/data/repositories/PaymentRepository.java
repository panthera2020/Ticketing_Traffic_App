package com.lastma.data.repositories;

import com.lastma.data.models.Payment;

import java.util.List;

public interface PaymentRepository {
    Payment save(Payment payment);

    long count();

    Payment findById(int id);

    List<Payment> findAll();

    void delete(int paymentId);

    void deleteAll();

    boolean existById(int paymentId);

}