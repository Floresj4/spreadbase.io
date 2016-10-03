package com.flores.h2.spreadful.greeting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GreetingController {

	@GetMapping("/spreadful/greeting")
	public String greetingForm(Model model) {
		model.addAttribute("greeting", new Greeting());
		return "greeting";
	}
	
	@PostMapping("/spreadful/greeting")
	public String greetingSubmit(@ModelAttribute Greeting greeting) {
		return "result";
	}
}
