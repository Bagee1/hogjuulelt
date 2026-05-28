package mn.edu.room.core.port.out;

import java.util.List;
import mn.edu.room.core.domain.AuditLog;

public interface AuditLogRepository {
    void save(AuditLog auditLog);

    List<AuditLog> findAll();
}
