package com.lastma.data.repositories;

import com.lastma.data.models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {
    private static List<Ticket> tickets = new ArrayList<>();
    private int nextId = 0;

    @Override
    public Ticket save(Ticket ticket) {
        if (existById(ticket.getId())) {
            update(ticket);
            return ticket;
        }
        ticket.setId(++nextId);
        tickets.add(ticket);
        return ticket;
    }

    private void update(Ticket ticket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId() == ticket.getId()) {
                tickets.set(i, ticket);
                return;
            }
        }
    }

    @Override
    public long count() {
        return tickets.size();
    }

    @Override
    public Ticket findById(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        throw new IllegalArgumentException("Ticket with id " + id + " not found");
    }

    @Override
    public List<Ticket> findAll() {
        if (tickets.isEmpty()) throw new IllegalArgumentException("No Tickets saved");
        return new ArrayList<>(tickets);
    }

    @Override
    public void delete(int id) {
        Ticket foundTicket = findById(id);
        tickets.remove(foundTicket);
    }

    @Override
    public void deleteAll() {
        tickets.clear();
        nextId = 0;
    }

    @Override
    public boolean existById(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return true;
            }
        }
        return false;
    }
}