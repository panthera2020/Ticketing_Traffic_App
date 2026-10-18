package com.lastma.data.repositories;

import com.lastma.data.models.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketRepositoryImplTest {
    private TicketRepositoryImpl ticketRepository;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        ticketRepository = new TicketRepositoryImpl();
        ticket = new Ticket();
        ticketRepository.deleteAll();
    }

    @Test
    public void testThatWhenISaveOneTicket_CountIsOne() {
        ticketRepository.save(ticket);
        assertEquals(1L, ticketRepository.count());
    }

    @Test
    public void testThatWhenIAddTwoTickets_CountIsTwo() {
        assertEquals(0L, ticketRepository.count());
        ticketRepository.save(new Ticket());
        ticketRepository.save(ticket);
        assertEquals(2L, ticketRepository.count());
    }

    @Test
    public void testThatWhenISaveTicket_IdIsSet() {
        ticketRepository.save(ticket);
        assertEquals(1, ticket.getId());
    }

    @Test
    public void testThatWhenISaveTwoTickets_IdsAreIncremented() {
        Ticket ticketTwo = new Ticket();
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);

        assertEquals(1, ticket.getId());
        assertEquals(2, ticketTwo.getId());
    }

    @Test
    public void testThatWhenISaveTicket_SavedTicketIsReturned() {
        Ticket savedTicket = ticketRepository.save(ticket);
        assertSame(ticket, savedTicket);
    }

    @Test
    public void testThatWhenISaveTicket_AndISaveSameTicket_TicketIsUpdated() {
        Ticket newTicket = ticketRepository.save(ticket);
        Ticket sameTicket = ticketRepository.findById(newTicket.getId());
        sameTicket.setViolationFee(5000);
        ticketRepository.save(sameTicket);

        assertEquals(1, ticketRepository.count());
        assertEquals(5000, ticketRepository.findById(newTicket.getId()).getViolationFee());
    }

    @Test
    public void testThatWhenISaveSameTicketTwice_CountDoesNotIncrease() {
        ticketRepository.save(ticket);
        ticketRepository.save(ticket);

        assertEquals(1L, ticketRepository.count());
        assertEquals(1, ticket.getId());
    }

    @Test
    public void testThatWhenISaveTicketWithExistingId_ThatTicketIsReplaced() {
        Ticket savedTicket = ticketRepository.save(ticket);

        Ticket replacement = new Ticket();
        replacement.setId(savedTicket.getId());
        replacement.setViolationFee(5000);
        ticketRepository.save(replacement);

        assertEquals(1L, ticketRepository.count());
        assertSame(replacement, ticketRepository.findById(savedTicket.getId()));
        assertEquals(5000, ticketRepository.findById(savedTicket.getId()).getViolationFee());
    }

    @Test
    public void testThatWhenIUpdateSecondTicket_FirstTicketIsNotAffected() {
        Ticket ticketTwo = new Ticket();
        ticket.setViolationFee(2000);
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);

        Ticket replacement = new Ticket();
        replacement.setId(ticketTwo.getId());
        replacement.setViolationFee(7000);
        ticketRepository.save(replacement);

        assertEquals(2L, ticketRepository.count());
        assertEquals(2000, ticketRepository.findById(ticket.getId()).getViolationFee());
        assertEquals(7000, ticketRepository.findById(ticketTwo.getId()).getViolationFee());
    }

    @Test
    public void testThatWhenISaveAfterDeletingATicket_NewIdIsUnique() {
        Ticket ticketTwo = new Ticket();
        Ticket ticketThree = new Ticket();
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);
        ticketRepository.delete(ticket.getId());
        ticketRepository.save(ticketThree);

        assertNotEquals(ticketTwo.getId(), ticketThree.getId());
        assertEquals(2L, ticketRepository.count());
    }

    @Test
    public void testThatWhenIFindById_TicketIsFound() {
        Ticket savedTicket = ticketRepository.save(ticket);
        assertEquals(savedTicket, ticketRepository.findById(savedTicket.getId()));
    }

    @Test
    public void testThatWhenIFindByIncorrectId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> ticketRepository.findById(-1));
    }

    @Test
    public void testThatWhenIFindById_CorrectTicketIsReturnedFromMany() {
        Ticket ticketTwo = new Ticket();
        Ticket ticketThree = new Ticket();
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);
        ticketRepository.save(ticketThree);

        assertSame(ticketTwo, ticketRepository.findById(ticketTwo.getId()));
        assertSame(ticketThree, ticketRepository.findById(ticketThree.getId()));
    }

    @Test
    public void testThatWhenIFindByIdOnEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> ticketRepository.findById(1));
    }

    @Test
    public void testThatWhenIFindDeletedTicket_ErrorIsThrown() {
        Ticket savedTicket = ticketRepository.save(ticket);
        int ticketId = savedTicket.getId();
        ticketRepository.delete(ticketId);

        assertThrows(IllegalArgumentException.class, () -> ticketRepository.findById(ticketId));
    }

    @Test
    public void testThatWhenIFindAll_AllTicketsAreFound() {
        Ticket ticketTwo = new Ticket();
        Ticket ticketThree = new Ticket();
        Ticket[] allTickets = {ticket, ticketTwo, ticketThree};
        for (Ticket eachTicket : allTickets) {
            ticketRepository.save(eachTicket);
        }
        List<Ticket> foundTickets = ticketRepository.findAll();
        assertEquals(3, foundTickets.size());
        for (int index = 0; index < allTickets.length; index++) {
            assertSame(allTickets[index], foundTickets.get(index));
        }
    }

    @Test
    public void testThatWhenIFindAll_AndNoSavedTickets_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> ticketRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteOneTicket_FindAllDoesNotContainIt() {
        Ticket ticketTwo = new Ticket();
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);
        ticketRepository.delete(ticket.getId());

        List<Ticket> foundTickets = ticketRepository.findAll();
        assertEquals(1, foundTickets.size());
        assertFalse(foundTickets.contains(ticket));
        assertTrue(foundTickets.contains(ticketTwo));
    }

    @Test
    public void testThatWhenIDeleteTheOnlyTicket_FindAllThrowsError() {
        Ticket savedTicket = ticketRepository.save(ticket);
        ticketRepository.delete(savedTicket.getId());

        assertThrows(IllegalArgumentException.class, () -> ticketRepository.findAll());
    }

    @Test
    public void testThatWhenIModifyListFromFindAll_RepositoryIsNotAffected() {
        ticketRepository.save(ticket);
        ticketRepository.findAll().clear();

        assertEquals(1L, ticketRepository.count());
        assertEquals(1, ticketRepository.findAll().size());
    }

    @Test
    public void testThatWhenIDeleteTicket_TicketIsDeleted() {
        Ticket savedTicket = ticketRepository.save(ticket);
        assertEquals(1L, ticketRepository.count());
        ticketRepository.delete(savedTicket.getId());
        assertEquals(0L, ticketRepository.count());
    }

    @Test
    public void testThatWhenIDeleteTicketWithInvalidId_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> ticketRepository.delete(-1));
    }

    @Test
    public void testThatWhenIDeleteOneTicket_OtherTicketsRemain() {
        Ticket ticketTwo = new Ticket();
        Ticket ticketThree = new Ticket();
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);
        ticketRepository.save(ticketThree);

        ticketRepository.delete(ticketTwo.getId());

        assertEquals(2L, ticketRepository.count());
        assertTrue(ticketRepository.existById(ticket.getId()));
        assertFalse(ticketRepository.existById(ticketTwo.getId()));
        assertTrue(ticketRepository.existById(ticketThree.getId()));
    }

    @Test
    public void testThatWhenIDeleteSameTicketTwice_ErrorIsThrown() {
        Ticket savedTicket = ticketRepository.save(ticket);
        int ticketId = savedTicket.getId();
        ticketRepository.delete(ticketId);

        assertThrows(IllegalArgumentException.class, () -> ticketRepository.delete(ticketId));
        assertEquals(0L, ticketRepository.count());
    }

    @Test
    public void testThatWhenIDeleteFromEmptyRepository_ErrorIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> ticketRepository.delete(1));
    }

    @Test
    public void testThatWhenIDeleteTicket_CountDecreasesByOne() {
        Ticket ticketTwo = new Ticket();
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);
        ticketRepository.delete(ticket.getId());

        assertEquals(1L, ticketRepository.count());
    }

    @Test
    public void testThatWhenIDeleteAllTickets_AllTicketsAreDeleted() {
        ticketRepository.save(ticket);
        ticketRepository.save(new Ticket());
        ticketRepository.save(new Ticket());
        ticketRepository.deleteAll();

        assertEquals(0L, ticketRepository.count());
        assertThrows(IllegalArgumentException.class, () -> ticketRepository.findAll());
    }

    @Test
    public void testThatWhenIDeleteAllOnEmptyRepository_NoErrorIsThrown() {
        assertDoesNotThrow(() -> ticketRepository.deleteAll());
        assertEquals(0L, ticketRepository.count());
    }

    @Test
    public void testThatWhenISaveAfterDeleteAll_IdStartsFromOneAgain() {
        ticketRepository.save(ticket);
        ticketRepository.save(new Ticket());
        ticketRepository.deleteAll();

        Ticket newTicket = new Ticket();
        ticketRepository.save(newTicket);

        assertEquals(1, newTicket.getId());
        assertEquals(1L, ticketRepository.count());
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueIfTicketExists() {
        Ticket savedTicket = ticketRepository.save(ticket);
        assertTrue(ticketRepository.existById(savedTicket.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsFalseIfTicketDoesNotExist() {
        assertFalse(ticketRepository.existById(-10));
    }

    @Test
    public void testThatWhenICheckExistById_ReturnsTrueForEachSavedTicket() {
        Ticket ticketTwo = new Ticket();
        Ticket ticketThree = new Ticket();
        ticketRepository.save(ticket);
        ticketRepository.save(ticketTwo);
        ticketRepository.save(ticketThree);

        assertTrue(ticketRepository.existById(ticket.getId()));
        assertTrue(ticketRepository.existById(ticketTwo.getId()));
        assertTrue(ticketRepository.existById(ticketThree.getId()));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDelete_ReturnsFalse() {
        Ticket savedTicket = ticketRepository.save(ticket);
        int ticketId = savedTicket.getId();
        ticketRepository.delete(ticketId);

        assertFalse(ticketRepository.existById(ticketId));
    }

    @Test
    public void testThatWhenICheckExistById_AfterDeleteAll_ReturnsFalse() {
        Ticket savedTicket = ticketRepository.save(ticket);
        int ticketId = savedTicket.getId();
        ticketRepository.deleteAll();

        assertFalse(ticketRepository.existById(ticketId));
    }
}