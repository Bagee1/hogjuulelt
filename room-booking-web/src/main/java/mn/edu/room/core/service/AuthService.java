package mn.edu.room.core.service;

import mn.edu.room.core.domain.User;
import mn.edu.room.core.dto.LoginDto;
import mn.edu.room.core.dto.RegisterDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.AuthUseCase;
import mn.edu.room.core.port.out.UserRepository;

public class AuthService implements AuthUseCase {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserSessionDto login(LoginDto loginDto) {
        if (loginDto == null || isBlank(loginDto.username()) || isBlank(loginDto.password())) {
            throw new IllegalArgumentException("Нэвтрэх нэр болон нууц үгээ оруулна уу.");
        }
        User user = userRepository.findByUsername(loginDto.username().trim())
                .orElseThrow(() -> new IllegalArgumentException("Нэвтрэх нэр эсвэл нууц үг буруу байна."));
        if (!user.getPassword().equals(loginDto.password())) {
            throw new IllegalArgumentException("Нэвтрэх нэр эсвэл нууц үг буруу байна.");
        }
        return new UserSessionDto(user.getId(), user.getUsername(), user.getRole().name());
    }

    @Override
    public UserSessionDto registerStudent(RegisterDto registerDto) {
        if (registerDto == null || isBlank(registerDto.username()) || isBlank(registerDto.password())
                || isBlank(registerDto.confirmPassword())) {
            throw new IllegalArgumentException("Бүртгэлийн бүх талбарыг бөглөнө үү.");
        }
        String username = registerDto.username().trim();
        String password = registerDto.password();
        if (username.length() < 3) {
            throw new IllegalArgumentException("Нэвтрэх нэр дор хаяж 3 тэмдэгт байна.");
        }
        if (password.length() < 3) {
            throw new IllegalArgumentException("Нууц үг дор хаяж 3 тэмдэгт байна.");
        }
        if (!password.equals(registerDto.confirmPassword())) {
            throw new IllegalArgumentException("Нууц үг давталт таарахгүй байна.");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Энэ нэвтрэх нэр бүртгэлтэй байна.");
        }
        User user = userRepository.save(new User(null, username, password, mn.edu.room.core.domain.Role.STUDENT));
        return new UserSessionDto(user.getId(), user.getUsername(), user.getRole().name());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
