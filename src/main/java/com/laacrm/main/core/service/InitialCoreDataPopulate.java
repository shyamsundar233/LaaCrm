package com.laacrm.main.core.service;

import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.Field;
import com.laacrm.main.core.entity.FieldPropertiesRef;
import com.laacrm.main.core.entity.Module;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InitialCoreDataPopulate {

    private final DaoHelper<FieldPropertiesRef, Long> fieldPropRefDao;
    private final ModuleService moduleService;
    private final Document dataXmlDocument;

    @Autowired
    public InitialCoreDataPopulate(DaoHelper<FieldPropertiesRef, Long> fieldPropRefDao, ModuleService moduleService) throws Exception{
        this.fieldPropRefDao = fieldPropRefDao;
        this.moduleService = moduleService;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File initialCoreDataXml = new File("src/main/java/com/laacrm/main/core/xml/PopulateInitialData.xml");
        dataXmlDocument = builder.parse(initialCoreDataXml);
        dataXmlDocument.getDocumentElement().normalize();
    }

    public void populate() throws Exception {
        populateFieldPropertiesRef();
    }

    public void populateDefaultModules() {
        try{
            NodeList defaultModulesList = dataXmlDocument.getElementsByTagName("Module");
            NodeList defaultFields = dataXmlDocument.getElementsByTagName("Field");

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
                    List<Field> fields = new ArrayList<>();
                    for(int j = 0; j < defaultFields.getLength(); j++){
                        Node fieldNode = defaultFields.item(j);
                        if(fieldNode.getNodeType() == Node.ELEMENT_NODE){
                            Element fieldElement = (Element) fieldNode;
                            String fldModName = fieldElement.getAttribute("module");
                            if(fldModName.equals(moduleName)){
                                String fieldName = fieldElement.getAttribute("field-name");
                                Integer fldType = Integer.valueOf(fieldElement.getAttribute("field-type"));
                                Field field = new Field(fieldName, fldType);
                                field.setModule(module);
                                fields.add(field);
                            }
                        }
                    }
                    module.setFields(fields);
                    moduleService.save(module);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateFieldPropertiesRef(){
        try {

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
                    fieldPropRefDao.save(fieldPropertiesRef);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
