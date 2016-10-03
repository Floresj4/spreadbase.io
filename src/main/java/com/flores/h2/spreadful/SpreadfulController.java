package com.flores.h2.spreadful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SpreadfulController {

	private static final Logger logger = LoggerFactory.getLogger(SpreadfulController.class);
	
	@GetMapping("/spreadful")
	public String init(Model model) {
		model.addAttribute("worksheet", new Worksheet());
		return "fileupload";
	}

	@PostMapping("/spreadful/load")
	public String load(@ModelAttribute Worksheet worksheet) {
		logger.debug("loading {}", worksheet.getContent());
		return "result";
	}
}