package com.laacrm.main.core.migration.initialDataPopulate;

import com.laacrm.main.core.entity.Field;
import com.laacrm.main.core.entity.FieldProperties;
import com.laacrm.main.core.entity.FieldPropertiesRef;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.service.FieldService;
import com.laacrm.main.core.service.ModuleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InitialDataPopulateUtil {

    private static ModuleService moduleService;
    private static FieldService fieldService;

    @Autowired
    public void setModuleService(ModuleService moduleService, FieldService fieldService) {
        InitialDataPopulateUtil.moduleService = moduleService;
        InitialDataPopulateUtil.fieldService = fieldService;
    }

    public static void populateInitialData() throws Exception {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File initialCoreDataXml = new File("src/main/java/com/laacrm/main/core/xml/PopulateInitialData.xml");
            Document dataXmlDocument = builder.parse(initialCoreDataXml);
            dataXmlDocument.getDocumentElement().normalize();
            NodeList defaultModulesList = dataXmlDocument.getElementsByTagName("Module");

            for (int i = 0; i < defaultModulesList.getLength(); i++) {
                Node modNode = defaultModulesList.item(i);

                if (modNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element modElement = (Element) modNode;

                    String moduleName = modElement.getAttribute("module-name");
                    String singularName = modElement.getAttribute("singular-name");
                    String pluralName = modElement.getAttribute("plural-name");
                    Integer type = Integer.valueOf(modElement.getAttribute("type"));
                    Integer status = Integer.valueOf(modElement.getAttribute("status"));
                    Module module = new Module(moduleName, singularName, pluralName, type, status, null);
                    addModFields(modElement, module);
                    moduleService.save(module);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static void addModFields(Element modElement, Module module) {
        NodeList modFields = modElement.getElementsByTagName("Field");
        for(int j = 0; j < modFields.getLength(); j++){
            Node fieldNode = modFields.item(j);
            if(fieldNode.getNodeType() == Node.ELEMENT_NODE){
                Element fieldElement = (Element) fieldNode;
                String fieldName = fieldElement.getAttribute("field-name");
                Integer fldType = Integer.valueOf(fieldElement.getAttribute("field-type"));
                Field field = new Field(fieldName, fldType, null);
                field.setModule(module);
                addFieldProps(fieldElement, field);
                module.getFields().add(field);
            }
        }
    }

    private static void addFieldProps(Element fieldElement, Field field) {
        NodeList defaultFieldProps = fieldElement.getElementsByTagName("FieldProperties");
        for (int i = 0; i < defaultFieldProps.getLength(); i++) {
            Node propNode = defaultFieldProps.item(i);
            if(propNode.getNodeType() == Node.ELEMENT_NODE){
                Element propElement = (Element) propNode;
                String propertyName = propElement.getAttribute("property");
                String propertyValue = propElement.getAttribute("property-value");
                FieldPropertiesRef propertiesRef = fieldService.getFieldPropertiesRefByPropertyName(propertyName);
                FieldProperties properties = new FieldProperties(field, propertiesRef, propertyValue);
                field.getFieldProperties().add(properties);
            }
        }
    }

}
