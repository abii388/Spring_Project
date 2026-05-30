package com.example.concert1.controller;


import java.time.LocalDate;



import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//import com.example.concert1.Services.CustomUserDetail;
import com.example.concert1.Model.bookedConcert;
//import com.example.concert1.DTO.BookedConcertDTO;
//import com.example.concert1.DTO.BookedConcertRequest;
import com.example.concert1.DTO.userdto;
import com.example.concert1.Model.UserModel;
import com.example.concert1.SecurityConfig.TokenGenerator;
import com.example.concert1.Services.Services;
import com.example.concert1.UserRepository.bookedconcertRepository;
import com.example.concert1.UserRepository.concertRepository;
import com.example.concert1.UserRepository.userRepository;
import com.example.concert1.Model.ConcertModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DatabaseMetaData;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class Controller {
	
	//=====================================Authentication ===============================================================
	
	
	
//	 Why Use @Autowired private Services service;?
//	This is dependency injection — a key concept in Spring. 
//	You're injecting an instance of your service class (Services) into your controller so you can call its methods (like save())
//	
//	         Why not create it manually using new Services()?
//			Spring manages your service classes as beans. By using @Autowired, you’re telling Spring:
//
//			“Give me the already-created instance of Services from the Spring context.”
//	
//	     if you did:
//        Services service = new Services();
//		You’d bypass Spring, and any Spring-managed features (like @Transactional, @Service, etc.) inside that class wouldn't work properly.
	@Autowired   
	private Services service;
	
	@Autowired 
	private TokenGenerator tokenGenerator;
	
	@Autowired
	private userRepository userrepository;
	
	@Autowired
	private concertRepository concertrepository;
	@Autowired
	private bookedconcertRepository  bookedconcertrepository ;
	
	
	
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody userdto userDto) {
    	System.out.print("hi");
        System.out.print(userDto.getPassword());
        System.out.print(userDto.getconfirmPassword());
    	if(!userDto.getPassword().equals(userDto.getconfirmPassword())) {
    		 return ResponseEntity  .badRequest()
    	                .body("Password and Confirm Password do not match!");
    		
    	}
        service.save(userDto);
        return ResponseEntity.ok("User registered successfully!");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody userdto user) {
    	System.out.printf("hello: %s %s%n", user.getEmail(), user.getPassword());
    	 
        String token = tokenGenerator.generateToken(user.getEmail(), user.getPassword());
        
       
        if (token != null) {
        	UserModel currentuser=userrepository.findByEmail(user.getEmail());
            return ResponseEntity.ok(Map.of("token", token,"id",currentuser.getId(),"role",currentuser.getRole()));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    
    @PostMapping("/logout")
    public ResponseEntity <Map<String,Object>> logout(@RequestHeader("Authorization") String authHeader) {
//    	    So for your /api/logout flow:
//    		You don’t need to mention ApiAuthenticationFilter inside the controller.
//
//    		It is automatically executed by Spring Security before your controller method is even entered.
//
//    		Once the filter passes (valid token), then the controller runs and performs the logout (i.e., setToken(null) etc.).
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            UserModel user = userrepository.findByToken(token);
            if (user != null) {
                user.setToken(null);
                userrepository.save(user);
                return ResponseEntity.ok(Map.of(
                	    "message", "Logout successful",  // Corrected the spelling of "message"
                	    "token", token,
                	    "id", user.getId(),
                	    "role", user.getRole()
                	));
            }

            return ResponseEntity.status(401).body( Map.of("message","Invalid token"));
        }
        return ResponseEntity.badRequest().body(Map.of("message","Missing Authorization header"));
    }

    //=========================================Admin page ===========================================================
// In postman date format is yyyy-mm-dd 
@PostMapping("/admin/createconcert")
public ResponseEntity <Map<String,Object>> createconcert(@RequestHeader("Authorization") String authHeader,
		                   @RequestBody ConcertModel concertmodel ){
	 if (authHeader != null && authHeader.startsWith("Bearer ")) {
         String token = authHeader.substring(7); 
         UserModel user = userrepository.findByToken(token);
	     if (user.getRole().equals("ROLE_ADMIN")) {
	    	     concertrepository.save(concertmodel);
	    	     return ResponseEntity.status(200).body( Map.of("message","concert successfully"));
	    	 
	     }
	      return ResponseEntity.badRequest().body(Map.of("message","Your not Admin"));

	 }
	
     return ResponseEntity.badRequest().body(Map.of("message","Missing Authorization header"));

}
@GetMapping("/admin/viewconcert")
public ResponseEntity<?> viewConcert(
        @RequestHeader("Authorization") String authHeader,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "concertName") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir) {

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
    }

    Pageable pageable = PageRequest.of(
            page, size,
            sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
    );

    Page<ConcertModel> concertPage;

    try {
        if (keyword != null && !keyword.isEmpty()) {
            // Try parsing as date
            LocalDate date = LocalDate.parse(keyword);
            concertPage = concertrepository.findByConcertDate(date, pageable);
        } else if (status != null && !status.isEmpty()) {
            concertPage = concertrepository.findByStatus(status, pageable);
        } else {
            concertPage = concertrepository.findAll(pageable);
        }
    } catch (DateTimeParseException e) {
        // Fallback to keyword-based search on name or venue with 'Enable' status
        concertPage = concertrepository.findAllEnabledByKeyword(keyword, pageable);
    }

    return ResponseEntity.ok(
        Map.of(
            "concerts", concertPage.getContent(),
            "currentPage", concertPage.getNumber(),
            "totalItems", concertPage.getTotalElements(),
            "totalPages", concertPage.getTotalPages(),
            "sortBy", sortBy,
            "sortDir", sortDir,
            "reverseSortDir", sortDir.equals("asc") ? "desc" : "asc"
        )
    );
}
//common for the user and admin
@GetMapping("/display/{id}")
public ResponseEntity <Map<String,Object>> displayconcert(@PathVariable Integer id ,@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }
	Optional<ConcertModel> data=concertrepository.findById(id);
	ConcertModel concert=data.get();
	return ResponseEntity.ok(
			Map.of(
					"data",concert)
			);
	
}

