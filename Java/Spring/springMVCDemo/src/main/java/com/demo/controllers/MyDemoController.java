package com.demo.controllers;

import java.util.Random;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.model.Account;

@Controller
public class MyDemoController {
	private String[] quotes = {"To be or not to be - Shakesphere",
			"Spring is Natures way of saying Lets Party",
			"The time is always right to do what is right" };
				
	// http://localhost:8080/springMVCDemo/getQuote.html
	@RequestMapping(value="/getQuote")
	public String getRandomQuote(Model model){
		int rand=new Random().nextInt(quotes.length);
		String randomQuote= quotes[rand];
		model.addAttribute("randomQuote",randomQuote);
		return "quote";
	}
	
	@RequestMapping(value="/createAccount")
	public String createAccount( @ModelAttribute ("aNewAccount") Account account ){
	System.out.println(account.getFirstName() + " " + account.getLastName() + " " + account.getAge()  + " " + account.getAddress() + " " + account.getEmail() );
	return "createAccount";
	}
	
	@RequestMapping(value="/acountCreated" , method=RequestMethod.POST)
	public String performCreate(@Valid @ModelAttribute ("aNewAccount") Account account, BindingResult result) {
		if(result.hasErrors()){
			System.out.println("Form has errors");
			return "createAccount";
			} 
	System.out.println("Form validated");
	System.out.println(account.getFirstName() + " " + account.getLastName() + " " + account.getAge()  + " " + account.getAddress() + " " + account.getEmail() );
	return "acountCreated";
	}

	} 

