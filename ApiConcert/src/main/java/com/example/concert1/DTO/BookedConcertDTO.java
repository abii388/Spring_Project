package com.example.concert1.DTO;

import java.time.LocalDate;

public class BookedConcertDTO {
    private String concertName;
    private LocalDate concertDate;
    private Integer ticketPrice;
    private String venue;

    // Constructors
    public BookedConcertDTO(String concertName, LocalDate concertDate, Integer ticketPrice, String venue) {
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.ticketPrice = ticketPrice;
        this.venue = venue;
    }

    // Getters & Setters
    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public LocalDate getConcertDate() {
        return concertDate;
    }

    public void setConcertDate(LocalDate concertDate) {
        this.concertDate = concertDate;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}

