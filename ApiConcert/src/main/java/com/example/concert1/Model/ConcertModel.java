package com.example.concert1.Model;



import java.time.LocalDate;


import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



@Entity
@Table(name="concert", uniqueConstraints=@UniqueConstraint(columnNames="concertname"))
public class ConcertModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String concertName; 
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate concertDate;
    private Integer ticketPrice;
    private String venue;
    private int availableTickets;
    @Column(nullable = false)
    private String status = "Disable";

    public ConcertModel() {
        super();
    }

    public ConcertModel(String concertName, LocalDate concertDate, Integer ticketPrice, String venue, Integer availableTickets,String status) {
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.ticketPrice = ticketPrice;
        this.venue = venue;
        this.availableTickets = availableTickets;
        this.status=status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(Integer availableTickets) {
        this.availableTickets = availableTickets;
    }
    public String getStatus() {
    	 return status;
    }
    public void  setStatus(String status) {
    	 this.status=status;
    }
}
