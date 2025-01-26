package com.laacrm.main.core.service;

import com.laacrm.main.core.entity.FieldPropertiesRef;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Service
@Transactional
@AllArgsConstructor
public class TemplateDataPopulate {
    
    private final FieldService fieldService;

    public Document getXmlDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File initialCoreDataXml = new File("src/main/java/com/laacrm/main/core/xml/TemplateData.xml");
        Document dataXmlDocument = builder.parse(initialCoreDataXml);
        dataXmlDocument.getDocumentElement().normalize();
        return dataXmlDocument;
    }

    public void populateFieldPropertiesRef() throws Exception {
        try {
            if(!fieldService.isFieldPropertiesRefPopulated()){
                Document dataXmlDocument = getXmlDocument();
                NodeList fieldPropsList = dataXmlDocument.getElementsByTagName("DefaultFieldProp");

                for (int i = 0; i < fieldPropsList.getLength(); i++) {
                    Node fieldNode = fieldPropsList.item(i);

                    if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element fieldElement = (Element) fieldNode;

                        String propertyName = fieldElement.getAttribute("property-name");
                        String displayName = fieldElement.getAttribute("display-name");
                        Integer fieldType = Integer.valueOf(fieldElement.getAttribute("field-type"));
                        Boolean mandatory = Boolean.valueOf(fieldElement.getAttribute("mandatory"));

                        FieldPropertiesRef fieldPropertiesRef = new FieldPropertiesRef();
                        fieldPropertiesRef.setPropertyName(propertyName);
                        fieldPropertiesRef.setDisplayName(displayName);
                        fieldPropertiesRef.setFieldType(fieldType);
                        fieldPropertiesRef.setMandatory(mandatory);
                        fieldService.saveFieldPropertiesRef(fieldPropertiesRef);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
