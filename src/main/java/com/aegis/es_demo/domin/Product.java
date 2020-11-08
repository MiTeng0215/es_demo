package com.aegis.es_demo.domin;

import com.aegis.es_demo.dao.AuditDao;
import com.aegis.es_demo.jpaListener.AuditListener;

import javax.persistence.Embedded;
import javax.persistence.EntityListeners;

@EntityListeners(AuditListener.class)
public class Product implements AuditDao {

    @Embedded
    private Audit audit;

    @Override
    public Audit getAudit() {
        return audit;
    }

    @Override
    public void setAudit(Audit audit) {
        this.audit = audit;
    }
}
