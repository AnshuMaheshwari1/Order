package com.anshu.order;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderParser {
	
//	private static String basepath = "E:\\eclipse-workspace\\order\\resources\\";
//	private static String csvSplitBy = ",";
//	private static int numOfCols = 4; 
//	private static String outPutFileName = "output.json";
	
	@RequestMapping("/{files}")
	public String parser(@PathVariable("files") String files) {
		Properties prop = null;
		try (InputStream input = new FileInputStream("E:\\eclipse-workspace\\order-spring\\src\\main\\resources\\application.properties")) {
			prop = new Properties();
			
			prop.load(input);
		} catch (IOException e) {
			
		}
		File outputFile = new File(prop.getProperty("filepath") + prop.getProperty("output-filename"));
		outputFile.delete();
		// String fileName = args[0];
		List<RunnableTask> tasks = new ArrayList<>();
		List<Thread> threads = new ArrayList<>();
		String[] input = files.split(",");
		for (String file : input) {
//			String[] input = file.split("\\.");
//			String fileType = input[1];
			RunnableTask task = new RunnableTask(file, prop);
			Thread t = new Thread(task);
			t.start();
			tasks.add(task);
			threads.add(t);
			
			for (Thread t1 : threads) {
				try {
					t1.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			if ("csv".equals(fileType)) {
//				saveObjectsInJson(csvParser(file));
//			} else if ("json".equals(fileType)) {
//				saveObjectsInJson(jsonFileToObjects(file));
//			}
		}
		return "hello";
	
	}

	public static void main(String[] args) {

		// String fileName = args[0];
		List<RunnableTask> tasks = new ArrayList<>();
		List<Thread> threads = new ArrayList<>();
		Properties prop = null;
		try (InputStream input = new FileInputStream("E:\\eclipse-workspace\\order-spring\\src\\main\\resources\\application.properties")) {
			prop = new Properties();
			
			prop.load(input);
		} catch (IOException e) {
			
		}
		for (String file : args) {
//			String[] input = file.split("\\.");
//			String fileType = input[1];
			RunnableTask task = new RunnableTask(file, prop);
			Thread t = new Thread(task);
			t.start();
			tasks.add(task);
			threads.add(t);
			
			for (Thread t1 : threads) {
				try {
					t1.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			if ("csv".equals(fileType)) {
//				saveObjectsInJson(csvParser(file));
//			} else if ("json".equals(fileType)) {
//				saveObjectsInJson(jsonFileToObjects(file));
//			}
		}
	}

//	private static List<Order> csvParser(String fileName) {
//		int lineNumber = -1;
//		BufferedReader br;
//		List<Order> orderList = new ArrayList<>();
//		try {
//			br = new BufferedReader(new FileReader(basepath + fileName));
//
//			String line = "";
//			
//			while ((line = br.readLine()) != null) {
//
//				String[] cols = line.split(csvSplitBy);
//				// order.list
//				lineNumber++;
//				if (cols.length == numOfCols && lineNumber != 0) {
//					Order order = new Order();
//					order.setOrderId(cols[0]);
//					order.setAmount(Float.parseFloat(cols[1]));
//					order.setCurrency(cols[2]);
//					order.setComment(cols[3]);
//					order.setFileName(fileName);
//					order.setLineNumber(lineNumber);
//					orderList.add(order);
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return orderList;
//	}
	
//	private static void saveObjectsInJson(List<Order> orderList) {
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			for (Order order : orderList) {
//				mapper.writeValue(new FileWriter(basepath + outPutFileName, true), order);
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}
//	
//	private static List<Order> jsonFileToObjects(String fileName) {
//		ObjectMapper mapper = new ObjectMapper();
//		List<Order> orderList = new ArrayList<Order>();
//		try {
//			orderList = mapper.readValue(new File(basepath + fileName), new TypeReference<List<Order>>() {});
//			for (int i = 0; i < orderList.size(); i++) {
//				orderList.get(i).setLineNumber(i+1);
//				orderList.get(i).setFileName(fileName);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return orderList;
//	}


}

