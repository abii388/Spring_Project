package com.example.concert.DTO;



public class TicketInfo {
    private int ticketPrice;
    private int availableTickets;

    public TicketInfo() {}

    public TicketInfo(int ticketPrice, int availableTickets) {
        this.ticketPrice = ticketPrice;
        this.availableTickets = availableTickets;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }
}
