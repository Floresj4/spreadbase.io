package com.flores.h2.spreadbaseio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.h2.tools.RunScript;
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

import com.flores.h2.spreadbase.Spreadbase;
import com.flores.h2.spreadbase.io.TableDefinitionWriter;
import com.flores.h2.spreadbase.model.ITable;
import com.flores.h2.spreadbase.model.impl.h2.DataDefinitionBuilder;
import com.flores.h2.spreadbase.util.BuilderUtil;
import com.flores.h2.spreadbaseio.model.impl.Version;

@RestController
public class SpreadbaseIO {

	private static final String STR_CONNECTION = "%s/test;MV_STORE=FALSE;FILE_LOCK=NO";
	
	private static final Logger logger = LoggerFactory
			.getLogger(SpreadbaseIO.class);

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public HttpEntity<String> spreadful(@RequestParam("file") MultipartFile inputFile) {

		//create file for spreadbase
		File tmp = new File(System.getProperty("java.io.tmpdir"), inputFile.getOriginalFilename());
		try(OutputStream w = new FileOutputStream(tmp);
				InputStream r = new DataInputStream(inputFile.getInputStream())) {

			int size;
			byte[] buffer = new byte[2048];
			while((size = r.read(buffer, 0, buffer.length)) > 0)
				w.write(buffer, 0, size);

			//start spreadbase analysis
			List<ITable>tables = null;
			try { tables = Spreadbase.analyze(tmp); }
			catch(Exception e) { 
				logger.error("analyzing {}...", tmp.getName());
				throw new IOException(e);
			}
			
			File sqlFile = null;

			//create .sql script
			try(TableDefinitionWriter writer = new TableDefinitionWriter(
					(sqlFile = BuilderUtil.fileAsSqlFile(tmp)), new DataDefinitionBuilder())) {
				logger.debug("writing sql script...");
				writer.write(tables);
			}
			
			//run sql script
			try {
				Class.forName("org.h2.Driver");
				Connection conn = DriverManager.getConnection(
						"jdbc:h2:" + String.format(STR_CONNECTION, BuilderUtil.fileAsH2File(tmp))
						, "sa", "");
				
				RunScript.execute(conn, new InputStreamReader(new FileInputStream(sqlFile)));
			} catch (ClassNotFoundException | SQLException e) {
				logger.error("creating datasource...");
				throw new IOException(e);
			}
		}
		catch (IOException e) {
			return new ResponseEntity<String>("failure...", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			tmp.deleteOnExit();
		}

		return new ResponseEntity<String>("success...", HttpStatus.OK);
	}
	
	@GetMapping("/info")
	public HttpEntity<Version> versionInfo() {
		return new ResponseEntity<Version>(
				new Version("spreadbase", "1.0.0"),
				HttpStatus.OK);
	}
}