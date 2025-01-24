package com.laacrm.main.core.migration.config;

import com.laacrm.main.core.MigrationConstants;
import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.MigrationEntry;
import com.laacrm.main.core.entity.ScheduledTasks;
import com.laacrm.main.core.service.MigrationService;
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
    private final MigrationService migrationService;
    private final DaoHelper<ScheduledTasks, Long> scheduledTasksDao;

    public MigratorService(DaoHelper<ScheduledTasks, Long> scheduledTasksDao, MigrationService migrationService) throws Exception {
        this.scheduledTasksDao = scheduledTasksDao;
        this.migrationService = migrationService;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File initialCoreDataXml = new File("src/main/java/com/laacrm/main/core/migration/config/DataMigrationEntries.xml");
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
                    boolean isError = false;
                    MigrationEntry entry = null;
                    try{
                        String className = migrationElement.getAttribute("class-name");
                        entry = migrationService.getMigrationEntryByName(className);
                        if(entry == null || entry.getMigrationStatus().equals(MigrationConstants.MigrationStatus.FAILED.getValue())){
                            entry = new MigrationEntry(className, MigrationConstants.MigrationStatus.ACTIVE.getValue());
                            migrationService.save(entry);
                            Class<?> clazz = Class.forName(className);
                            Object instance = clazz.getDeclaredConstructor().newInstance();
                            java.lang.reflect.Method runMethod = clazz.getMethod("run");
                            runMethod.invoke(instance);
                        }
                    }catch (Exception e){
                        isError = true;
                        e.printStackTrace();
                    }finally {
                        if(isError){
                            entry.setMigrationStatus(MigrationConstants.MigrationStatus.FAILED.getValue());
                        }
                        entry.setFailureCount(entry.getFailureCount() + 1);
                        migrationService.save(entry);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTask(ScheduledTasks task) {
        scheduledTasksDao.save(task);
    }

    public boolean isTaskScheduled(String taskName) {
        return !scheduledTasksDao.findByCriteria(ScheduledTasks.class, context -> {
            CriteriaBuilder cb = context.getCriteriaBuilder();
            Root<ScheduledTasks> root = context.getRoot();
            context.addPredicate(cb.equal(root.get("taskName"), taskName));
        }).isEmpty();
    }

}
