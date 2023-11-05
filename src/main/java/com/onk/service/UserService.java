package com.onk.service;

import com.onk.core.results.DataResult;
import com.onk.core.results.Result;
import com.onk.dto.AddressDto;
import com.onk.dto.UserDto;
import com.onk.model.User;
import com.onk.security.request.UserRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService {


    UserDto createUser(UserRequest userRequest);

    DataResult<UserDto> getUser(String email);

    User findUserByUsername(String username);

    DataResult<List<UserDto>> getUserByName(String name);

    DataResult<List<UserDto>> getUserByNameAndLastName(String name, String lastName);

    DataResult<List<UserDto>> getUsers();

    DataResult<List<UserDto>> getDeletedUsers();

    DataResult<List<UserDto>> getUsersByIsDeletedFalse();

    Result addAddressToUser(AddressDto addressDto);

    DataResult<List<AddressDto>> getAddresses();

    DataResult<List<AddressDto>> getUserAddresses(Long userId);

    DataResult<UserDto> findById(Long id);

    Result changeUserPassword(String password);

    DataResult<List<? extends GrantedAuthority>> authorities(Long id);

    DataResult<List<? extends GrantedAuthority>> getUserRole();

    Result updateUser(UserDto userDto);

    Result deleteOwnUser();

    Result activation(String token, Long id);

    Result deleteUser(Long id);

    Result addRoleToUser(Long userId, Long roleId);

    Result deleteRoleFromUser(Long userId, Long roleId);
}
