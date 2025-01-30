package com.laacrm.main.core.xml;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlUtils {

    public static Document loadXmlDocument(String filePath) {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File initialCoreDataXml = new File(filePath);
            Document xmlDoc = builder.parse(initialCoreDataXml);
            xmlDoc.getDocumentElement().normalize();
            return xmlDoc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
