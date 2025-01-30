package com.laacrm.main.core.service;

import com.laacrm.main.core.TaskRecordConstants;
import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.TaskRecord;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskRecordService implements ServiceWrapper<TaskRecord> {

    private final DaoHelper<TaskRecord, Long> taskRecordDaoHelper;

    @Override
    public List<TaskRecord> findAll(Object... param) {
        String sysName = (String) param[0];
        if(sysName != null && !sysName.isEmpty()){
            return taskRecordDaoHelper.findByCriteria(TaskRecord.class, context -> {
                CriteriaBuilder cb = context.getCriteriaBuilder();
                Root<TaskRecord> root = context.getRoot();

                context.addPredicate(cb.equal(root.get("sysName"), sysName));
            });
        }else{
            return taskRecordDaoHelper.findAll(TaskRecord.class);
        }
    }

    @Override
    public TaskRecord findById(Long id) {
        return null;
    }

    @Override
    public TaskRecord save(TaskRecord entity) {
        return taskRecordDaoHelper.save(entity);
    }

    @Override
    public TaskRecord update(TaskRecord entity) {
        return taskRecordDaoHelper.update(entity);
    }

    @Override
    public void delete(Long id) {

    }

    public void addTaskRecord(String taskName, String sysName, TaskRecordConstants.TaskStatus status) {
        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setTaskName(taskName);
        taskRecord.setSysName(sysName);
        taskRecord.setTaskStatus(status.getValue());
        save(taskRecord);
    }

    public void updateTaskRecord(String sysName, TaskRecordConstants.TaskStatus status) {
        TaskRecord taskRecord = findAll(sysName).get(0);
        taskRecord.setTaskStatus(status.getValue());
        update(taskRecord);
    }
}
