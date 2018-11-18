package com.javasampleapproach.multipartfile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.javasampleapproach.multipartfile.filestorage.FileStorage;

@Controller
public class UploadFileController {
	
	@Autowired
	FileStorage fileStorage;
	
    @GetMapping("/")
    public String index() {
        return "multipartfile/uploadform.html";
    }
    
    @PostMapping("/")
    public String uploadMultipartFile(@RequestParam("uploadfile") MultipartFile file, Model model) {
		try {
			fileStorage.store(file);
			model.addAttribute("message", "¡Trabajo académico subido exitosamente! -> archivo = " + file.getOriginalFilename());
		} catch (Exception e) {
			model.addAttribute("message", "!Error! -> al subir el archivo: " + file.getOriginalFilename());
		}
        return "multipartfile/uploadform.html";
    }
    
    
}
