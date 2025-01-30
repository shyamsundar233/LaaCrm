package com.laacrm.main.core.service;

import com.laacrm.main.core.FeatureLimits;
import com.laacrm.main.core.xml.XmlUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
@Transactional
@AllArgsConstructor
public class RecordService {

    @PersistenceContext
    private EntityManager entityManager;

    private final FeatureLimits featureLimits;

    @Transactional
    public void createRecordTables() {
        Document initData = XmlUtils.loadXmlDocument("src/main/java/com/laacrm/main/core/xml/PopulateInitialData.xml");
        NodeList defaultModules = initData.getElementsByTagName("Module");
        for(int index = 0; index < defaultModules.getLength(); index++) {
            Element module = (Element) defaultModules.item(index);
            createRecordTableForModule(module.getAttribute("module-name"));
        }
        for (int index = 0 ; index < 200; index++) {
            createRecordTableForModule("CustomModule" + index);
        }
    }

    private void createRecordTableForModule(String moduleName) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ")
                .append(moduleName + "_rec")
                .append(" (")
                .append("record_id BIGINT PRIMARY KEY, ");
        for (int index = 0 ; index < 150; index++) {
            if(index == 149){
                sql.append("field_").append(index).append(" TEXT");
            }else {
                sql.append("field_").append(index).append(" TEXT, ");
            }
        }
        sql.append(")");
        entityManager.createNativeQuery(sql.toString()).executeUpdate();
    }

}
