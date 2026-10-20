package com.lastma.data.repositories;

import com.lastma.data.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {
    private static List<Payment> payments = new ArrayList<>();
    private int numberOfPayments = 0;

    @Override
    public Payment save(Payment payment) {
        if (existById(payment.getId())) {
            update(payment);
            return payment;
        }
        payment.setId(++numberOfPayments);
        payments.add(payment);
        return payment;
    }

    @Override
    public long count() {
        return payments.size();
    }

    @Override
    public Payment findById(int id) {
        for (Payment payment : payments) {
            if (payment.getId() == id) {
                return payment;
            }
        }
        return null;
    }

    private void update(Payment payment) {
        for (int index = 0; index < payments.size(); index++) {
            if (payments.get(index).getId() == payment.getId()) {
                payments.set(index, payment);
                break;
            }
        }
    }

    @Override
    public List<Payment> findAll() {
        if (count() == 0) return null;
        return payments;
    }

    @Override
    public void delete(int id) {
        Payment foundPayment = findById(id);
        payments.remove(foundPayment);
    }

    @Override
    public void deleteAll() {
        payments.clear();
        numberOfPayments = 0;
    }

    @Override
    public boolean existById(int id) {
        for (Payment payment : payments) {
            if (payment.getId() == id) {
                return true;
            }
        }
        return false;
    }
}