//view the which users are booked the concert
@GetMapping("/admin/viewusers/{concertName}")
public ResponseEntity <?> viewusers( @PathVariable String concertName ,@RequestHeader("Authorization") String authHeader) {
	 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
	    }

	    String token = authHeader.substring(7);
	    UserModel user = userrepository.findByToken(token);

	    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
	        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
	    }

	List<bookedConcert> data = bookedconcertrepository.findByConcertName(concertName);
	System.out.print(data);
	if(data != null) {
	return ResponseEntity.ok(
			Map.of(
					"data",data)
			);
	}
	return ResponseEntity.ok(
			Map.of(
					"message","no data booking available"));
}
    

@GetMapping("/admin/status/{id}")
public ResponseEntity <Map<String,Object>> toggleConcertStatus(@PathVariable Integer id ,@RequestHeader("Authorization") String authHeader ) {
	 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
	    }

	    String token = authHeader.substring(7);
	    UserModel user = userrepository.findByToken(token);

	    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
	        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
	    }
    Optional<ConcertModel> optionalConcert = concertrepository.findById(id);
    if (optionalConcert.isPresent()) {
        ConcertModel concert = optionalConcert.get();
        String currentStatus = concert.getStatus();
        
        concert.setStatus(currentStatus.equals("Enable") ? "Disable" : "Enable");
        concertrepository.save(concert);
        return  ResponseEntity.ok(Map.of(
     		   "data",concert
     		)) ;
    }
    return  ResponseEntity.ok(Map.of(
  		   "data","concert not available"
  		)) ;
   
}

