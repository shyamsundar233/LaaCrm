package com.laacrm.main.core.service;

import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.Module;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ModuleServiceImpl implements ServiceWrapper<Module> {

    private final DaoHelper<Module, Long> moduleDaoHelper;

    @Override
    public List<Module> findAll() {
        return moduleDaoHelper.findAll(Module.class);
    }

    @Override
    public Module findById(Long id) {
        return null;
    }

    @Override
    public Module save(Module entity) {
        return moduleDaoHelper.save(entity);
    }

    @Override
    public Module update(Module entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
