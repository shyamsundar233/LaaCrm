package com.laacrm.main.core.service;

import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.MigrationEntry;
import com.laacrm.main.core.entity.ScheduledTasks;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MigrationService implements ServiceWrapper<MigrationEntry> {

    private final DaoHelper<MigrationEntry, Long> migrationEntryDao;

    @Override
    public List<MigrationEntry> findAll() {
        return migrationEntryDao.findAll(MigrationEntry.class);
    }

    @Override
    public MigrationEntry findById(Long id) {
        return null;
    }

    @Override
    public MigrationEntry save(MigrationEntry entity) {
        return migrationEntryDao.save(entity);
    }

    @Override
    public MigrationEntry update(MigrationEntry entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    public MigrationEntry getMigrationEntryByName(String migrationName) {
        return migrationEntryDao.findByCriteria(MigrationEntry.class, context -> {
            CriteriaBuilder cb = context.getCriteriaBuilder();
            Root<MigrationEntry> root = context.getRoot();
            context.addPredicate(cb.equal(root.get("migrationName"), migrationName));
        }).stream().findFirst().orElse(null);
    }
}
