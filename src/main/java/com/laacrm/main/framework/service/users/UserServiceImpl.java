package com.laacrm.main.framework.service.users;

import com.laacrm.main.framework.entities.Tenant;
import com.laacrm.main.framework.entities.Users;
import com.laacrm.main.framework.exception.FrameworkException;
import com.laacrm.main.framework.repo.UsersRepo;
import com.laacrm.main.framework.service.role.RoleService;
import com.laacrm.main.framework.service.tenant.TenantService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private final UsersRepo usersRepo;
    private final RoleService roleService;
    private final TenantService tenantService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return null;
    }

    @Override
    public UserDTO saveUser(UserDTO userDetails) {
        LOGGER.log(Level.INFO, "==========> User Creation Starts <==========");
        createUserValidation(userDetails);
        Users users = new Users(
                userDetails.getUserName(),
                generateUserCode(),
                passwordEncoder.encode(userDetails.getPassword()),
                userDetails.getEmail(),
                userDetails.getPhone(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                roleService.getRoleByName(userDetails.getRoleName())
        );
        Tenant nextAvailableTenant = tenantService.getNextAvailableTenant();
        if(nextAvailableTenant == null) {
            throw new FrameworkException(HttpStatus.INSUFFICIENT_STORAGE.value(), "No Tenant Available");
        }
        users.setTenant(nextAvailableTenant);
        users = usersRepo.save(users);
        tenantService.allocateRangeForTenant(nextAvailableTenant);
        nextAvailableTenant.setIsActive(Boolean.TRUE);
        tenantService.saveTenant(nextAvailableTenant);
        LOGGER.log(Level.INFO, "==========> User Creation Successful :: {0} <==========", users.getUserCode());
        users.setUserId(users.getUserId());
        return userDetails;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public Users authenticateUser(LoginUser loginUserDetails) {
        try{
            Users loginUser = loginUserDetails.getUsername() != null && !loginUserDetails.getUsername().isEmpty() ?
                        usersRepo.findByUserName(loginUserDetails.getUsername()).orElseThrow(() -> new FrameworkException(HttpStatus.UNAUTHORIZED.value(), "User not found, Username is invalid")) :
                        usersRepo.findByEmail(loginUserDetails.getEmail()).orElseThrow(() -> new FrameworkException(HttpStatus.UNAUTHORIZED.value(), "User not found, Email is invalid"));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            loginUserDetails.getPassword()
                    )
            );

            return loginUser;
        } catch (Exception exp) {
            throw new FrameworkException(HttpStatus.UNAUTHORIZED.value(), exp.getMessage());
        }
    }

    private void createUserValidation(UserDTO userDetails) {
        LOGGER.log(Level.INFO, "==========> User Creation Validation Starts <==========");
        int exceptionCode = -1;
        exceptionCode = userDetails.getUserName() == null ? 1 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getUserName().isEmpty() ? 2 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getPassword() == null ? 3 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getPassword().isEmpty() ? 4 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getEmail() == null ? 5 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getEmail().isEmpty() ? 6 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getPhone() == null ? 7 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getPhone().isEmpty() ? 8 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getFirstName() == null ? 9 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getFirstName().isEmpty() ? 10 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getLastName() == null ? 11 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getLastName().isEmpty() ? 12 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getRoleName() == null ? 13 : exceptionCode;
        exceptionCode = exceptionCode == -1 && userDetails.getRoleName().isEmpty() ? 14 : exceptionCode;

        if(exceptionCode != -1){
            String message = switch (exceptionCode) {
                case 1 -> "User Name cannot be null";
                case 2 -> "User Name cannot be empty";
                case 3 -> "Password cannot be null";
                case 4 -> "Password cannot be empty";
                case 5 -> "Email cannot be null";
                case 6 -> "Email cannot be empty";
                case 7 -> "Phone cannot be null";
                case 8 -> "Phone cannot be empty";
                case 9 -> "First Name cannot be null";
                case 10 -> "First Name cannot be empty";
                case 11 -> "Last Name cannot be null";
                case 12 -> "Last Name cannot be empty";
                case 13 -> "Role Name cannot be null";
                case 14 -> "Role Name cannot be empty";
                default -> "Unknown exceptionCode";
            };
            LOGGER.log(Level.SEVERE, "==========> Exception Occurred in User Creation Validation :: {0} <==========", message);
            throw new FrameworkException(HttpStatus.BAD_REQUEST.value(), message);
        }
        LOGGER.log(Level.INFO, "==========> User Creation Validation Completed <==========");
    }

    private String generateUserCode() {
        Long maxSuffix = usersRepo.findMaxUserCodeSuffix();
        maxSuffix = (maxSuffix == null) ? 1 : maxSuffix + 1;
        return "USR-" + String.format("%05d", maxSuffix);
    }
}
