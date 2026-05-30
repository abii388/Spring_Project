package com.example.concert1.UserRepository;


import java.time.LocalDate;


import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.concert1.Model.ConcertModel;
public interface concertRepository extends JpaRepository<ConcertModel, Integer> {

    // Match concertDate exactly
    Page<ConcertModel> findByConcertDateAndStatus(LocalDate date, String status,Pageable pageable);

    // Flexible keyword search on name or venue
 // Get concerts by keyword (name or venue) AND status = 'Enable'
    @Query("SELECT c FROM ConcertModel c WHERE (LOWER(c.concertName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.venue) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND c.status = 'Enable'")
    
    Page<ConcertModel> findAllEnabledByKeyword(@Param("keyword") String keyword, Pageable pageable);
    Page<ConcertModel> findByStatus(String status, Pageable pageable);
    Page<ConcertModel> findByConcertDate(LocalDate date, Pageable pageable);
   
    
    //==============================================================================================================
    
    // Match concertDate exactly
    List<ConcertModel> findByConcertDateAndStatus(LocalDate date, String status);

    // Flexible keyword search on name or venue
 // Get concerts by keyword (name or venue) AND status = 'Enable'
    @Query("SELECT c FROM ConcertModel c WHERE (LOWER(c.concertName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.venue) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND c.status = 'Enable'")
    
    List<ConcertModel> findAllEnabledByKeyword(@Param("keyword") String keyword);
    List<ConcertModel> findByStatus(String status, Sort sort);
     List<ConcertModel> findByConcertDate(LocalDate date);
    
}
