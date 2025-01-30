package com.laacrm.main.core.service;

import com.laacrm.main.core.entity.FieldPropertiesRef;
import com.laacrm.main.core.xml.XmlUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
@AllArgsConstructor
public class TemplateDataPopulate {

    private static final Logger LOGGER = Logger.getLogger(TemplateDataPopulate.class.getName());
    
    private final FieldService fieldService;
    private final RecordService recordService;

    public void populateFieldPropertiesRef() throws Exception {
        try {
            LOGGER.log(Level.INFO, "Populating FieldPropertiesRef....");
            if(!fieldService.isFieldPropertiesRefPopulated()){
                Document dataXmlDocument = XmlUtils.loadXmlDocument("src/main/java/com/laacrm/main/core/xml/TemplateData.xml");
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
            LOGGER.log(Level.SEVERE, "Exception occurred while populating Field Properties Ref", e);
            throw new Exception(e);
        }
    }

    public void populateRecordsTable() throws Exception {
        try {
            recordService.createRecordTables();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while populating Records Table", e);
            throw new Exception(e);
        }
    }

}
