package com.aegis.es_demo.jpaListener;

import com.aegis.es_demo.dao.AuditDao;
import com.aegis.es_demo.domin.Audit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Slf4j
@Component
@Transactional
public class AuditListener {
    @PrePersist
    public void setCreatedOn(AuditDao auditDao) {
        log.info("保存前");
        Audit audit = auditDao.getAudit();

        if (audit == null) {
            audit = new Audit();
            auditDao.setAudit(audit);
        }

        audit.setCreatedOn(LocalDateTime.now());
        audit.setUpdatedOn(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdatedOn(AuditDao auditDao) {
        log.info("更新前");
        Audit audit = auditDao.getAudit();
        audit.setUpdatedOn(LocalDateTime.now());
    }
}
