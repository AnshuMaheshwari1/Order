package com.anshu.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import com.anshu.model.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallableTask implements Callable<List<Order>> {
	
	private String basepath;
	private String csvSplitBy;
	private int numOfCols = 4; 
	private String outPutFileName;
	
	private String file;
	
	
	public CallableTask(String file, Properties prop) {
		this.file = file;
		this.basepath = prop.getProperty("filepath");
		this.csvSplitBy = prop.getProperty("csv-split-by");
		this.outPutFileName = prop.getProperty("output-filename");
	}

	@Override
	public List<Order> call() {
		List<Order> orders = new ArrayList<>();
		String[] input = file.split("\\.");
		String fileType = input[1];
		if ("csv".equals(fileType)) {
			orders.addAll(csvParser(file));
		} else if ("json".equals(fileType)) {
			orders.addAll(jsonFileToObjects(file));
		}
		return orders;
	}
	
	private List<Order> csvParser(String fileName) {
		int lineNumber = -1;
		BufferedReader br;
		List<Order> orderList = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(basepath + fileName));

			String line = "";
			
			while ((line = br.readLine()) != null) {

				String[] cols = line.split(csvSplitBy);
				// order.list
				lineNumber++;
				if (cols.length == numOfCols && lineNumber != 0) {
					Order order = new Order();
					order.setOrderId(cols[0]);
					order.setAmount(Float.parseFloat(cols[1]));
					order.setCurrency(cols[2]);
					order.setComment(cols[3]);
					order.setFileName(fileName);
					order.setLineNumber(lineNumber);
					orderList.add(order);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderList;
	}
	
	private void saveObjectsInJson(List<Order> orderList) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			for (Order order : orderList) {
				mapper.writeValue(new FileWriter(basepath + outPutFileName, true), order);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private List<Order> jsonFileToObjects(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		List<Order> orderList = new ArrayList<Order>();
		try {
			orderList = mapper.readValue(new File(basepath + fileName), new TypeReference<List<Order>>() {});
			for (int i = 0; i < orderList.size(); i++) {
				orderList.get(i).setLineNumber(i+1);
				orderList.get(i).setFileName(fileName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderList;
	}

}
