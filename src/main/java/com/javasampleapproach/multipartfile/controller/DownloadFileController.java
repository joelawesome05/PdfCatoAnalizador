package com.javasampleapproach.multipartfile.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.javasampleapproach.multipartfile.filestorage.FileStorage;
import com.javasampleapproach.multipartfile.model.FileInfo;
import java.nio.file.Files;
@Controller
public class DownloadFileController {

	@Autowired
	FileStorage fileStorage;
	
	/*
	 * Retrieve Files' Information
	 */
	@GetMapping("/files")
	public String getListFiles(Model model) {
		List<FileInfo> fileInfos = fileStorage.loadFiles().map(
					path ->	{
						String filename = path.getFileName().toString();
						String url = MvcUriComponentsBuilder.fromMethodName(DownloadFileController.class,
		                        "downloadFile", path.getFileName().toString()).build().toString();
						return new FileInfo(filename, url); 
					} 
				)
				.collect(Collectors.toList());
		
		model.addAttribute("files", fileInfos);
		return "multipartfile/listfiles";
	}
 
    /*
     * Download Files
     */
	@GetMapping("/files/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		Resource file = fileStorage.loadFile(filename);
		return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);	
	}

	@GetMapping(value="/filestorage/{filename}")
	public ResponseEntity<byte[]> showPdf(Model model,@PathVariable String filename) {
		String fileName = "filestorage/"+filename;
		byte[] contents = new byte[0];
		try {
			contents = Files.readAllBytes( new File(fileName).toPath() );
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	}
}