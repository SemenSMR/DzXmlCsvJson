package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import org.w3c.dom.Document;


public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV( columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "new_data.json");
        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list);
        writeString(json2,"data.json");


    }
    private static List<Employee> parseXML(String fileName)  {
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(Node.ELEMENT_NODE == node.getNodeType()){
                    Element element = (Element) node;
                    long id = Long.parseLong(element.getAttribute("id"));
                    String firstName = element.getAttribute("firsName");
                    String lastName = element.getAttribute("lastName");
                    String country = element.getAttribute("country");
                    int age = Integer.parseInt(element.getAttribute("age"));
                    Employee employee = new Employee(id,firstName,lastName,country,age);
                    employees.add(employee);
                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        } return employees;

    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }
    public static void writeString(String json,String fileName){
try (FileWriter file = new FileWriter(fileName)){
        file.write(json);
         file.flush();
} catch (IOException e){
    e.printStackTrace();
}
    }

    public static List <Employee> parseCSV (String[] columnMapping, String fileName){
        List<Employee> employees = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            var csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            employees = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        } return employees;
    }
}