package com.anshu.writer;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anshu.exception.OrderException;
import com.anshu.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/writer")
public class Writer {
	
	private Properties prop = null;
	
	Logger logger = LoggerFactory.getLogger(CallableTask.class);

	@RequestMapping(method = RequestMethod.POST, value = "/writetofile")
	public void parser(@RequestBody List<Order> orders) {
		logger.info("parsing started");
		try (InputStream input = new FileInputStream(
				"E:\\eclipse-workspace\\writer\\src\\main\\resources\\application.properties")) {
			prop = new Properties();

			prop.load(input);
		} catch (IOException e) {

			new OrderException("error occured while reading prop file", e);
			
		}
		saveObjectsInJson(orders);
	}
	
	private void saveObjectsInJson(List<Order> orderList) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			for (Order order : orderList) {
				mapper.writeValue(new FileWriter(prop.getProperty("filepath") + prop.getProperty("output-filename"), true), order);
			}
			
		} catch (Exception e) {
			new OrderException("error occured while saving file", e);
		}		
	}

}
