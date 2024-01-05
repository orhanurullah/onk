package com.onk.serviceImpl;

import com.onk.component.MessageService;
import com.onk.core.exception.ErrorCode;
import com.onk.core.exception.GenericException;
import com.onk.core.results.*;
import com.onk.core.sendmail.EmailService;
import com.onk.core.sendmail.MailObject;
import com.onk.core.utils.EntityConstants;
import com.onk.dto.AddressDto;
import com.onk.dto.RoleDto;
import com.onk.dto.UserDto;
import com.onk.model.Address;
import com.onk.model.Role;
import com.onk.model.User;
import com.onk.repository.AddressRepository;
import com.onk.repository.RoleRepository;
import com.onk.repository.UserRepository;
import com.onk.security.auth.SecurityManager;
import com.onk.security.jwt.JwtTokenProvider;
import com.onk.security.request.UserRequest;
import com.onk.service.RoleService;
import com.onk.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${admin.email}")
    private String root;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String mail;

    @Value("${settings.email.validate.isRequired}")
    private boolean isRequiredEmailValidation;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;

    private final JwtTokenProvider jwtTokenProvider;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final MessageService messageService;

    private final AddressRepository addressRepository;


    @Override
    public UserDto createUser(UserRequest userRequest){
        if(userRepository.findByEmail(userRequest.getEmail().trim()).isPresent()){
            if(userRepository.findByEmail(userRequest.getEmail().trim()).get().getIsDeleted()){
                var user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow(null);
                user.setIsDeleted(false);
                user.setIsActive(true);
                userRepository.save(user);
                return convertToUserDto(user);
            }
            throw new GenericException(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, messageService.getMessage("user.email.already_register", null));
        }
        try{
            var newPassword = passwordEncoder.encode(userRequest.getPassword());
            User user = User.builder()
                    .name(userRequest.getName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .password(newPassword)
                    .roles(new HashSet<>())
                    .isActive(false)
                    .isDeleted(false)
                    .build();
            var savedUser = this.userRepository.save(user);
            if(!isRequiredEmailValidation){
                savedUser.setIsActive(true);
                registerUserWithRole(savedUser);
            }else{
                sendEmailConfirmation(savedUser);
            }
            return convertToUserDto(savedUser);
        }catch (Exception e){
            throw GenericException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .errorMessage(messageService.getMessage("user.register.false", null))
                    .build();
        }
    }

    @Override
    public DataResult<UserDto> getUser(String email){
        if(email.equalsIgnoreCase(root) && !Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail().equals(root)){
            return new ErrorDataResult<>(
                    messageService.getMessage("user.rootEmailQuery.message", null)
            );
        }
        try{
            var user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
            return new SuccessDataResult<>(
                    convertToUserDto(user),
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{email})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public User findUserByUsername(String email){
        try{
            return this.userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(messageService.getMessage("error.not_found", new Object[]{email}));
        }catch (Exception e){
            throw GenericException.builder()
                    .errorMessage(messageService.getMessage("error.message", null))
                    .build();
        }
    }

    @Override
    public DataResult<List<UserDto>> getUserByName(String name) {
        try{
            var users = this.userRepository.getUserByName(name);
            var dtoUsers = users.stream().filter(u -> !u.getEmail().equalsIgnoreCase(root)).map(UserServiceImpl::convertToUserDto).toList();
            return new SuccessDataResult<>(
                    dtoUsers,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(
                    messageService.getMessage("error.not_found", new Object[]{name})
            );
        }catch (Exception e){
            throw GenericException.builder()
                    .errorMessage(messageService.getMessage("error.message", null))
                    .build();
        }
    }

    @Override
    public DataResult<List<UserDto>> getUserByNameAndLastName(String name, String lastName) {
        try{
            var users = this.userRepository.getUserByNameAndLastName(name, lastName);
            var dtoUsers = users.stream().filter(u -> !u.getEmail().equalsIgnoreCase(root))
                    .map(UserServiceImpl::convertToUserDto).toList();
            return new SuccessDataResult<>(
                    dtoUsers,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(
                    messageService.getMessage("error.not_found", new Object[]{name + " " + lastName})
            );
        }catch (Exception e){
            throw GenericException.builder()
                    .errorMessage(messageService.getMessage("error.message", null))
                    .build();
        }
    }

    @Override
    public DataResult<List<UserDto>> getUsers(){
        try{
            var users = userRepository.allActiveUsers().stream().filter(u -> !u.getEmail().equals(root)).toList();
            if(users.isEmpty()){
                return new ErrorDataResult<>(
                        messageService.getMessage("error.not_found", null)
                );
            }
            var dtoUsers = users.stream().map(UserServiceImpl::convertToUserDto).toList();
            return new SuccessDataResult<>(
                    dtoUsers,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<UserDto>> getDeletedUsers() {
        try{
            var users = userRepository.allDeletedUsers().stream().filter(u -> !u.getEmail().equalsIgnoreCase(root)).toList();
            if(users.isEmpty()){
                return new SuccessDataResult<>(
                        messageService.getMessage("success.process.null.data.message", null)
                );
            }
            var dtoUsers = users.stream().map(UserServiceImpl::convertToUserDto).toList();
            return new SuccessDataResult<>(
                    dtoUsers,
                    messageService.getMessage("success.data.message", null)
            );
        }catch(Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<UserDto>> getUsersByIsDeletedFalse() {
        try{
            var users = userRepository.getUsersByIsDeletedFalse().stream().filter(u -> !u.getEmail().equalsIgnoreCase(root)).toList();
            if(users.isEmpty()){
                return new ErrorDataResult<>(
                        messageService.getMessage("error.not_found", new Object[]{users.toString()})
                );
            }
            var dtoUsers = users.stream().map(UserServiceImpl::convertToUserDto).toList();
            return new SuccessDataResult<>(
                    dtoUsers,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result addAddressToUser(AddressDto addressDto) {
        try{
            var user = SecurityManager.getCurrentUser();
            var address = convertToAddress(user, addressDto);
            addressRepository.save(address);
            return new SuccessResult(
                    messageService.getMessage("user.addAddress.success.message", null)
            );
        }catch(Exception e){
            return new ErrorResult(
                    messageService.getMessage("user.addAddress.error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<AddressDto>> getAddresses() {
        try{
            var user = SecurityManager.getCurrentUser();
            assert user != null;
            var dtoAddress = addressRepository.userAddresses(user.getId()).stream().map(UserServiceImpl::convertToAddressDto).toList();
            userRepository.save(user);
            return new SuccessDataResult<>(
                    dtoAddress,
                    messageService.getMessage("success.data.message", null)
            );
        }catch(Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<AddressDto>> getUserAddresses(Long userId) {
       try{
           var user = userRepository.findById(userId).orElse(null);
           if(user != null){
               var dtoAddress = addressRepository.userAddresses(user.getId()).stream().map(UserServiceImpl::convertToAddressDto).toList();
               return new SuccessDataResult<>(
                       dtoAddress,
                       messageService.getMessage("success.data.message", null)
               );
           }else{
               return new ErrorDataResult<>(
                       messageService.getMessage("error.not_found", new Object[]{userId})
               );
           }
       }catch(Exception e){
           return new ErrorDataResult<>(
                   messageService.getMessage("error.message", null)
           );
       }
    }

    @Override
    public DataResult<UserDto> findById(Long id) {
        try{
            var user = this.userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equalsIgnoreCase(root)){
                return new ErrorDataResult<>(
                        messageService.getMessage("user.rootEmailQuery.message", null)
                );
            }
            return new SuccessDataResult<>(
                    convertToUserDto(user),
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }
    @Override
    public Result changeUserPassword(String password) {
        try{
            String email = Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail();
            var pass = passwordEncoder.encode(password);
            this.userRepository.changeUserPassword(pass, email);
            return new SuccessResult(
                    messageService.getMessage("user.change.password.true", null)
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("user.change.password.false", null)
            );
        }
    }

    @Override
    public Result updateUser(UserDto userDto) {
        try{
            var user = Objects.requireNonNull(SecurityManager.getCurrentUser());
            this.userRepository.updateUser(
                    userDto.getName(),
                    userDto.getLastName(),
                    user.getEmail(),
                    user.getId());
            return new SuccessResult(
                    messageService.getMessage("user.update.true", null)
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("user.update.false", null)
            );
        }
    }

    @Override
    public Result deleteOwnUser() {
        try{
            String email = Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail();
            var user = userRepository.findByEmailAddress(email).orElseThrow(EntityNotFoundException::new);
            if (user.getRoles().stream().anyMatch(e -> e.getName().equalsIgnoreCase("root"))){
                return new ErrorResult(
                        messageService.getMessage("user.delete.hasRoleRoot.false", null)
                );
            }
            if(user.getRoles().stream().anyMatch(e -> e.getName().equalsIgnoreCase("admin"))){
                return new ErrorResult(
                        messageService.getMessage("user.delete.hasRoleAdmin.false", null)
                );
            }
            user.setIsActive(false);
            user.setIsDeleted(true);
            return new SuccessResult(
                    messageService.getMessage("user.delete.hasRoleOnlyUser.true", null)
            );
        }catch (Exception e){
            return new ErrorResult(messageService.getMessage("error.message", null));
        }
    }

    @Override
    public Result deleteUser(Long id) {
        try{
            var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equals(root)){
                return new ErrorResult(
                        messageService.getMessage("user.delete.hasRoleRoot.false", null)
                );
            }
            this.userRepository.changeUserIsActive(user.getId(), false);
            this.userRepository.changeUserIsDeleted(user.getId(), true);
            return new SuccessResult(
                    messageService.getMessage("admin.delete.user.true", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorResult(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("admin.delete.user.false", null)
            );
        }
    }

    @Override
    public Result addRoleToUser(Long userId, Long roleId) {
        try{
            User user = userRepository.findById(userId)
                    .orElse(null);
            if(user == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", new Object[]{userId})
                );
            }
            Role role = roleRepository.findById(roleId)
                    .orElse(null);
            if(role == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", new Object[]{roleId})
                );
            }
            if(user.getRoles().contains(role)){
                return new ErrorResult(
                        messageService.getMessage("role.reAssigned.false.message", new Object[]{role.getName()})
                );
            }
            if (role.getName().equalsIgnoreCase("root")) {
                return new ErrorResult(
                        messageService.getMessage("role.assigned.false.message", new Object[]{role.getName()})
                );
            }
            user.getRoles().add(role);
            userRepository.save(user);
            return new SuccessResult(
                    messageService.getMessage("role.assigned.true.message", new Object[]{role.getName()})
            );
        }catch (Exception e) {
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result deleteRoleFromUser(Long userId, Long roleId) {
        try{
            var user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equalsIgnoreCase(root)){
                return new ErrorResult(
                        messageService.getMessage("admin.never.delete.role.fromAdmin.message", new Object[]{user.getEmail()})
                );
            }
            var role = roleService.getRoleById(roleId).getData();
            if(role == null){
                return new ErrorResult(
                        messageService.getMessage("error.not_found", new Object[]{roleId})
                );
            }
            var roles = user.getRoles();
            if(role.getName().equalsIgnoreCase("user")){
                return new ErrorResult(
                        messageService.getMessage("admin.delete.roleUser.from.user.false", null)
                );
            }
            if(roles.stream().noneMatch(e -> e.getName().equals(role.getName()))){
                return new ErrorResult(
                        messageService.getMessage("user.hasNot.role.message", new Object[]{role.getName()})
                );
            }
            roles.remove(convertToRole(role));
            userRepository.save(user);
            return new SuccessResult(
                    messageService.getMessage("admin.delete.role.success.message", new Object[]{role.getName()})
            );
        }catch (EntityNotFoundException e){
            return new ErrorResult(
                    messageService.getMessage("error.not_found", new Object[]{userId})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<? extends GrantedAuthority>> authorities(Long id){
        try{
            var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if(user.getEmail().equalsIgnoreCase(root) && !Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail().equalsIgnoreCase(root)){
                return new ErrorDataResult<>(
                        messageService.getMessage("user.rootEmailQuery.message", null)
                );
            }
            var roles = user.getRoles().stream().map(role ->
                    new SimpleGrantedAuthority("ROLE_"+ role.getName())).toList();
            return new SuccessDataResult<>(
                    roles,
                    messageService.getMessage("success.data.message", null)
            );
        }catch(EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", new Object[]{id})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<? extends GrantedAuthority>> getUserRole() {
        try{
            String email = Objects.requireNonNull(SecurityManager.getCurrentUser()).getEmail();
            var roles = userRepository.findByEmailAddress(email).orElseThrow(EntityNotFoundException::new).getRoles().stream().map(role ->
                    new SimpleGrantedAuthority("ROLE_"+ role.getName())).toList();
            return new SuccessDataResult<>(
                    roles,
                    messageService.getMessage("success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<?> getCurrentUser(String email) {
       try{
           var user = SecurityManager.getCurrentUser();
           assert user != null;
           if(user.getEmail().equals(email)){
               return new SuccessDataResult<>(
                       SecurityManager.getCurrentUser(),
                       messageService.getMessage("token.validateUser.message", null)
               );
           }else{
               return new ErrorDataResult<>(
                       messageService.getMessage("token.notValidateUser.message", null)
               );
           }

       }catch (Exception e){
           return new ErrorDataResult<>(
                   messageService.getMessage("error.message", null)
           );
       }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByUsername(email);
        if(user == null){
            throw new UsernameNotFoundException(
                    messageService.getMessage("invalid.user.login.data", null)
            );
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getAuthorities());
    }



    private static Address convertToAddress(User user, AddressDto addressDto){
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .county(addressDto.getCounty())
                .neighbourhood(addressDto.getNeighbourhood())
                .street(addressDto.getStreet())
                .detail(addressDto.getDetail())
                .zipCode(addressDto.getZipCode())
                .user(user)
                .build();
    }

    private static AddressDto convertToAddressDto(Address address){
        return AddressDto.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .county(address.getCounty())
                .neighbourhood(address.getNeighbourhood())
                .street(address.getStreet())
                .detail(address.getDetail())
                .zipCode(address.getZipCode())
                .build();
    }

    private static Role convertToRole(RoleDto roleDto){
        return Role.builder()
                .name(roleDto.getName())
                .description(roleDto.getDescription())
                .build();
    }

    private static UserDto convertToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .email(user.getEmail())
                .build();
    }

    private void sendEmailConfirmation(User user){
        long exp = 1000 * 60 * 60; // 60 minute | 1 hour
        String token = jwtTokenProvider.generateToken(user, exp);
        Map<String, Object> model = getStringObjectMap(user, token);
        emailService.sendMail(MailObject.builder()
                        .from(mail)
                        .to(Collections.singleton(user.getEmail()))
                        .subject(messageService.getMessage("app.name" , null))
//                        .attachments(attachmentObjects)
                        .build(), "emailActivation.ftl"
                , model);
    }

    private Map<String, Object> getStringObjectMap(User user, String token){
        String urlActivation = baseUrl + "/api/v1/auth/" + user.getId() + "/activation" + "?token="+ token;
        Map<String, Object> model = new HashMap<>();
        model.put("name", messageService.getMessage("app.name", null));
        model.put("activationLink", urlActivation);
        model.put("title", messageService.getMessage("welcome.message", null));
        return model;
    }

    public Result activation(String token, Long id) {
        var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if(user == null){
            return new ErrorResult(
                    messageService.getMessage("error.not_found", new Object[]{"User "})
            );
        }
        if (token == null) {
            return new ErrorResult(
                    messageService.getMessage("error.not_found", new Object[]{"Token "})
            );
        }
        if(!jwtTokenProvider.validateToken(token, user)){
            return new ErrorResult(
                    messageService.getMessage("token.validate.error", null)
            );
        }
        user.setIsActive(true);
        if(isRequiredEmailValidation){
            registerUserWithRole(user);
        }
        if(!user.getIsActive()){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
        return new SuccessResult(
                messageService.getMessage("email.activation.true", null)
        );

    }

    private void registerUserWithRole(User user){
        Role baseRole = roleRepository.findByRoleName(EntityConstants.baseRoleName);
        if(baseRole == null){
            baseRole = Role.builder()
                    .name(EntityConstants.baseRoleName)
                    .description(EntityConstants.baseRoleDescription)
                    .build();
            roleRepository.save(baseRole);
        }
        Set<Role> roles = new HashSet<>();
        roles.add(baseRole);
        if(root.equals(user.getEmail().trim())){
            Role primaryRole = roleRepository.findByRoleName(EntityConstants.primaryRoleName);
            if(primaryRole == null){
                primaryRole = Role.builder()
                        .name(EntityConstants.primaryRoleName)
                        .description(EntityConstants.primaryRoleDescription)
                        .build();
                roleRepository.save(primaryRole);
            }
            Role secondaryRole = roleRepository.findByRoleName(EntityConstants.secondaryRoleName);
            if(secondaryRole == null){
                secondaryRole = Role.builder()
                        .name(EntityConstants.secondaryRoleName)
                        .description(EntityConstants.secondaryRoleDescription)
                        .build();
                roleRepository.save(secondaryRole);
            }
            roles.add(primaryRole);
            roles.add(secondaryRole);
        }
        user.setRoles(roles);
    }

}
