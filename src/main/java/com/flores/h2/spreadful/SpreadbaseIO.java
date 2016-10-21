package com.flores.h2.spreadful;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flores.h2.spreadful.model.impl.Version;

@RestController
public class SpreadbaseIO {

	private static final Logger logger = LoggerFactory
			.getLogger(SpreadbaseIO.class);

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public HttpEntity<String> spreadful(@RequestParam("file") MultipartFile inputFile) {
		
		//create the temporary file
		File tmp = new File(System.getProperty("java.io.tmpdir"), inputFile.getOriginalFilename());
		try(OutputStream w = new FileOutputStream(tmp);
				InputStream r = new DataInputStream(inputFile.getInputStream())) {
			
			byte[] buffer = new byte[2048];
			while(r.read(buffer, 0, buffer.length) > 0) {
				w.write(buffer);
			}

		} catch (IOException e) {
			return new ResponseEntity<String>("failure...", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			tmp.deleteOnExit();
		}
		
		return new ResponseEntity<String>("success...", HttpStatus.OK);
	}
	
	@GetMapping("/info")
	public HttpEntity<Version> versionInfo() {
		logger.debug("endpoint...");
		
		Version v = new Version();
		v.setName("spreadbase");
		v.setVersion("1.0.0");
		
		return new ResponseEntity<Version>(v, HttpStatus.OK);
	}
}