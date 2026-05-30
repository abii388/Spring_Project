package com.example.concert.Model;

import java.time.LocalDate;



import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table (name="bookedconcert")
public class bookedConcert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String concertName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate concertDate;
    private Integer ticketPrice;
    private String venue;
   
    private Integer totalPrice;
    private Integer numberofPerson;
    private String userName;
    private Integer userId;

    public bookedConcert() {
        super();
    }

    public bookedConcert(String concertName, LocalDate concertDate, Integer ticketPrice, String venue,Integer  totalPrice,
    		Integer numberofPerson, String userName ,Integer userId ) {
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.ticketPrice = ticketPrice;
        this.venue = venue;
        
        this.totalPrice=totalPrice;
        this.numberofPerson=numberofPerson;
        this.userName=userName;
        this.userId=userId;
    }
     
    //this is used for connect the bookedconcert table and cocnerttable;
    @ManyToOne
    @JoinColumn(name = "concert_id")
    private ConcertModel concert; // Reference to ConcertModel

    // Getters and setters for concert and other fields
    public ConcertModel getConcert() {
        return concert;
    }

    public void setConcert(ConcertModel concert) {
        this.concert = concert;
    }
//    
    
    
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

    public void setTicketPrice(Integer  ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

   
    public int getTotalPrice() {
    	return totalPrice;
    }
    
    public void setTotalPrice(Integer  totalPrice) {
    	
    	this.totalPrice=totalPrice;
    }
    public  Integer getNumberofPerson() {
    	return numberofPerson;
    }
    public void setNumberofPerson(Integer  numberofPerson) {
    	this.numberofPerson=numberofPerson;
    }
	public String getUserName() {
		return userName;
	}
	
    public void setUserName(String userName) {
    	this.userName=userName;
    }
    public int getUserId() {
    	return userId;
    }
    
    public void setUserId(Integer  userId) {
    	this.userId=userId;
    }
    
}
