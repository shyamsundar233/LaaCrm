package com.laacrm.main.core.service;

import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.repo.ModuleRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ModuleServiceImpl implements ServiceWrapper<Module> {

    private final ModuleRepo moduleRepo;

    @Override
    public List<Module> findAll() {
        return List.of();
    }

    @Override
    public Module findById(Long id) {
        return null;
    }

    @Override
    public Module save(Module entity) {
        return null;
    }

    @Override
    public Module update(Module entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
