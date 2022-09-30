package com.anshu.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.border.AbstractBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.anshu.model.Order;


@RestController
@RequestMapping("/reader")
public class Reader {
	
	@Autowired
	@Qualifier("rest")
	RestTemplate restTemplate;

	@RequestMapping("/{files}")
	public ResponseEntity<?> parser(@PathVariable("files") String files) {
		Properties prop = null;
		try (InputStream input = new FileInputStream(
				"E:\\eclipse-workspace\\reader\\src\\main\\resources\\application.properties")) {
			prop = new Properties();

			prop.load(input);
		} catch (IOException e) {

		}
		//File outputFile = new File(prop.getProperty("filepath") + prop.getProperty("output-filename"));
		//outputFile.delete();
		List<Callable<List<Order>>> tasks = new ArrayList<>();
		String[] input = files.split(prop.getProperty("csv-split-by"));

		for (String file : input) {
			tasks.add(new CallableTask(file, prop));
		}
		List<Order> orderList = new ArrayList<>();
		ExecutorService exec = Executors.newFixedThreadPool(2);
		try {
			List<Future<List<Order>>> results = exec.invokeAll(tasks);
			exec.shutdown();

			for (Future<List<Order>> orders : results) {
				orderList.addAll(orders.get());
			}
		} catch (Exception e) {

		}
		

		restTemplate.postForObject("http://localhost:8060/writer/writetofile", orderList, ResponseEntity.class);
		//restTemplate.getForObject("http://writer/" + orderList, Order[].class);
		return ResponseEntity.ok(orderList);
	}
}
