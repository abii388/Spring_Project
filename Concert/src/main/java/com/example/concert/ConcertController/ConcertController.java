package com.example.concert.ConcertController;
import org.springframework.web.bind.annotation.GetMapping;





import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.concert.DTO.userdto;
import com.example.concert.Services.CustomUserDetail;
import com.example.concert.Services.Services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.concert.Model.ConcertModel;
import com.example.concert.Model.UserModel;
import com.example.concert.Model.bookedConcert;
import com.example.concert.UserRepository.bookedconcertRepository;
import com.example.concert.UserRepository.concertRepository;
import com.example.concert.UserRepository.userRepository;

import jakarta.servlet.http.HttpServletResponse;

//pagination no dependencies
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

//pdf generation  dependencies have
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ConcertController{
	
	@Autowired
	private Services UserServices;
    @Autowired
	private concertRepository concertRepository;//this line declares a reference to an object of type concertRepository — it does NOT create an instance by itself.
//	Then Spring automatically injects (creates and assigns) 
//	the appropriate implementation of the concertRepository interface behind the scenes.
    @Autowired
    private bookedconcertRepository bookedconcertRepository;
    @Autowired
    private userRepository userRepository;
	
@GetMapping("/")

public String Homecontroller() {
	 return "home";
};

//====================================USER AUTHENTICATION ==============================================================

@GetMapping("/register")
public String register(Model model) {
	System.out.println("success");
	 model.addAttribute("user", new userdto());
	return "register";
}

@PostMapping("/registration")
public String saveUser(@ModelAttribute("user") userdto userDto, Model model) {
//		System.out.println("success");
//		System.out.println("===== Form Data Received =====");
//	    System.out.println("Username: " + userDto.getUsername());
//	    System.out.println("Email: " + userDto.getEmail());
//	    System.out.println("Phone: " + userDto.getPhonenumber());
//	    System.out.println("Password: " + userDto.getPassword());
//	    System.out.println("Role: " + userDto.getRole());
//	    System.out.println("================================");
    UserServices.save(userDto);
    model.addAttribute("message", "Registered Successfuly!");
    return "register";
}
@GetMapping("/login")
public String login() {
	return "login";
}
// =======================================================ADMIN HOME ========================================================

@GetMapping("/adminhome")
public String adminpage() {
	 return "admin/adminpage";
}
@GetMapping("/concertform")
public String concertform(Model model) {
	model.addAttribute("concert",new ConcertModel() );//✅ concert — The Model Attribute Name 
//	This is the actual empty object you’re binding the form to.
//
//	When the form is submitted, Spring binds the form data back into a ConcertModel object and passes it to your controller method.
	return "admin/concertform";
}
@PostMapping("/submitform")
public String submitForm(@ModelAttribute("concert") ConcertModel concertmodel ,Model model) {
	
	System.out.println("===== Form Data Received =====");
    System.out.println("concertname: " + concertmodel.getConcertName());
    System.out.println("venue: " + concertmodel.getVenue());
    System.out.println("date: " + concertmodel.getConcertDate());
    System.out.println("Price: " + concertmodel.getTicketPrice());
    System.out.println("availableticket: " + concertmodel. getAvailableTickets());
    System.out.println("================================");
	    concertRepository.save(concertmodel);
	    model.addAttribute("message" ,"concert Created successfuly!");
	    return "admin/concertform";
	
}

@GetMapping("/viewconcert")
public String viewconcert(Model model,@RequestParam(value = "keyword", required = false)String keyword, 
		                   @RequestParam(value = "status", required = false) String status,
		                   @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size,
                           @RequestParam(defaultValue = "concertName") String sortBy,
                           @RequestParam(defaultValue = "asc") String sortDir
		                   ) 
{
   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   CustomUserDetail userDetail=(CustomUserDetail) auth.getPrincipal();
   Integer userId = userDetail.getUserId();
   String userName = userDetail.getUserName();
   String userRole=userDetail.getRole();
   System.out.println("Logged-in User ID: " + userId);
   System.out.println("Logged-in User name: " + userName);
   System.out.println("Logged-in User role: "+userRole);
   if (userRole.equals("ROLE_ADMIN")){
	   Pageable pageable = PageRequest.of(page, size, 
		        sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

	   Page<ConcertModel> concertsPage;
	   List<ConcertModel> concerts;
	   try {
	       if (keyword != null && !keyword.isEmpty()) {
	           LocalDate date = LocalDate.parse(keyword);
	           concertsPage= concertRepository.findByConcertDate(date,pageable);
	       } 
	       else if(status != null && !status.isEmpty()) {
	    	   System.out.print(status);
	    	   concertsPage=concertRepository.findByStatus(status, pageable);
	    	   
	       }
	       else {
	    	   concertsPage = concertRepository.findAll(pageable);
	       }
	   } catch (DateTimeParseException e) {
	       // If keyword is not a date, search by name/venue with status = 'Enable'
		   concertsPage= concertRepository.findAllEnabledByKeyword(keyword, pageable);
	   }

	    model.addAttribute("concertPage", concertsPage);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", concertsPage.getTotalPages());
	    model.addAttribute("sortBy", sortBy);
	    model.addAttribute("sortDir", sortDir);
	    model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "admin/viewconcert";
   }
  else {
	  return "login";
  }
	
}
@GetMapping("/display/{id}")
public String displayconcert(@PathVariable Integer id,  Model model) {
	Optional<ConcertModel> data=concertRepository.findById(id);
	ConcertModel concert=data.get();
	model.addAttribute("concert", concert);
	return "admin/displayconcert";
}
//view the which users are booked the concert
@GetMapping("/viewusers/{concertName}")
public String viewusers(Model model, @PathVariable String concertName) {
	List<bookedConcert> data = bookedconcertRepository.findByConcertName(concertName);
	System.out.print(data);
	model.addAttribute("concert", data);
	return "admin/bookedconcert";
}

@GetMapping("/status/{id}")
public String toggleConcertStatus(@PathVariable("id") Integer id) {
    Optional<ConcertModel> optionalConcert = concertRepository.findById(id);
    if (optionalConcert.isPresent()) {
        ConcertModel concert = optionalConcert.get();
        String currentStatus = concert.getStatus();
        
        concert.setStatus(currentStatus.equals("Enable") ? "Disable" : "Enable");
        concertRepository.save(concert);
    }
    return "redirect:/viewconcert"; // or your concerts list route
}







@GetMapping ("/edit/{id}")
public String editconcert(@PathVariable Integer id ,Model model) {

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
	Optional <ConcertModel> value=concertRepository.findById(id);
	ConcertModel concert=value.get();

	model.addAttribute("concert",concert);
	return "admin/editform";
	
	
}
@PostMapping("/admin/update/{id}")
public String updateconcert(@PathVariable Integer id, String concertName, LocalDate concertDate, int ticketPrice, 
        String venue, int availableTickets,Model model) {
        Optional<ConcertModel> optionalConcert = concertRepository.findById(id);
    
    // Check if the concert exists
   
        ConcertModel existingConcert = optionalConcert.get();
        
        // Update the concert fields with the submitted data
        existingConcert.setConcertName(concertName);
        existingConcert.setConcertDate(concertDate);
        existingConcert.setTicketPrice(ticketPrice);
        existingConcert.setVenue(venue);
        existingConcert.setAvailableTickets(availableTickets);

        // Save the updated concert to the database
        concertRepository.save(existingConcert);

        // Redirect to the concert list or any other page
        return "redirect:/viewconcert";
	
}
//===========================best practice==================================
//@PostMapping("/updateconcert")
//public String updateConcert(@ModelAttribute ConcertModel concert) {
//    concertRepository.save(concert); // Automatically updates because ID is present
//    return "redirect:/viewconcert";
//}

@GetMapping("/delete/{id}")
public String delete(@PathVariable Integer id ,Model model) {
	  ConcertModel concert=concertRepository.findById(id).get();
	  concertRepository.delete(concert);
	  return  "redirect:/viewconcert";
	
}

//@GetMapping("/bookedconcert")
//public String bookedconcert(Model model) {
//	List<bookedConcert> data=bookedconcertRepository.findAll();
//	model.addAttribute("concert",data);
//	return "admin/bookedconcert";
//}
@GetMapping("/manageuser")
public String manageuser(Model model) {
	List<UserModel> userdata;
	userdata=userRepository.findAll();
	System.out.print(userdata);
	model.addAttribute("users", userdata);
	return "admin/userdetails";
	
}
@GetMapping("/userview/{id}")
public String viewuser(Model model, @PathVariable Long id) {
    Optional<UserModel> userOptional = userRepository.findById(id);
   UserModel data=userOptional.get();
   model.addAttribute("user",data);
   return "admin/userview";
   
}
@GetMapping("/usereditform/{id}")
public String edituser(Model model ,@PathVariable Long id) {
	Optional<UserModel> user=userRepository.findById(id);
	
	model.addAttribute("user",user.get());
	return "admin/usereditform";
	
}
@PostMapping("/userupdate/{id}")
public String updateuser(Model model,@PathVariable Long id ,@ModelAttribute("user") UserModel updatedUser) {
	
	Optional<UserModel> olduser=userRepository.findById(id);
	
	if (olduser.isPresent()) {
		UserModel existing_user=olduser.get();
		existing_user.setUserName(updatedUser.getUserName());
		existing_user.setEmail(updatedUser.getEmail());
		existing_user.setPhoneNumber(updatedUser.getPhoneNumber());
		userRepository.save(existing_user);
	}
	return "redirect:/manageuser";

		
	}
@GetMapping("/deleteuser/{id}")
public String deleteuser(@PathVariable Long id,Model model ) {
	System.out.println(id);
	Optional <UserModel> data=userRepository.findById(id);
	if (data.isPresent()) {
		UserModel user=data.get();
	    userRepository.delete(user);
	}
	else {
		System.out.println("user is not found");
	}
    return "redirect:/manageuser";
	
	

	
}


//=========================d====================================USER HOME======================================================

@GetMapping("/userhome")
public String userpage() {
	return "user/userhome";
}


@GetMapping("/listconcert")
public String listconcert(Model model, @RequestParam(value = "keyword", required = false)String keyword) {
   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//   What's happening?
//   You're accessing the SecurityContextHolder, which holds security information for the current thread.
//   You're getting the current Authentication object, which represents the currently logged-in user.
   CustomUserDetail userDetail=(CustomUserDetail) auth.getPrincipal();
//   What's happening?
//   auth.getPrincipal() returns the currently authenticated user object.
//   By default, this is an object that implements UserDetails — in your case, it's your custom class CustomUserDetail.
//   So you cast it to CustomUserDetail so you can access your custom methods like getUserId() and getUserName().
   Integer userId = userDetail.getUserId();
   String userName = userDetail.getUserName();
//   Line	What it does
//   SecurityContextHolder...	  Gets the logged-in user's auth info
//   auth.getPrincipal()	      Gets the current user object
//   Cast to CustomUserDetail	  Allows access to your custom methods
//   getUserId()	              Retrieves the current user's ID from the database
//   getUserName()	              Retrieves the current user's name
   System.out.println("Logged-in User ID: " + userId);
   System.out.println("Logged-in User ID: " + userName);
   List<ConcertModel> concerts;

   try {
       if (keyword != null && !keyword.isEmpty()) {
           LocalDate date = LocalDate.parse(keyword);
           concerts = concertRepository.findByConcertDateAndStatus(date, "Enable");
       } else {
           concerts = concertRepository.findByStatus("Enable",Sort.by(Sort.Order.asc("concertName")));
       }
   } catch (DateTimeParseException e) {
       // If keyword is not a date, search by name/venue with status = 'Enable'
       concerts = concertRepository.findAllEnabledByKeyword(keyword);
   }

   model.addAttribute("concert", concerts);
   return "user/listconcert";
}
@GetMapping("/bookticket/{id}")
public String bookticket(@PathVariable Integer id, Model model) {
    Optional<ConcertModel> data = concertRepository.findById(id);
   
        ConcertModel concert = data.get();
//        concert is now an object of type ConcertModel.
//
//        That means it already is a concert — not something inside another concert
        bookedConcert bc = new bookedConcert();
        bc.setConcert(concert);//concert is now an object of type ConcertModel.
        //That means it already is a concert — not something inside another concert
//        💡 So Why concert.getConcert() Doesn't Work?
//        Because ConcertModel does not have any method named getConcert().
//        You're treating it like it contains another concert inside it — but it doesn't
        bc.setConcertName(concert.getConcertName());
        bc.setConcertDate(concert.getConcertDate());
        bc.setTicketPrice(concert.getTicketPrice());
        bc.setVenue(concert.getVenue());
        model.addAttribute("availableTickets", concert.getAvailableTickets());
        model.addAttribute("confirmconcert", bc);
        return "user/bookingform";
    
}
@PostMapping("/confirmticket/{id}")
public String confirmticket(@PathVariable Integer id, @ModelAttribute("confirmconcert") bookedConcert bookedconcert, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetail userDetail = (CustomUserDetail) auth.getPrincipal();
    Integer userId = userDetail.getUserId();
    String userName = userDetail.getUserName();

    bookedconcert.setId(null);
    bookedconcert.setUserName(userName);
    bookedconcert.setUserId(userId);

    Optional<ConcertModel> optionalConcert = concertRepository.findById(id);
    if (optionalConcert.isPresent()) {
        ConcertModel concert = optionalConcert.get();
        Integer available = concert.getAvailableTickets();
        Integer requested = bookedconcert.getNumberofPerson();
       

        // Check if enough tickets are available
        if (available >= requested) {
            // Save booking
            bookedconcertRepository.save(bookedconcert);

            
            Integer remaining = available - requested;
            concert.setAvailableTickets(remaining);
            concertRepository.save(concert);

            model.addAttribute("message", "Ticket booked successfully!");
        } else {
           
            model.addAttribute("message", "Not enough tickets available. Only " + available + " left.");
        }
    } else {
        model.addAttribute("message", "Concert not found.");
    }

    model.addAttribute("confirmconcert",bookedconcert);
    return "user/bookingform";
}

@GetMapping("/userhistory")
public String userhistory(Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    CustomUserDetail userDetail = (CustomUserDetail) auth.getPrincipal();//is a type cast in Java
//	    What auth.getPrincipal() Returns
//	    By default, auth.getPrincipal() returns an object of type Object or sometimes UserDetails.
//	    But in your app, you've created a custom class called CustomUserDetail (likely implementing UserDetails) that holds extra info like getUserId().
//	    Since getPrincipal() returns a generic type (Object), Java doesn't know it is your CustomUserDetail unless you explicitly cast it.
	    Integer userId = userDetail.getUserId();
	    List<bookedConcert> userBookings = bookedconcertRepository.findByUserId(userId);  
	    model.addAttribute("concert",userBookings);
	    return "user/userhistory";
}
@GetMapping("/deletehistory/{id}")
public String deletehistory(@PathVariable Integer id,Model model ) {
	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 CustomUserDetail userDetail = (CustomUserDetail) auth.getPrincipal();
	 Integer userId = userDetail.getUserId();
	 System.out.println("user_id:"+userId);
	 System.out.println("Concert ID: " + id);
      bookedConcert data=(bookedConcert) bookedconcertRepository.getBookingByUserAndId(userId , id);
	if (data != null) {
	     bookedconcertRepository.delete(data);
	}
	else {
		System.out.println("user is not found");
	}
    return "redirect:/userhistory";
}


@GetMapping("/pdf_generate/{id}")
public void pdf_generate(@PathVariable Integer id, Model model, HttpServletResponse response) throws IOException {
    Optional<bookedConcert> data = bookedconcertRepository.findById(id);

    if (data.isPresent()) {
        bookedConcert concert = data.get();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=concert_" + concert.getId() + ".pdf");

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

                // Concert Name
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Name: " + (concert.getConcertName() != null ? concert.getConcertName() : "N/A"));
                contentStream.endText();

                // Concert Date
                yPosition -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Date: " + (concert.getConcertDate() != null ? concert.getConcertDate().toString() : "N/A"));
                contentStream.endText();

                // Number of Persons
                yPosition -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Persons: " + (concert.getNumberofPerson() != null ? concert.getNumberofPerson().toString() : "N/A"));
                contentStream.endText();

                // Total Price
                yPosition -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Total Price: " + (concert.getTotalPrice() != 0 ? concert.getTotalPrice(): "N/A"));
                contentStream.endText();
            }

            document.save(response.getOutputStream());
        }
    } else {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Concert booking not found");
    }


	
}
// Path where files will be stored
private static String UPLOADED_FOLDER = "uploads/";

// Show the file upload page
@GetMapping("/uploadfile")
public String showUploadPage(Model model) {
    // Get the list of files from the "uploads" directory
    File folder = new File(UPLOADED_FOLDER);
    String[] fileNames = folder.list((dir, name) -> name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".avif"));
   System.out.println(fileNames);
    // Pass the list of image files to the view
    model.addAttribute("files", fileNames);
    return "user/gallery";
}

@PostMapping("/uploadfile")
public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
    if (file.isEmpty()) {
        model.addAttribute("message", "Please select a file to upload.");
        return "user/gallery";
    }

    try {
        // Create the directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOADED_FOLDER);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, bytes);

        model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");

    } catch (Exception e) {
        model.addAttribute("message", "Failed to upload '" + file.getOriginalFilename() + "' -> " + e.getMessage());
    }
    return "user/gallery";
}





}