@GetMapping ("/admin/edit/{id}")
public ResponseEntity <Map<String,Object>> editconcert(@PathVariable Integer id ,@RequestHeader("Authorization") String authHeader) {

	//This returns an Optional<ConcertModel>, because the ID might not exist in the database.
//	So Spring Data JPA uses Optional to avoid null pointer exceptions
	//You're passing an Optional, not the actual ConcertModel object. Thymeleaf doesn't know how to bind form fields to an Optional
//	You need to extract the actual object from the Optional before passing it to the model.
//	why use optional?
//	That can lead to:
//
//		NullPointerExceptions
//
//		Forgetting to handle null
//
//		Repetitive null-check boilerplate
////	
//	Why is Optional used?
//	Optional<T> is a wrapper around an object that may or may not be present. 
//	It's a way to avoid NullPointerException in case the value you're searching for doesn't exist in the database
	 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
	    }

	    String token = authHeader.substring(7);
	    UserModel user = userrepository.findByToken(token);

	    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
	        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
	    }
	Optional <ConcertModel> value=concertrepository.findById(id);
	ConcertModel concert=value.get();

	
	return ResponseEntity.ok(Map.of(
			"data",concert
			));
	
	
}

@PostMapping("/admin/update/{id}")
public ResponseEntity <Map<String,Object>> updateconcert(@PathVariable Integer id,@RequestBody ConcertModel concertmodel,@RequestHeader("Authorization") String authHeader) {
	 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
	    }

	    String token = authHeader.substring(7);
	    UserModel user = userrepository.findByToken(token);

	    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
	        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
	    }
        Optional<ConcertModel> optionalConcert = concertrepository.findById(id);
    
    // Check if the concert exists
   
        ConcertModel existingConcert = optionalConcert.get();
        
        // Update the concert fields with the submitted data
        existingConcert.setConcertName(concertmodel.getConcertName());
        existingConcert.setConcertDate(concertmodel.getConcertDate());
        existingConcert.setTicketPrice(concertmodel.getTicketPrice());
        existingConcert.setVenue(concertmodel.getVenue());
        existingConcert.setAvailableTickets(concertmodel.getAvailableTickets());

        // Save the updated concert to the database
        concertrepository.save(existingConcert);

        // Redirect to the concert list or any other page
        return ResponseEntity.ok(Map.of(
        		"Message","Successfully uodated"
        		));
	
}
@DeleteMapping("/admin/delete/{id}")
public ResponseEntity <Map<String,Object>>  delete(@PathVariable Integer id ,@RequestHeader("Authorization") String authHeader) {
	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
    }
	  ConcertModel concert=concertrepository.findById(id).get();
	  concertrepository.delete(concert);
	  return ResponseEntity.ok(Map.of(
      		"Message","Successfully deleted"
      		));
	
}

@GetMapping("/admin/manageuser")
public ResponseEntity <Map<String,Object>>  manageuser(@RequestHeader("Authorization") String authHeader) {
	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
    }
	List<UserModel> userdata;
	userdata=userrepository.findAll();
	 return ResponseEntity.ok(Map.of(
	      		"date",userdata
	      		));
		
	
}
@GetMapping("/admin/userview/{id}")
public ResponseEntity <Map<String,Object>> viewuser(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
    }
    Optional<UserModel> userOptional = userrepository.findById(id);
    if (userOptional.isEmpty()) {
        return ResponseEntity.status(404).body(Map.of("message", "No user found with ID: " + id));
    }
     UserModel data=userOptional.get();
  
   return ResponseEntity.ok(Map.of(
     		"date",data
     		));
   
}
@GetMapping("/admin/usereditform/{id}")
public ResponseEntity <Map<String,Object>> edituser(@RequestHeader("Authorization") String authHeader ,@PathVariable Long id) {
	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
    }
	Optional<UserModel> userOptional=userrepository.findById(id);
	  if (userOptional.isEmpty()) {
	        return ResponseEntity.status(404).body(Map.of("message", "No user found with ID: " + id));
	    }
	     UserModel data=userOptional.get();
	 return ResponseEntity.ok(Map.of(
	     		"date",data
	     		));
	
}

