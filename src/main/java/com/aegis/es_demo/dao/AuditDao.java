package com.aegis.es_demo.dao;

import com.aegis.es_demo.domin.Audit;

public interface AuditDao {
    Audit getAudit();

    void setAudit(Audit audit);
}
