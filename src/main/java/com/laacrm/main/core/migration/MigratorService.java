package com.laacrm.main.core.migration;

import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.ScheduledTasks;
import com.laacrm.main.framework.AuthThreadLocal;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Component
public class MigratorService{

    private final Document migrationEntriesDoc;

    private final DaoHelper<ScheduledTasks, Long> scheduledTasksDao;

    public MigratorService(DaoHelper<ScheduledTasks, Long> scheduledTasksDao) throws Exception {
        this.scheduledTasksDao = scheduledTasksDao;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File initialCoreDataXml = new File("src/main/java/com/laacrm/main/core/migration/DataMigrationEntries.xml");
        migrationEntriesDoc = builder.parse(initialCoreDataXml);
        migrationEntriesDoc.getDocumentElement().normalize();
    }

    public void migrate() {
        try{
            NodeList migrationEntries = migrationEntriesDoc.getElementsByTagName("MigrationEntry");
            for (int i = 0; i < migrationEntries.getLength(); i++) {
                Node migrationEntry = migrationEntries.item(i);

                if (migrationEntry.getNodeType() == Node.ELEMENT_NODE) {
                    Element migrationElement = (Element) migrationEntry;

                    String className = migrationElement.getAttribute("class-name");
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    java.lang.reflect.Method runMethod = clazz.getMethod("run");
                    runMethod.invoke(instance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isTaskScheduled(String taskName) {
        return !scheduledTasksDao.findByUserCriteria(ScheduledTasks.class, context -> {
            CriteriaBuilder cb = context.getCriteriaBuilder();
            Root<ScheduledTasks> root = context.getRoot();
            context.addPredicate(cb.equal(root.get("taskName"), taskName));
        }).isEmpty();
    }

}
