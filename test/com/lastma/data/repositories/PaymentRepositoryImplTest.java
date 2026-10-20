package com.lastma.data.repositories;

import com.lastma.data.models.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryImplTest {
    private PaymentRepositoryImpl paymentRepository;
    private Payment payment;

    @BeforeEach
    public void setUp() {
        paymentRepository = new PaymentRepositoryImpl();
        payment = new Payment();
        paymentRepository.deleteAll();
    }

    @Test
    public void testThatWhenISaveOnePayment_CountIsOne() {
        paymentRepository.save(payment);
        assertEquals(1L, paymentRepository.count());
    }

    @Test
    public void testThatWhenISavePayment_AndISaveSamePayment_PaymentIsUpdated() {
        Payment newPayment = paymentRepository.save(payment);
        Payment samePayment = paymentRepository.findById(newPayment.getId());
        samePayment.setPaymentReference("PAY-001");
        paymentRepository.save(samePayment);

        assertEquals(1, paymentRepository.count());
        assertEquals("PAY-001", paymentRepository.findById(newPayment.getId()).getPaymentReference());
    }

    @Test
    public void testThatWhenIAddTwoPayments_CountIsTwo() {
        assertEquals(0L, paymentRepository.count());
        Payment paymentTwo = new Payment();
        paymentRepository.save(paymentTwo);
        paymentRepository.save(payment);
        assertEquals(2L, paymentRepository.count());
    }

    @Test
    public void testThatWhenISavePayment_IdIsSet() {
        paymentRepository.save(payment);
        assertEquals(1, payment.getId());
    }

    @Test
    public void testThatWhenIFindById_PaymentIsFound() {
        Payment savedPayment = paymentRepository.save(payment);
        int paymentId = savedPayment.getId();
        assertEquals(savedPayment, paymentRepository.findById(paymentId));
    }

    @Test
    public void testThatWhenIFindByIncorrectId_returnsNull() {
        assertNull(paymentRepository.findById(-1));
    }

    @Test
    public void testThatWhenIFindAll_AllPaymentsAreFound() {
        Payment paymentTwo = new Payment();
        Payment paymentThree = new Payment();
        Payment[] allPayments = {payment, paymentTwo, paymentThree};
        for (Payment item : allPayments) {
            paymentRepository.save(item);
        }
        List<Payment> foundPayments = paymentRepository.findAll();
        int count = 0;
        for (Payment item : foundPayments) {
            assertEquals(item, allPayments[count]);
            count++;
        }
    }

    @Test
    public void testThatWhenIFindAll_AndNoSavedPayments_ErrorIsThrown() {
        assertNull(paymentRepository.findAll());
    }

    @Test
    public void testThatWhenIDeletePayment_PaymentIsDeleted() {
        Payment savedPayment = paymentRepository.save(payment);
        int paymentId = savedPayment.getId();
        assertEquals(1L, paymentRepository.count());
        paymentRepository.delete(paymentId);
        assertEquals(0L, paymentRepository.count());
    }

    @Test
    public void testThatWhenIDeleteAllPayments_AllPaymentsAreDeleted() {
        Payment paymentTwo = new Payment();
        Payment paymentThree = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);
        paymentRepository.save(paymentThree);
        paymentRepository.deleteAll();
        assertEquals(0L, paymentRepository.count());
        assertNull(paymentRepository.findAll());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueIfPaymentExists() {
        Payment savedPayment = paymentRepository.save(payment);
        int paymentId = savedPayment.getId();
        assertTrue(paymentRepository.existById(paymentId));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsFalseIfPaymentDoesNotExist() {
        assertFalse(paymentRepository.existById(-10));
    }

    @Test
    public void testThatWhenISaveTwoPayments_IdsAreIncremented() {
        Payment paymentTwo = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);

        assertEquals(1, payment.getId());
        assertEquals(2, paymentTwo.getId());
    }

    @Test
    public void testThatWhenISavePayment_SavedPaymentIsReturned() {
        Payment savedPayment = paymentRepository.save(payment);
        assertSame(payment, savedPayment);
    }

    @Test
    public void testThatWhenISaveSamePaymentTwice_CountDoesNotIncrease() {
        paymentRepository.save(payment);
        paymentRepository.save(payment);

        assertEquals(1L, paymentRepository.count());
        assertEquals(1, payment.getId());
    }

    @Test
    public void testThatWhenISavePaymentWithExistingId_ThatPaymentIsReplaced() {
        Payment savedPayment = paymentRepository.save(payment);

        Payment replacement = new Payment();
        replacement.setId(savedPayment.getId());
        replacement.setPaymentReference("PAY-001");
        paymentRepository.save(replacement);

        assertEquals(1L, paymentRepository.count());
        assertSame(replacement, paymentRepository.findById(savedPayment.getId()));
        assertEquals("PAY-001", paymentRepository.findById(savedPayment.getId()).getPaymentReference());
    }

    @Test
    public void testThatWhenIUpdateSecondPayment_FirstPaymentIsNotAffected() {
        Payment paymentTwo = new Payment();
        payment.setPaymentReference("PAY-002");
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);

        Payment replacement = new Payment();
        replacement.setId(paymentTwo.getId());
        replacement.setPaymentReference("PAY-003");
        paymentRepository.save(replacement);

        assertEquals(2L, paymentRepository.count());
        assertEquals("PAY-002", paymentRepository.findById(payment.getId()).getPaymentReference());
        assertEquals("PAY-003", paymentRepository.findById(paymentTwo.getId()).getPaymentReference());
    }

    @Test
    public void testThatWhenISaveAfterDeletingAPayment_NewIdIsUnique() {
        Payment paymentTwo = new Payment();
        Payment paymentThree = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);
        paymentRepository.delete(payment.getId());
        paymentRepository.save(paymentThree);

        assertNotEquals(paymentTwo.getId(), paymentThree.getId());
        assertEquals(2L, paymentRepository.count());
    }

    @Test
    public void testThatWhenIFindById_CorrectPaymentIsReturnedFromMany() {
        Payment paymentTwo = new Payment();
        Payment paymentThree = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);
        paymentRepository.save(paymentThree);

        assertSame(paymentTwo, paymentRepository.findById(paymentTwo.getId()));
        assertSame(paymentThree, paymentRepository.findById(paymentThree.getId()));
    }

    @Test
    public void testThatWhenIFindByIdOnEmptyRepository_returnsNull() {
        assertNull(paymentRepository.findById(1));
    }

    @Test
    public void testThatWhenIFindDeletedPayment_ErrorIsThrown() {
        Payment savedPayment = paymentRepository.save(payment);
        int paymentId = savedPayment.getId();
        paymentRepository.delete(paymentId);

        assertNull(paymentRepository.findById(paymentId));
    }

    @Test
    public void testThatWhenIFindAll_CorrectNumberOfPaymentsIsReturned() {
        paymentRepository.save(payment);
        paymentRepository.save(new Payment());
        paymentRepository.save(new Payment());

        assertEquals(3, paymentRepository.findAll().size());
    }

    @Test
    public void testThatWhenIDeleteOnePayment_FindAllDoesNotContainIt() {
        Payment paymentTwo = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);
        paymentRepository.delete(payment.getId());

        List<Payment> foundPayments = paymentRepository.findAll();
        assertEquals(1, foundPayments.size());
        assertFalse(foundPayments.contains(payment));
        assertTrue(foundPayments.contains(paymentTwo));
    }

    @Test
    public void testThatWhenIDeleteTheOnlyPayment_FindAllThrowsError() {
        Payment savedPayment = paymentRepository.save(payment);
        paymentRepository.delete(savedPayment.getId());

        assertNull(paymentRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOnePayment_OtherPaymentsRemain() {
        Payment paymentTwo = new Payment();
        Payment paymentThree = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);
        paymentRepository.save(paymentThree);

        paymentRepository.delete(paymentTwo.getId());

        assertEquals(2L, paymentRepository.count());
        assertTrue(paymentRepository.existById(payment.getId()));
        assertFalse(paymentRepository.existById(paymentTwo.getId()));
        assertTrue(paymentRepository.existById(paymentThree.getId()));
    }

    @Test
    public void testThatWhenIDeleteAllOnEmptyRepository_NoErrorIsThrown() {
        assertDoesNotThrow(() -> paymentRepository.deleteAll());
        assertEquals(0L, paymentRepository.count());
    }

    @Test
    public void testThatWhenISaveAfterDeleteAll_IdStartsFromOneAgain() {
        paymentRepository.save(payment);
        paymentRepository.save(new Payment());
        paymentRepository.deleteAll();

        Payment newPayment = new Payment();
        paymentRepository.save(newPayment);

        assertEquals(1, newPayment.getId());
        assertEquals(1L, paymentRepository.count());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueForEachSavedPayment() {
        Payment paymentTwo = new Payment();
        Payment paymentThree = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);
        paymentRepository.save(paymentThree);

        assertTrue(paymentRepository.existById(payment.getId()));
        assertTrue(paymentRepository.existById(paymentTwo.getId()));
        assertTrue(paymentRepository.existById(paymentThree.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDelete_ReturnsFalse() {
        Payment savedPayment = paymentRepository.save(payment);
        int paymentId = savedPayment.getId();
        paymentRepository.delete(paymentId);

        assertFalse(paymentRepository.existById(paymentId));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDeleteAll_ReturnsFalse() {
        Payment savedPayment = paymentRepository.save(payment);
        int paymentId = savedPayment.getId();
        paymentRepository.deleteAll();

        assertFalse(paymentRepository.existById(paymentId));
    }

    @Test
    public void testThatWhenIDeletePayment_CountDecreasesByOne() {
        Payment paymentTwo = new Payment();
        paymentRepository.save(payment);
        paymentRepository.save(paymentTwo);
        paymentRepository.delete(payment.getId());

        assertEquals(1L, paymentRepository.count());
    }
}