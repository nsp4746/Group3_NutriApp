package com.group3.nutriapp.persistence;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * @author Nikhil Patil + Group 3
 * @version 1.0
 * @description This class is used to read and write XML files.
 * @dateCreated 4/10/2023
 */
public class XMLAdapter {

    private File file;

    private final String FILEPATH = " ";

    public XMLAdapter() {
        this.file = new File(FILEPATH);
    }

    public void parseXML(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
