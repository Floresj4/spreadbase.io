package com.flores.h2.spreadbaseio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flores.h2.spreadbase.Spreadbase;
import com.flores.h2.spreadbase.util.BuilderUtil;
import com.flores.h2.spreadbaseio.model.impl.Version;

@RestController
public class SpreadbaseIO {
	private static final Logger logger = LoggerFactory
			.getLogger(SpreadbaseIO.class);

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public HttpEntity<Object> spreadful(@RequestParam("file") MultipartFile inputFile) {
		logger.debug("upload received: {}", inputFile.getOriginalFilename());
		
		//create file for spreadbase
		File tmp = new File(System.getProperty("java.io.tmpdir"), inputFile.getOriginalFilename());
		try(OutputStream w = new FileOutputStream(tmp);
				InputStream r = new DataInputStream(inputFile.getInputStream())) {

			int size;
			byte[] buffer = new byte[2048];
			while((size = r.read(buffer, 0, buffer.length)) > 0)
				w.write(buffer, 0, size);

			try { Spreadbase.asDataSource(tmp); }
			catch (Exception e) {
				logger.error("spreadbase invocation exception: {}", e.getMessage());
				throw e;
			}
			
			return getResponseFile(BuilderUtil.fileAsH2File(tmp));
		}
		catch (Exception e) {
			return new ResponseEntity<Object>("failure...", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			tmp.deleteOnExit();
		}
	}
	
	@GetMapping("/info")
	public HttpEntity<Version> versionInfo() {
		return new ResponseEntity<Version>(
				new Version("spreadbase", "1.0.0"),
				HttpStatus.OK);
	}
	
	private static ResponseEntity<Object> getResponseFile(File f) throws FileNotFoundException {

		//build the response header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Disposition", "filename=" + f.getName());
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		headers.setContentLength(f.getTotalSpace());  
		return new ResponseEntity<Object>(
				new InputStreamResource(new FileInputStream(f)),
				headers, HttpStatus.OK);
	}
	
}