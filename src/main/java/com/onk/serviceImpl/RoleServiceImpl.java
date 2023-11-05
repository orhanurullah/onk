package com.onk.serviceImpl;

import com.onk.component.MessageService;
import com.onk.core.results.*;
import com.onk.core.utils.EntityConstants;
import com.onk.dto.RoleDto;
import com.onk.dto.UserDto;
import com.onk.model.Role;
import com.onk.model.User;
import com.onk.repository.RoleRepository;
import com.onk.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final MessageService messageService;
    @Override
    public Result addRole(RoleDto roleDto) {
        if(roleRepository.findRoleByNameEqualsIgnoreCase(roleDto.getName()) != null){
            return new ErrorResult(
                    messageService.getMessage("error.already_has_role", new Object[]{roleDto.getName()})
            );
        }
        try{
            Role newRole = Role.builder()
                    .name(roleDto.getName())
                    .description(roleDto.getDescription())
                    .build();
            this.roleRepository.save(newRole);
            return new SuccessResult(
                    messageService.getMessage("success.role.insert", new Object[]{newRole.getName()})
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.role.insert", null)
            );
        }
    }

    @Override
    public DataResult<RoleDto> getRoleByName(String name) {
        var role = this.roleRepository.findRoleByNameEqualsIgnoreCase(name);
        if (role == null) {
            return new ErrorDataResult<>(
                    messageService.getMessage("error.not_found", null)
            );
        }
        return new SuccessDataResult<>(
                convertRoleToRoleDto(role),
                messageService.getMessage("success.data.message", null)
        );
    }

    @Override
    public DataResult<RoleDto> getRoleById(Long id) {
        try{
            var role = roleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            return new SuccessDataResult<>(
                    convertRoleToRoleDto(role),
                    messageService.getMessage("success.data.message", null)
            );
        }catch (EntityNotFoundException e){
            return new ErrorDataResult<>(
                    messageService.getMessage("role.not.found.message",null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<RoleDto>> getAllRole() {
        try{
            var roles = this.roleRepository.findAll();
            var dtoRoles = roles.stream().map(RoleServiceImpl::convertRoleToRoleDto).toList();
            if(roles.isEmpty()){
                return new ErrorDataResult<>(
                      messageService.getMessage("role.not.found.message", null)
                );
            }
            return new SuccessDataResult<>(
                    dtoRoles,
                    messageService.getMessage("role.success.data.message", null)
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public DataResult<List<UserDto>> getUsersByRoleName(String roleName) {
        try{
            if(!getRoleByName(roleName).isSuccess()){
                return new ErrorDataResult<>(
                        messageService.getMessage("role.not.found.message", null)
                );
            }
            var users = this.roleRepository.findUsersByRoleName(roleName.toUpperCase(Locale.ROOT));
            if(users.isEmpty()){
                return new ErrorDataResult<>(
                        messageService.getMessage("success.process.null.data.message", null)
                );
            }
            var dtoUsers = users.stream().map(e -> UserDto.builder()
                    .name(e.getName())
                    .lastName(e.getLastName())
                    .email(e.getEmail())
                    .roles(e.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .build()).toList();
            return new SuccessDataResult<>(
                    dtoUsers,
                    messageService.getMessage("role.users.success.message", new Object[]{roleName})
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result deleteRole(Long id) {
        try{
            var role = this.roleRepository.findById(id)
                    .orElseThrow(EntityNotFoundException::new);
            if(role.getName().equalsIgnoreCase(EntityConstants.primaryRoleName) ||
                role.getName().equalsIgnoreCase(EntityConstants.secondaryRoleName) ||
                role.getName().equalsIgnoreCase(EntityConstants.baseRoleName)){
                return new ErrorResult(
                        messageService.getMessage("role.never.deleted.message", new Object[]{role.getName()})
                );
            }
            var users = role.getUsers();
            if(!users.isEmpty()){
                for(User u : users){
                    var a = u.getRoles();
                    a.remove(role);
                    u.setRoles(a);
                }

            }
            this.roleRepository.delete(role);
            return new SuccessResult(
                    messageService.getMessage("role.delete.success.message", new Object[]{role.getName()})
            );
        }catch (EntityNotFoundException e){
            return new ErrorResult(
                    messageService.getMessage("role.not.found.message", null)
            );
        }catch (Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    private static RoleDto convertRoleToRoleDto(Role role){
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .users(role.getUsers().stream().map(User::getEmail).collect(Collectors.toList()))
                .build();
    }
}
