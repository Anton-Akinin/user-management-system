package com.example.usermanagementsystem.service;

import com.example.usermanagementsystem.exceptions.*;
import com.example.usermanagementsystem.model.dtos.AdminCreateUserDto;
import com.example.usermanagementsystem.model.dtos.ResetPasswordDto;
import com.example.usermanagementsystem.model.entity.Role;
import com.example.usermanagementsystem.model.entity.User;
import com.example.usermanagementsystem.model.dtos.CreateUserDto;
import com.example.usermanagementsystem.model.dtos.UpdateUserDto;
import com.example.usermanagementsystem.model.enums.RoleName;
import com.example.usermanagementsystem.repository.RoleRepository;
import com.example.usermanagementsystem.repository.UserRepository;
import com.example.usermanagementsystem.service.interfaces.UserService;
import com.example.usermanagementsystem.util.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User createUser(AdminCreateUserDto adminCreateUserDto) {
        log.info("IN createUser - creating new user with email: {}", adminCreateUserDto.getEmail());

        String email = adminCreateUserDto.getEmail();
        validateEmailAvailability(email);

        String password = PasswordGenerator.generatePassword();

        User newUser = new User();

        newUser.setEmail(adminCreateUserDto.getEmail());
        newUser.setFirstName(adminCreateUserDto.getFirstName());
        newUser.setLastName(adminCreateUserDto.getLastName());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setPasswordResetRequired(true);

        Role roleUser = roleRepository.findByName(RoleName.ROLE_USER.name());
        newUser.setRole(roleUser);
        userRepository.save(newUser);

        //set an unencrypted password because this field will be used for email
        newUser.setPassword(password);

        log.debug("IN createUser - user with email {} created", email);
        return newUser;
    }

    @Override
    public User register(CreateUserDto createUserDto) {

        String email = createUserDto.getEmail();

        validatePasswordMatch(createUserDto.getPassword(), createUserDto.getConfirmPassword());

        validateEmailAvailability(email);

        User newUser = new User();

        newUser.setEmail(createUserDto.getEmail());
        newUser.setFirstName(createUserDto.getFirstName());
        newUser.setLastName(createUserDto.getLastName());
        newUser.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        newUser.setGender(createUserDto.getGender());
        newUser.setBirthDate(createUserDto.getBirthDate());
        newUser.setPasswordResetRequired(false);

        Role roleUser = roleRepository.findByName(RoleName.ROLE_USER.name());
        newUser.setRole(roleUser);
        User registeredUser = userRepository.save(newUser);
        log.info("IN register - user: {} successfully registered", registeredUser);

        return newUser;
    }

    @Override
    public User updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = findById(id);

        if (updateUserDto.getFirstName() != null) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            user.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getBirthDate() != null) {
            user.setBirthDate(updateUserDto.getBirthDate());
        }
        if (updateUserDto.getGender() != null) {
            user.setGender(updateUserDto.getGender());
        }

        User updatedUser = userRepository.save(user);
        log.info("IN updateUser - user: {} successfully updated", updatedUser);

        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        findById(id);
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public List<User> getUsers(List<String> firstNames,
                               List<String> lastNames,
                               List<Long> userIds,
                               String roles,
                               String currentUserEmail) {
        User currentUser = findByEmail(currentUserEmail);
        List<User> users = userRepository.findAll();

        if (firstNames != null) {
            users = users.stream()
                    .filter(user -> firstNames.contains(user.getFirstName()))
                    .collect(Collectors.toList());
        }

        if (lastNames != null) {
            users = users.stream()
                    .filter(user -> lastNames.contains(user.getLastName()))
                    .collect(Collectors.toList());
        }

        if (userIds != null) {
            users = users.stream()
                    .filter(user -> userIds.contains(user.getId()))
                    .collect(Collectors.toList());
        }

        if (roles != null) {
            if (currentUser.getRole().getName().equals(RoleName.ROLE_ADMIN.name())) {
                users = users.stream()
                        .filter(user -> user.getRole().getName().equals(roles))
                        .collect(Collectors.toList());
            } else {
                throw new PermissionDeniedException("You don't have permission for filter by role");
            }
        }

        return users;
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        String email = resetPasswordDto.getEmail();
        User user = findByEmail(email);

        if (!passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())) {
            throw new PasswordException("Incorrect old password");
        }

        validatePasswordMatch(resetPasswordDto.getNewPassword(), resetPasswordDto.getConfirmNewPassword());

        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setPasswordResetRequired(false);

        log.info("IN resetPassword - password for user with email: {} successfully reset", email);
        userRepository.save(user);
    }

    @Override
    public void assignAdminRoleToUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN.name());
        user.setRole(roleAdmin);
        userRepository.save(user);
    }

    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordException("Password mismatch");
        }
    }

    private void validateEmailAvailability(String email) {
        Optional<User> userForRegistration = userRepository.findByEmailIgnoreCase(email);
        userForRegistration.ifPresent(usr -> {
            if (usr.isPasswordResetRequired()) {
                throw new EmailException("Login details have already been sent to your email \n" +
                        "Please check your email");
            } else {
                throw new EmailException("Email is already taken");
            }
        });
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User not found in database")
        );
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                new UserNotFoundException("User with email: " + email + " not found")
        );
    }
}
