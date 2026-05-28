package mn.edu.room.core.service;

import java.util.List;
import mn.edu.room.core.domain.AuditLog;
import mn.edu.room.core.dto.AuditLogResponseDto;
import mn.edu.room.core.port.in.AuditLogUseCase;
import mn.edu.room.core.port.out.AuditLogRepository;

public class AuditLogService implements AuditLogUseCase {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public List<AuditLogResponseDto> listAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private AuditLogResponseDto toDto(AuditLog log) {
        String createdAt = log.getCreatedAt() == null ? "" : log.getCreatedAt().toString().replace('T', ' ');
        return new AuditLogResponseDto(log.getId(), log.getBookingId(), log.getMessage(), createdAt);
    }
}
