package mn.edu.room.core.port.in;

import mn.edu.room.core.dto.LoginDto;
import mn.edu.room.core.dto.RegisterDto;
import mn.edu.room.core.dto.UserSessionDto;

public interface AuthUseCase {
    UserSessionDto login(LoginDto loginDto);

    UserSessionDto registerStudent(RegisterDto registerDto);
}
