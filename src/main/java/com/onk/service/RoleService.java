package com.onk.service;

import com.onk.core.results.DataResult;
import com.onk.core.results.Result;
import com.onk.dto.RoleDto;
import com.onk.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface RoleService {

    Result addRole(RoleDto roleDto);

    DataResult<RoleDto> getRoleByName(String name) throws EntityNotFoundException;

    DataResult<RoleDto> getRoleById(Long id);

    DataResult<List<RoleDto>> getAllRole();

    DataResult<List<UserDto>> getUsersByRoleName(String name);

    Result deleteRole(Long id);

}
