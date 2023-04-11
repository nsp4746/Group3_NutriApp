package com.group3.nutriapp.persistence;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Nikhil Patil + Group 3
 * @version 1.0
 * @description This class is used to read and write XML files.
 * @dateCreated 4/10/2023
 */
public class XMLAdapter {

    private File file;

    private final String FILEPATH = "..\\Group3_NutriApp\\data\\food.xml";

    public XMLAdapter() {
        this.file = new File(FILEPATH);
    }

    /**
     * This method is used to parse an XML file.
     * {@link} <br></br>
     * {@link} https://www.javatpoint.com/how-to-read-xml-file-in-java
     * @return void. will probably change however to fit our needs
     */
    public void parseXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("food");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                // System.out.println(node.getTextContent()); 
                // One way to print the text from an XML file

                // Another way to print the text from an XML file, this way is far more organized
                System.out.println("\nNode Name: " + node.getNodeName());
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("Name: " + element.getElementsByTagName("name").item(0).getTextContent());
                    System.out.println("Calories: " + element.getElementsByTagName("calories").item(0).getTextContent());
                //  System.out.println("Fat: " + element.getElementsByTagName("fat").item(0).getTextContent());
                    System.out.println("Carbs: " + element.getElementsByTagName("carbs").item(0).getTextContent());
                    System.out.println("Protein: " + element.getElementsByTagName("protein").item(0).getTextContent());
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        XMLAdapter adapter = new XMLAdapter();
        adapter.parseXML();
    }
}
