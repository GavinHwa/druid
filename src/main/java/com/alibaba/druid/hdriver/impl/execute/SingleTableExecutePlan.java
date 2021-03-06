package com.alibaba.druid.hdriver.impl.execute;

import com.alibaba.druid.hdriver.impl.mapping.HMapping;
import com.alibaba.druid.hdriver.impl.mapping.HMappingDefaultImpl;

public class SingleTableExecutePlan extends ExecutePlanAdapter {

    private String   tableName;

    private HMapping mapping;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public HMapping getMapping() {
        HMapping mapping = this.mapping;
        if (mapping == null) {
            mapping = new HMappingDefaultImpl();
        }
        return mapping;
    }

    public void setMapping(HMapping mapping) {
        this.mapping = mapping;
    }

}
