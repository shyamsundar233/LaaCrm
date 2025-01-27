package com.laacrm.main.core.service;

import com.laacrm.main.core.entity.Field;
import com.laacrm.main.core.entity.FieldProperties;
import com.laacrm.main.core.entity.FieldPropertiesRef;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.repo.FieldPropertiesRefRepo;
import com.laacrm.main.framework.AuthThreadLocal;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
@AllArgsConstructor
public class InitPopulateService {

    private static final Logger LOGGER = Logger.getLogger(InitPopulateService.class.getName());

    private final ModuleService moduleService;
    private final FieldPropertiesRefRepo propertyRefRepo;

    public void populate() {
        LOGGER.log(Level.INFO, "Populating default data to CRM :: {0}", AuthThreadLocal.getCurrentTenant().getTenantId());
        populateDefaultModules();
    }

    private Document loadXmlDocument(String filePath) {
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

    private void populateDefaultModules() {
        try{
            Document xmlDoc = loadXmlDocument("src/main/java/com/laacrm/main/core/xml/PopulateInitialData.xml");
            NodeList defaultModules = xmlDoc.getElementsByTagName("Module");
            for (int modIndex = 0; modIndex < defaultModules.getLength(); modIndex++) {
                Element modElement = (Element) defaultModules.item(modIndex);
                Module module = new Module(
                        modElement.getAttribute("module-name"),
                        modElement.getAttribute("singular-name"),
                        modElement.getAttribute("plural-name"),
                        Integer.valueOf(modElement.getAttribute("type")),
                        Integer.valueOf(modElement.getAttribute("status")),
                        null
                );
                NodeList modFields = modElement.getElementsByTagName("Field");
                List<Field> fieldList = new ArrayList<>();
                for (int fldIndex = 0; fldIndex < modFields.getLength(); fldIndex++) {
                    Element fldElement = (Element) modFields.item(fldIndex);
                    Field fld = new Field(
                            fldElement.getAttribute("field-name"),
                            Integer.valueOf(fldElement.getAttribute("field-type")),
                            null
                    );
                    fld.setModule(module);
                    NodeList fldProps = fldElement.getElementsByTagName("FieldProperties");
                    List<FieldProperties> fldPropsList = new ArrayList<>();
                    for (int propIndex = 0; propIndex < fldProps.getLength(); propIndex++) {
                        Element propElement = (Element) fldProps.item(propIndex);
                        FieldPropertiesRef propRef = new FieldPropertiesRef(propElement.getAttribute("property"));
                        FieldProperties prop = new FieldProperties(
                                fld,
                                propRef,
                                propElement.getAttribute("property-value")
                        );
                        fldPropsList.add(prop);
                    }
                    fld.setFieldProperties(fldPropsList);
                    fieldList.add(fld);
                }
                module.setFields(fieldList);
                moduleService.save(module);
                LOGGER.log(Level.INFO, "{0} Default Module populated", module.getModuleName());
            }
        }catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while populating default modules :: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
