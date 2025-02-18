package com.laacrm.main.core.service;

import com.laacrm.main.core.TaskRecordConstants;
import com.laacrm.main.core.entity.*;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.xml.XmlUtils;
import com.laacrm.main.framework.AuthThreadLocal;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
    private final TaskRecordService taskRecordService;

    public boolean isInitialized() {
        TaskRecord taskRecord = taskRecordService.findAll("initPopulateTask").stream().findFirst().orElse(null);
        return taskRecord != null;
    }

    public void populate() {
        TaskRecord taskRecord = taskRecordService.findAll("initPopulateTask").stream().findFirst().orElse(null);
        if (taskRecord == null) {
            LOGGER.log(Level.INFO, "Populating default data to CRM :: {0}", AuthThreadLocal.getCurrentTenant().getTenantId());
            populateDefaultModules();
        }else {
            LOGGER.log(Level.INFO, "Default data to CRM already populated :: {0}", AuthThreadLocal.getCurrentTenant().getTenantId());
        }
    }

    private void populateDefaultModules() {
        try{
            Document xmlDoc = XmlUtils.loadXmlDocument("src/main/java/com/laacrm/main/core/xml/PopulateInitialData.xml");
            NodeList defaultModules = xmlDoc.getElementsByTagName("Module");
            for (int modIndex = 0; modIndex < defaultModules.getLength(); modIndex++) {
                Element modElement = (Element) defaultModules.item(modIndex);
                Module module = new Module(
                        modElement.getAttribute("module-name"),
                        modElement.getAttribute("singular-name"),
                        modElement.getAttribute("plural-name"),
                        Integer.valueOf(modElement.getAttribute("type")),
                        Integer.valueOf(modElement.getAttribute("status"))
                );
                NodeList layout = modElement.getElementsByTagName("Layout");
                List<Layout> modLayouts = new ArrayList<>();
                for (int layoutIndex = 0; layoutIndex < layout.getLength(); layoutIndex++) {
                    Element layoutElement = (Element) layout.item(layoutIndex);
                    Layout modLayout = new Layout(
                            layoutElement.getAttribute("layout-name"),
                            Boolean.parseBoolean(layoutElement.getAttribute("is-default"))
                    );
                    module.getLayouts().add(modLayout);
                    NodeList modFields = layoutElement.getElementsByTagName("Field");
                    List<Field> fieldList = new ArrayList<>();
                    for (int fldIndex = 0; fldIndex < modFields.getLength(); fldIndex++) {
                        Element fldElement = (Element) modFields.item(fldIndex);
                        Field fld = new Field(
                                fldElement.getAttribute("field-name"),
                                Integer.valueOf(fldElement.getAttribute("field-type")),
                                null
                        );
                        fld.setModule(module);
                        fld.setLayout(modLayout);
                        fld.setIsVisible(Boolean.parseBoolean(fldElement.getAttribute("is-visible")));
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
                    modLayout.setFields(fieldList);
                    modLayouts.add(modLayout);
                }
                module.setLayouts(modLayouts);
                moduleService.save(module);
                LOGGER.log(Level.INFO, "{0} Default Module populated", module.getModuleName());
            }
            taskRecordService.addTaskRecord("initPopulateTask", "initPopulateTask", TaskRecordConstants.TaskStatus.SUCCESS);
        }catch (Exception e) {
            taskRecordService.addTaskRecord("initPopulateTask", "initPopulateTask", TaskRecordConstants.TaskStatus.FAILURE);
            LOGGER.log(Level.SEVERE, "Error while populating default modules :: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
