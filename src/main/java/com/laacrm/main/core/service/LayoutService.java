package com.laacrm.main.core.service;

import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.Layout;
import com.laacrm.main.core.entity.Module;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LayoutService implements ServiceWrapper<Layout> {

    private final DaoHelper<Layout, Long> layoutDaoHelper;

    public Layout getDefaultLayout(Module module){
        List<Layout> defaultLayout = layoutDaoHelper.findByCriteria(Layout.class, context -> {
            CriteriaBuilder cb = context.getCriteriaBuilder();
            Root<Layout> root = context.getRoot();
            context.addPredicate(cb.equal(root.get("module"), module));
        });
        return defaultLayout != null && !defaultLayout.isEmpty() ? defaultLayout.get(0) : null;
    }

    @Override
    public List<Layout> findAll(Object... param) {
        return List.of();
    }

    @Override
    public Layout findById(Long layoutId) {
        return layoutDaoHelper.findById(Layout.class, layoutId).orElse(null);
    }

    @Override
    public Layout save(Layout entity) {
        return null;
    }

    @Override
    public Layout update(Layout entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
