package com.example.config;


import com.example.entity.Permission;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.enums.Gender;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PermissionService permissionService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.sql.init.mode}")
    private String initMode;

    @Override
    public void run(String... args) throws Exception {

        if (permissionService.getList() == null) {
            permissionService.save(new Permission(1, "ROLE_ACCESS"));
            permissionService.save(new Permission(2, "ADD"));
            permissionService.save(new Permission(3, "read"));
        }

        if (initMode.equals("always")) {
            Role supper_admin = new Role(1, "SUPER_ADMIN");
            Role role = roleRepository.save(supper_admin);
            User admin = User.builder()
                    .fullName("ADMIN")
                    .phoneNumber("111111111")
                    .birthDate(LocalDate.parse("1998-05-13"))
                    .gender(Gender.ERKAK)
                    .registeredDate(LocalDateTime.now())
                    .verificationCode(0)
                    .password(passwordEncoder.encode("111111"))
                    .isBlocked(true)
                    .roles(List.of(role))
                    .build();
             userRepository.save(admin);
        }
    }
}
