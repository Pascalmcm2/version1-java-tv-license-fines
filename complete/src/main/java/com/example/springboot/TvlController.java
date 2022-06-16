package com.example.springboot;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

// @RestController
// https://spring.io/guides/gs/handling-form-submission/
@Controller
public class TvlController {

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@PostMapping("/index")
	public String index() {
		return "index";
	}

	@PostMapping("/fines")
	public String fines(Model model, TvlUser user, @RequestParam String email) {
		TvlRepository.getByEmail(email);
		model.addAttribute("userfines", TvlRepository.userfines);

		return "fines";
	}

	@GetMapping("/fines")
	public String fines(Model model) {
		model.addAttribute("userfines", TvlRepository.userfines);

		return "fines";
	}

	@GetMapping("/fine")
	public String fine(Model model, @RequestParam String id) throws ParseException {

		int fineId = Integer.parseInt(id);
		List<String> fine = TvlRepository.getById(fineId);

		// Get number of days from fine was issued
		// Fine are generally £130 or £65 if paid within 28 days
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date fineDate = sdf.parse(fine.get(3));
		Date now = new Date();
		long diff = now.getTime() - fineDate.getTime();
		TimeUnit time = TimeUnit.DAYS;
		long fineAge = time.convert(diff, TimeUnit.MILLISECONDS);

		model.addAttribute("fine", fine);
		model.addAttribute("fineAge", fineAge);

		return "fine";

	}

	@PostMapping("/pay")
	public String pay(Model model, @RequestParam String id) {

		// Let's assume every payment is a good payment
		int fineId = Integer.parseInt(id);
		List<String> fine = TvlRepository.getById(fineId);
		fine.set(8, "PAID");

		return "redirect:fines";
	}

}
