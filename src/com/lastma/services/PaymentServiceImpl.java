package com.lastma.services;

import com.lastma.data.models.Challenge;
import com.lastma.data.models.ChallengeStatus;
import com.lastma.data.models.Payment;
import com.lastma.data.models.Ticket;
import com.lastma.data.models.TicketStatus;
import com.lastma.data.repositories.ChallengeRepository;
import com.lastma.data.repositories.ChallengeRepositoryImpl;
import com.lastma.data.repositories.PaymentRepository;
import com.lastma.data.repositories.PaymentRepositoryImpl;
import com.lastma.data.repositories.TicketRepository;
import com.lastma.data.repositories.TicketRepositoryImpl;
import com.lastma.dtos.requests.FindPaymentByIdRequest;
import com.lastma.dtos.requests.FindPaymentByReferenceRequest;
import com.lastma.dtos.requests.MakePaymentRequest;
import com.lastma.dtos.responses.FindAllPaymentsResponse;
import com.lastma.dtos.responses.FindPaymentByIdResponse;
import com.lastma.dtos.responses.FindPaymentByReferenceResponse;
import com.lastma.dtos.responses.MakePaymentResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.lastma.utils.PaymentServiceMapper.*;

public class PaymentServiceImpl implements PaymentService {
    private PaymentRepository paymentRepository = new PaymentRepositoryImpl();
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private ChallengeRepository challengeRepository = new ChallengeRepositoryImpl();

    @Override
    public MakePaymentResponse makePayment(MakePaymentRequest request) {
        validateTicket(request.getTicketId());
        Ticket ticket = ticketRepository.findById(request.getTicketId());
        validateTicketIsUnpaid(ticket);
        validateNoPendingChallenge(ticket.getId());
        validateReference(request.getPaymentReference());
        validateAmount(request.getAmount(), ticket);
        Payment newPayment = map(request);
        Payment payment = paymentRepository.save(newPayment);
        ticket.setStatus(TicketStatus.PAID.toString());
        ticketRepository.save(ticket);

        return mapMade(payment);
    }

    @Override
    public FindPaymentByIdResponse findPaymentBy(FindPaymentByIdRequest idRequest) {
        validate(idRequest.getPaymentId());
        Payment foundPayment = paymentRepository.findById(idRequest.getPaymentId());

        return mapId(foundPayment);
    }

    @Override
    public FindPaymentByReferenceResponse findPaymentBy(FindPaymentByReferenceRequest request) {
        for (Payment foundPayment : findAllMadePayments()) {
            if (foundPayment.getPaymentReference().equals(request.getPaymentReference())) return mapReference(foundPayment);
        }
        throw new IllegalArgumentException("Payment with reference " + request.getPaymentReference() + " not found");
    }

    @Override
    public List<FindAllPaymentsResponse> findAllPayments() {
        List<Payment> foundPayments = findAllMadePayments();

        return mapListOf(foundPayments);
    }

    private List<Payment> findAllMadePayments() {
        List<Payment> madePayments = paymentRepository.findAll();
        if (madePayments == null) return new ArrayList<>();
        return madePayments;
    }

    private void validate(int paymentId) {
        if (paymentRepository.findById(paymentId) == null) throw new IllegalArgumentException("Payment with id " + paymentId + " not found");
    }

    private void validateTicket(int ticketId) {
        if (ticketRepository.findById(ticketId) == null) throw new IllegalArgumentException("Ticket with id " + ticketId + " not found");
    }

    private void validateTicketIsUnpaid(Ticket ticket) {
        if (!ticket.getStatus().equals(TicketStatus.UNPAID.toString())) {
            throw new IllegalArgumentException("Ticket with id " + ticket.getId() + " is " + ticket.getStatus() + " and cannot be paid");
        }
    }

    private void validateNoPendingChallenge(int ticketId) {
        List<Challenge> submittedChallenges = challengeRepository.findAll();
        if (submittedChallenges == null) return;
        for (Challenge challenge : submittedChallenges) {
            if (challenge.getTicketId() == ticketId && challenge.getStatus().equals(ChallengeStatus.PENDING.toString())) {
                throw new IllegalArgumentException("Ticket with id " + ticketId + " has a pending challenge and cannot be paid");
            }
        }
    }

    private void validateReference(String paymentReference) {
        if (paymentReference == null || paymentReference.isBlank()) throw new IllegalArgumentException("Payment reference is required");
        for (Payment payment : findAllMadePayments()) {
            if (payment.getPaymentReference().equals(paymentReference)) throw new IllegalArgumentException("Payment with reference " + paymentReference + " already exists");
        }
    }

    private void validateAmount(BigDecimal amount, Ticket ticket) {
        if (amount == null) throw new IllegalArgumentException("Amount is required");
        if (amount.compareTo(ticket.getViolationFee()) != 0) {
            throw new IllegalArgumentException("Amount must be exactly " + ticket.getViolationFee() + " for ticket with id " + ticket.getId());
        }
    }
}
