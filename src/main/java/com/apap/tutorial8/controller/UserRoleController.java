package com.apap.tutorial8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		userService.addUser(user);
		return "home";
	}
	
	@RequestMapping(value = "/changePassword")
	private String changePassword(Model model) {
		model.addAttribute("text","");
		return "changePassword";
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	private String changePasswordSubmit(@RequestParam(value = "username") String username,
			@RequestParam(value = "nPassword") String nPassword,
			@RequestParam(value = "oPassword") String oPassword,
			@RequestParam(value = "confirmPass") String confirmPass, Model model) {
		
		if(!nPassword.equals(confirmPass)) {
			model.addAttribute("message","Konfirmasi password baru salah");
			return "changePassword";
		}
		
		UserRoleModel user = userService.getUserByUsername(username);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (passwordEncoder.matches(oPassword, user.getPassword())){
			if( nPassword.length()<8 || !nPassword.matches(".*[a-zA-Z]+.*") || !nPassword.matches(".*\\d+.*")){
				model.addAttribute("message","Password harus minimum terdiri atas 8 karakter, mengandung angka dan huruf");
				return "changePassword";
			}
			userService.changeUserPassword(user, nPassword);
			model.addAttribute("text","Password berhasil diubah!");
		}
		else {
			model.addAttribute("text","Password yang dimasukkan beda dengan password lama");
		}
		return "changePassword";
	}
}
