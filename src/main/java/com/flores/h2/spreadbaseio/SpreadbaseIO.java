package com.flores.h2.spreadbaseio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flores.h2.spreadbase.Spreadbase;
import com.flores.h2.spreadbase.util.SpreadbaseUtil;
import com.flores.h2.spreadbaseio.model.impl.Version;

@RestController
public class SpreadbaseIO {
	private static final Logger logger = LoggerFactory
			.getLogger(SpreadbaseIO.class);

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public HttpEntity<InputStreamResource> spreadful(@RequestParam("file") MultipartFile inputFile) {
		logger.debug("upload received: {}", inputFile.getOriginalFilename());
		
		try {
			//create file
			File tmp = new File(System.getProperty("java.io.tmpdir"), inputFile.getOriginalFilename());
			try(OutputStream w = new FileOutputStream(tmp);
					InputStream r = new DataInputStream(inputFile.getInputStream())) {
				int size;
				byte[] buffer = new byte[2048];
				while((size = r.read(buffer, 0, buffer.length)) > 0)
					w.write(buffer, 0, size);
			} catch (Exception e) {
				logger.error("saving filestream: {}", e.getMessage());
				throw e;
			} finally { tmp.deleteOnExit(); }
			
			//convert file
			try { Spreadbase.asDataSource(tmp); }
			catch (Exception e) { 
				logger.error("creating datasource: {}", e.getMessage());
				throw e;
			}
			
			//return file
			try { return getResponseFile(SpreadbaseUtil.fileAsH2File(tmp)); }
			catch(Exception e) { 
				logger.error("generating response file: {}", e.getMessage());
				throw e;
			}
		} catch(Exception e) {
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/info")
	public HttpEntity<Version> versionInfo() {
		logger.debug("version information request...");
		return new ResponseEntity<Version>(
				new Version("spreadbase", "1.0.0"),
				HttpStatus.OK);
	}
	
	private static ResponseEntity<InputStreamResource> getResponseFile(File f) throws IOException {
		logger.debug("creating response...");

		//build the response header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + f.getName());
		headers.add("Content-Type", "application/octet-stream");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		UrlResource outFile = new UrlResource(f.toURI());
		
		headers.setContentLength(outFile.contentLength());
		return new ResponseEntity<InputStreamResource>(
				new InputStreamResource(outFile.getInputStream()),
				headers, HttpStatus.OK);
	}
	
}