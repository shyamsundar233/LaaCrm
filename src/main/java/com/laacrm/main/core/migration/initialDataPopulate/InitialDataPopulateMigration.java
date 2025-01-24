package com.laacrm.main.core.migration.initialDataPopulate;

import com.laacrm.main.core.migration.config.MigrationWrapper;

public class InitialDataPopulateMigration implements MigrationWrapper {

    public InitialDataPopulateMigration() {}

    @Override
    public void run() {
        try{
            InitialDataPopulateUtil.populateInitialData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
