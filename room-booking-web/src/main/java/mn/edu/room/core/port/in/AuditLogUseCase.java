package mn.edu.room.core.port.in;

import java.util.List;
import mn.edu.room.core.dto.AuditLogResponseDto;

public interface AuditLogUseCase {
    List<AuditLogResponseDto> listAuditLogs();
}