@PostMapping("/admin/userupdate/{id}")
public ResponseEntity <Map<String,Object>> updateuser(@PathVariable Long id ,@RequestBody UserModel updatedUser ,@RequestHeader("Authorization") String authHeader) {
	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
    }
	
	Optional<UserModel> olduser=userrepository.findById(id);
	
	
	if (olduser.isPresent()) {
		UserModel existing_user=olduser.get();
		existing_user.setUserName(updatedUser.getUserName());
		existing_user.setEmail(updatedUser.getEmail());
		existing_user.setPhoneNumber(updatedUser.getPhoneNumber());
		userrepository.save(existing_user);
		 return ResponseEntity.ok(Map.of(
		     		"message","update successfully"
		     		));
		
	}
	return ResponseEntity.status(404).body(Map.of("message", "No user found with ID: " + id));

		
	}

@DeleteMapping("/admin/deleteuser/{id}")
public ResponseEntity <Map<String,Object>> deleteuser(@PathVariable Long id,@RequestHeader("Authorization") String authHeader ) {
	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null || !user.getRole().equals("ROLE_ADMIN")) {
        return ResponseEntity.status(403).body(Map.of("message", "Access denied. Not an Admin."));
    }
	
	System.out.println(id);
	Optional <UserModel> data=userrepository.findById(id);
	if (data.isPresent()) {
		UserModel User=data.get();
	    userrepository.delete(User);
	}
	else {
		return ResponseEntity.status(404).body(Map.of("message", "No user found with ID: " + id));
	}
	 return ResponseEntity.ok(Map.of(
	     		"message","user delete successfully"
	     		));
	
	

	
}

//=====================================userpage=============================================================

@GetMapping("/user/listconcert")
public ResponseEntity <Map<String,Object>> listconcert( @RequestParam(value = "keyword", required = false)String keyword,@RequestHeader("Authorization") String authHeader) 
{
 
	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);
    
    if (user != null) {
    List<ConcertModel> concerts;

   try {
       if (keyword != null && !keyword.isEmpty()) {
           LocalDate date = LocalDate.parse(keyword);
           concerts = concertrepository.findByConcertDateAndStatus(date, "Enable");
       } else {
           concerts = concertrepository.findByStatus("Enable",Sort.by(Sort.Order.asc("concertName")));
           
       }
   } catch (DateTimeParseException e) {
    
       concerts = concertrepository.findAllEnabledByKeyword(keyword);
   }

   return ResponseEntity.ok(Map.of(
    		"data",concerts
    		));
    }

    return ResponseEntity.ok(Map.of(
		"message","user isnot found"
		));
}
//================================================using with BookedconcertDTO and BookedConcertREquest ===================================================
//@GetMapping("/user/bookticket/{id}")
//public ResponseEntity<Map<String, Object>> bookticket(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
//    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
//    }
//
//    String token = authHeader.substring(7);
//    UserModel user = userrepository.findByToken(token);
//
//    if (user != null) {
//        Optional<ConcertModel> data = concertrepository.findById(id);
//        if (data.isPresent()) {
//            ConcertModel concert = data.get();
//
//            // Build DTO
//            BookedConcertDTO dto = new BookedConcertDTO(
//                concert.getConcertName(),
//                concert.getConcertDate(),
//                concert.getTicketPrice(),
//                concert.getVenue()
//            );
//
//            return ResponseEntity.ok(Map.of(
//                "message", "Concert retrieved successfully",
//                "concert", dto
//            ));
//        } else {
//            return ResponseEntity.status(404).body(Map.of("message", "No concert found with ID: " + id));
//        }
//    }
//
//    return ResponseEntity.status(404).body(Map.of("message", "User not found"));
//}
//@PostMapping("/user/confirmbook/{id}")
//public ResponseEntity<?> confirmBooking(
//        @PathVariable Integer id,
//        @RequestBody BookedConcertRequest request,
//        @RequestHeader("Authorization") String authHeader) {
//
//    String token = authHeader.substring(7);
//    UserModel user = userrepository.findByToken(token);
//
//    if (user == null) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
//    }
//
//    Optional<ConcertModel> optionalConcert = concertrepository.findById(id);
//    if (optionalConcert.isPresent()) {
//        ConcertModel concert = optionalConcert.get();
//        Integer available = concert.getAvailableTickets();
//        Integer requested = request.getNumberofPerson();
//
//        if (available >= requested) {
//            bookedConcert booking = new bookedConcert();
//
//            // ✅ Set all fields properly
//            booking.setUserId(user.getId());
//            booking.setUserName(user.getUserName());
//            booking.setNumberofPerson(requested);
//            booking.setTotalPrice(requested * concert.getTicketPrice());
//
//            // These fields were missing:
//            booking.setConcertName(concert.getConcertName());
//            booking.setConcertDate(concert.getConcertDate());
//            booking.setTicketPrice(concert.getTicketPrice());
//            booking.setVenue(concert.getVenue());
//
//            booking.setConcert(concert);  // setting concert reference (ManyToOne)
//
//            bookedconcertrepository.save(booking);
//
//            // Update available ticket count
//            concert.setAvailableTickets(available - requested);
//            concertrepository.save(concert);
//
//            return ResponseEntity.ok(Map.of(
//                "message", "Ticket booked successfully"
//            ));
//        } else {
//            return ResponseEntity.badRequest().body(Map.of(
//                "message", "Not enough tickets. Only " + available + " left."
//            ));
//        }
//    } else {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
//            "message", "Concert not found."
//        ));
//    }
//}

