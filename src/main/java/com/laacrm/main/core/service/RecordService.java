package com.laacrm.main.core.service;

import com.laacrm.main.core.FeatureLimits;
import com.laacrm.main.core.controller.APIException;
import com.laacrm.main.core.entity.*;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.entity.Record;
import com.laacrm.main.core.repo.FieldPropertiesRefRepo;
import com.laacrm.main.core.xml.XmlUtils;
import com.laacrm.main.framework.service.tenant.TenantService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class RecordService {

    private static final Logger LOGGER = Logger.getLogger(RecordService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;
    private final FeatureLimits featureLimits;
    private final ModuleService moduleService;
    private final TenantService tenantService;
    private final FieldPropertiesRefRepo fieldPropertiesRefRepo;

    public void createRecordTables() {
        Document initData = XmlUtils.loadXmlDocument("src/main/java/com/laacrm/main/core/xml/PopulateInitialData.xml");
        NodeList defaultModules = initData.getElementsByTagName("Module");
        for(int index = 0; index < defaultModules.getLength(); index++) {
            Element module = (Element) defaultModules.item(index);
            createRecordTableForModule(module.getAttribute("module-name"));
        }
        for (int index = 0 ; index < featureLimits.getModuleLimit(); index++) {
            createRecordTableForModule("CustomModule" + index);
        }
    }

    private void createRecordTableForModule(String moduleName) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ")
                .append(moduleName).append("_rec")
                .append(" (")
                .append("record_id BIGINT PRIMARY KEY, ")
                .append("layout_id BIGINT, ")
                .append("createdAt BIGINT, ")
                .append("updatedAt BIGINT, ");
        for (int index = 0 ; index < featureLimits.getFieldLimit(); index++) {
            if(index == featureLimits.getFieldLimit() - 1){
                sql.append("field_").append(index).append(" TEXT");
            }else {
                sql.append("field_").append(index).append(" TEXT, ");
            }
        }
        sql.append(")");
        entityManager.createNativeQuery(sql.toString()).executeUpdate();
    }

    public Record saveRecord(Record record) {
        Module module = moduleService.findById(record.getModuleId());
        int operation = record.getRecordId() == null ? 1 : 2;
        if(module == null){
            throw new APIException(HttpStatus.NOT_FOUND.value(), "Module not found");
        }
        Layout layout = module.getLayouts().stream()
                .filter(elem -> elem.getLayoutId().equals(record.getLayoutId()))
                .findFirst().orElse(null);

        if (layout == null) {
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "Invalid layout ID");
        }

        Map<String, Object> recordDetails = record.getRecordDetails();
        List<Field> fields = layout.getFields().stream()
                .filter(f -> recordDetails.containsKey(f.getFieldName()))
                .toList();
        validateFields(recordDetails, layout.getFields());
        if(fields.isEmpty()){
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "Fields cannot be empty");
        }
        StringBuilder query = new StringBuilder();
        Long recordId;
        if(operation == 1){
            query.append("INSERT INTO ")
                    .append(getRecordTableNameForModule(module))
                    .append(" (record_id, layout_id, createdAt, updatedAt, ");

            List<String> fieldNames = new ArrayList<>();
            for (Field field : fields) {
                fieldNames.add(field.getRecColName().split("::")[1]);
            }
            query.append(String.join(", ", fieldNames)); // Dynamically set field names
            query.append(") VALUES (:record_id, :layout_id, :createdAt, :updatedAt, ");
            query.append(fieldNames.stream().map(f -> ":" + f).collect(Collectors.joining(", ")));
            query.append(")");

            recordId = tenantService.getCurrentAvailablePK();
            Query entQuery = entityManager.createNativeQuery(query.toString())
                    .setParameter("record_id", recordId)
                    .setParameter("layout_id", record.getLayoutId())
                    .setParameter("createdAt", record.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .setParameter("updatedAt", record.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            for (Field field : fields) {
                entQuery.setParameter(field.getRecColName().split("::")[1], recordDetails.get(field.getFieldName()));
            }
            entQuery.executeUpdate();
            record.setRecordId(recordId);
            LOGGER.log(Level.INFO, "Saved record :: {0}", record.toString());
        }else {
            query.append("UPDATE ")
                    .append(getRecordTableNameForModule(module))
                    .append(" SET ");
            for(int index = 0 ; index < fields.size(); index++){
                Field field = fields.get(index);
                String colName = field.getRecColName().split("::")[1];
                if(index == fields.size() - 1){
                    query.append(colName).append(" = :").append(colName);
                }else {
                    query.append(colName).append(" = :").append(colName).append(", ");
                }
            }
            query.append(" WHERE record_id = :recordId");
            Query entQuery = entityManager.createNativeQuery(query.toString());
            entQuery.setParameter("recordId", record.getRecordId());
            for(Field field : fields){
                String colName = field.getRecColName().split("::")[1];
                entQuery.setParameter(colName, recordDetails.get(field.getFieldName()));
            }
            entQuery.executeUpdate();
            LOGGER.log(Level.INFO, "Updated record :: {0}", record.toString());
        }
        return record;
    }

    public String getRecordTableNameForModule(Module module) {
        return module.getPluralName() + "_rec";
    }

    public List<Record> getAllRecordsForModule(Long moduleId, Integer page, Integer limit) {
        page = page == null ? 1 : page;
        page = page - 1;
        limit = limit == null || limit > 500 ? 10 : limit;
        List<Record> records = new ArrayList<>();
        Module module = moduleService.findById(moduleId);
        if(module == null){
            throw new APIException(HttpStatus.NOT_FOUND.value(), "Module not found");
        }
        List<Object[]> recordObj = entityManager.createNativeQuery("SELECT * from " + getRecordTableNameForModule(module) + " ORDER BY record_id DESC LIMIT " + limit + " OFFSET " + page * limit).getResultList();
        for (Object[] recObj : recordObj) {
            records.add(getRecordFromRecordObj(module, recObj));
        }
        return records;
    }

    public Record getRecordForModuleById(Long moduleId, Long recordId) {
        Module module = moduleService.findById(moduleId);
        if(module == null){
            throw new APIException(HttpStatus.NOT_FOUND.value(), "Module not found");
        }
        List<Object[]> recordObj = entityManager.createNativeQuery("SELECT * from " + getRecordTableNameForModule(module) + " where record_id = :recordId")
                                    .setParameter("recordId", recordId)
                                    .getResultList();
        if(!recordObj.isEmpty()){
            return getRecordFromRecordObj(module, recordObj.get(0));
        }
        return null;
    }

    private Record getRecordFromRecordObj(Module module, Object[] recObj) {
        Record record = new Record();
        record.setModuleId(module.getModuleId());
        record.setRecordId((Long) recObj[0]);
        record.setLayoutId((Long) recObj[1]);
        record.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) recObj[2]), ZoneId.systemDefault()));
        record.setUpdatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) recObj[3]), ZoneId.systemDefault()));
        record.setRecordDetails(getRecordDetailsFromRecordObj(module, (Long) recObj[1], recObj));
        return record;
    }

    private Map<String, Object> getRecordDetailsFromRecordObj(Module module, Long layoutId, Object[] recObj) {
        Map<String, Object> recordDetails = new HashMap<>();
        Layout layout = module.getLayouts().stream().filter(elem -> elem.getLayoutId().equals(layoutId)).findFirst().orElse(null);
        List<Field> fields = layout.getFields();
        int offset = 4;
        for(int index = 4; index < recObj.length; index++){
            if(recObj[index] != null){
                int actualIndex = index - offset;
                Field fld = fields.stream().filter(elem -> elem.getRecColName().split("::")[1].equals("field_" + actualIndex)).findFirst().orElse(null);
                if(fld != null){
                    recordDetails.put(fld.getFieldName(), recObj[index]);
                }
            }
        }
        return recordDetails;
    }

    private void validateFields(Map<String, Object> recordDetails, List<Field> fields) {
        for (Field field : fields) {
            List<FieldProperties> fieldProperties = field.getFieldProperties();
            List<FieldPropertiesRef> fieldPropertiesRefs = fieldPropertiesRefRepo.findByFieldType(field.getFieldType());
            for (FieldPropertiesRef propRef : fieldPropertiesRefs) {
                FieldProperties fieldProp = fieldProperties.stream().filter(fld -> fld.getProperty().getPropertyName().equals(propRef.getPropertyName())).findFirst().orElse(null);
                if(fieldProp == null){
                    throw new APIException(HttpStatus.BAD_REQUEST.value(), "Field Prop not found");
                }
                if(propRef.getPropertyName().equals("isMandatory")){
                    boolean isMandatory = Boolean.parseBoolean(fieldProp.getPropertyValue());
                    if(isMandatory && recordDetails.get(field.getFieldName()) == null) {
                        throw new APIException(HttpStatus.BAD_REQUEST.value(), "Mandatory Field Value not found");
                    }
                }
                if(propRef.getPropertyName().equals("maxChar")){
                    int maxChar = Integer.parseInt(fieldProp.getPropertyValue());
                    Object recordValue = recordDetails.get(field.getFieldName());
                    if(recordValue != null && recordValue.toString().length() > maxChar){
                        throw new APIException(HttpStatus.BAD_REQUEST.value(), "Max Character Value Exceeded");
                    }
                }
            }
        }
    }


    public void deleteRecordById(Long moduleId, Long recordId) {
        Module module = moduleService.findById(moduleId);
        String deleteQuery = "DELETE FROM " + getRecordTableNameForModule(module) + " WHERE record_id = :recordId";
        Query entQuery = entityManager.createNativeQuery(deleteQuery);
        entQuery.setParameter("recordId", recordId);
        int count = entQuery.executeUpdate();
        if(count == 0){
            throw new APIException(HttpStatus.NOT_FOUND.value(), "Record not found");
        }
        LOGGER.log(Level.INFO, "Deleted record :: {0}", recordId);
    }
}
