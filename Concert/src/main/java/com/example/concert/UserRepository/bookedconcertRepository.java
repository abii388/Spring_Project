package com.example.concert.UserRepository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.concert.Model.bookedConcert;
public interface bookedconcertRepository extends JpaRepository <bookedConcert ,Integer> {
	        List<bookedConcert> findByUserId(Integer userId);
	       //works without a cis because Spring Data JPA uses method name conventions to automatically generate
	        //queries behind the scenes.
            
	        
	        //custom @Query 
	        //==================
//	        @Query("SELECT b FROM bookedConcert b WHERE b.userId = :userId")
//	        List<bookedConcert> findByUserId(@Param("userId") Integer userId);
	        
	        List<bookedConcert> findByConcertName(String concertName);
	        
	        //delete Userhistory
	        @Query("SELECT b FROM bookedConcert b WHERE b.userId = :userId AND b.id = :concertId")
	        bookedConcert getBookingByUserAndId(@Param("userId") Integer userId, @Param("concertId") Integer concertId);

	        // Custom query to find booked concerts by concert's ID
//	        @Query("SELECT b FROM bookedConcert b WHERE b.concert.id = :concertId")
//	        List<bookedConcert> findByConcertId(@Param("concertId") Long concertId);
//	        
	        
	        
}