//=======================================================================================================================
@GetMapping("/user/bookticket/{id}")
public ResponseEntity<Map<String, Object>> bookticket(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body(Map.of("message", "Missing or invalid Authorization header"));
    }

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user != null) {
        Optional<ConcertModel> data = concertrepository.findById(id);
        if (data.isPresent()) {
            ConcertModel concert = data.get();
            bookedConcert bookedconcert=new bookedConcert();
            bookedconcert.setConcert(concert);
            bookedconcert.setConcertName(concert.getConcertName());
            bookedconcert.setConcertDate(concert.getConcertDate());
            bookedconcert.setTicketPrice(concert.getTicketPrice());
            return ResponseEntity.ok(Map.of(
                "message", "Concert retrieved successfully",
                 "concertname",bookedconcert.getConcertName(),
                 "concertdate",bookedconcert.getConcertDate(),
                 "Ticketprice",bookedconcert.getTicketPrice()
                
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "No concert found with ID: " + id));
        }
    }

    return ResponseEntity.status(404).body(Map.of("message", "User not found"));
}
@PostMapping("/user/confirmbook/{id}")
public ResponseEntity<?> confirmBooking(
        @PathVariable Integer id,
        @RequestBody bookedConcert bookedconcert,
        @RequestHeader("Authorization") String authHeader) {

    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
    }
    bookedconcert.setId(null);
    bookedconcert.setUserName(user.getUserName());
    bookedconcert.setUserId(user.getId());

    Optional<ConcertModel> optionalConcert = concertrepository.findById(id);
    if (optionalConcert.isPresent()) {
        ConcertModel concert = optionalConcert.get();
        Integer available = concert.getAvailableTickets();
        Integer requested = bookedconcert.getNumberofPerson();
        if (requested >3) {

            return ResponseEntity.badRequest().body(Map.of(
                "message", "A user can only booke 3 Tickets"));
        }

        if (available >= requested) {
            bookedConcert booking = new bookedConcert();

            // ✅ Set all fields properly
            booking.setUserId(user.getId());
            booking.setUserName(user.getUserName());
            booking.setNumberofPerson(requested);
            booking.setTotalPrice(requested * concert.getTicketPrice());

            // These fields were missing:
            booking.setConcertName(concert.getConcertName());
            booking.setConcertDate(concert.getConcertDate());
            booking.setTicketPrice(concert.getTicketPrice());
            booking.setVenue(concert.getVenue());

            booking.setConcert(concert);  // setting concert reference (ManyToOne)

            bookedconcertrepository.save(booking);

            // Update available ticket count
            concert.setAvailableTickets(available - requested);
            concertrepository.save(concert);

            return ResponseEntity.ok(Map.of(
                "message", "Ticket booked successfully"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Not enough tickets. Only " + available + " left."
            ));
        }
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "message", "Concert not found."
        ));
    }
}


@GetMapping("/userhistory")
public  ResponseEntity<?> userhistory(

        @RequestHeader("Authorization") String authHeader) {
	    String token = authHeader.substring(7);
	    UserModel user = userrepository.findByToken(token);
       
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
	    }
	    Integer userId=user.getId();
	    System.out.print(userId);
	    List <bookedConcert> concert = bookedconcertrepository.findByUserId(userId);
	
	    return ResponseEntity.ok(Map.of(
	            "message", concert
	        ));
}
@DeleteMapping("/user/deletehistory/{id}")
public ResponseEntity<?>  deletehistory(@PathVariable Integer id,
      
        @RequestHeader("Authorization") String authHeader) {
	
	     String token = authHeader.substring(7);
	    UserModel user = userrepository.findByToken(token);
    
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
	     }
	     Integer userId=user.getId();
          bookedConcert data=(bookedConcert) bookedconcertrepository.getBookingByUserAndId(userId , id);
		if (data != null) {
		     bookedconcertrepository.delete(data);
		     return ResponseEntity.ok(Map.of(
			            "message", "deleted sucessfully"
			        ));
		}
		 return ResponseEntity.ok(Map.of(
		            "message", "no data found"
		        ));
         
}

private static String UPLOADED_FOLDER = "uploads/";

//Show the file upload page
@GetMapping("/uploadfile")
public ResponseEntity<?> getUploadedFiles() {
    File folder = new File(UPLOADED_FOLDER);
    String[] fileNames = folder.list((dir, name) -> 
        name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".avif") ||  name.endsWith(".webp")
    );

    if (fileNames == null || fileNames.length == 0) {
        return ResponseEntity.ok(Map.of("message", "No files found"));
    }

    return ResponseEntity.ok(Map.of("files", fileNames));
}

@PostMapping("/user/uploadfile")
public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
    System.out.printf("Uploaded file: %s, Size: %d bytes\n", file.getOriginalFilename(), file.getSize());

    if (file == null || file.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("message", "Please select a non-empty file to upload."));
    }

    try {
        Path uploadPath = Paths.get(UPLOADED_FOLDER);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.write(filePath, file.getBytes());

        return ResponseEntity.ok(Map.of("message", "Upload successful: '" + file.getOriginalFilename() + "'"));

    } catch (Exception e) {
        e.printStackTrace(); // Log the exception for debugging
        return ResponseEntity.internalServerError()
                .body(Map.of("message", "Upload failed: " + e.getMessage()));
    }
}

//@GetMapping("/user/image/{filename:.+}")
//public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//    try {
//        Path filePath = Paths.get(UPLOADED_FOLDER).resolve(filename).normalize();
//        Resource resource = new UrlResource(filePath.toUri());
//
//        if (resource.exists() && resource.isReadable()) {
//            String contentType = Files.probeContentType(filePath);
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(null);
//        }
//
//    } catch (IOException e) {
//        e.printStackTrace();
//        return ResponseEntity.internalServerError().build();
//    }
//}


@GetMapping("/user/pdf_generate/{id}")
public ResponseEntity<byte[]> pdf_generate(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) throws IOException {
    String token = authHeader.substring(7);
    UserModel user = userrepository.findByToken(token);

    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    Integer userId = user.getId();
    bookedConcert concert = bookedconcertrepository.getBookingByUserAndId(userId, id);

    if (concert != null) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Concert Details");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                int yPosition = 720;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Name: " + (concert.getConcertName() != null ? concert.getConcertName() : "N/A"));
                contentStream.endText();

                yPosition -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Date: " + (concert.getConcertDate() != null ? concert.getConcertDate().toString() : "N/A"));
                contentStream.endText();

                yPosition -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Persons: " + (concert.getNumberofPerson() != null ? concert.getNumberofPerson().toString() : "N/A"));
                contentStream.endText();

                yPosition -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Total Price: " + (concert.getTotalPrice() != 0 ? concert.getTotalPrice() : "N/A"));
                contentStream.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            byte[] pdfBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("concert_" + concert.getId() + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}





}

