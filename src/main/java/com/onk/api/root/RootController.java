package com.onk.api.root;

import com.onk.core.utils.RouteConstants;
import com.onk.dto.RoleDto;
import com.onk.service.RoleService;
import com.onk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteConstants.primaryRoleBaseRoute)
public class RootController {

    private final UserService userService;

    private final RoleService roleService;

    @PostMapping(value = RouteConstants.userDeleteRoute)
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(this.userService.deleteUser(id));
    }
    @PostMapping(value = RouteConstants.userAddRoleRoute)
    public ResponseEntity<?> addRoleToUser(@PathVariable Long id, @RequestParam("roleId") Long roleId){
        return ResponseEntity.ok(this.userService.addRoleToUser(id, roleId));
    }
    @GetMapping(value = RouteConstants.userFindAllRoute)
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(this.userService.getUsers());
    }
    @GetMapping(value = RouteConstants.userFindAllDeletedUsersRoute)
    public ResponseEntity<?> getDeletedUsers(){
        return ResponseEntity.ok(this.userService.getDeletedUsers());
    }

    @PostMapping(value = RouteConstants.userDeleteRoleRoute)
    public ResponseEntity<?> deleteRoleFromUser(@PathVariable Long id, @RequestParam("roleId") Long roleId){
        return ResponseEntity.ok(this.userService.deleteRoleFromUser(id, roleId));
    }

    @GetMapping(RouteConstants.roleFindByNameRoute)
    public ResponseEntity<?> getRoleByName(@RequestParam("name") String name){
        return ResponseEntity.ok(this.roleService.getRoleByName(name));
    }

    @GetMapping(RouteConstants.roleFindByIdRoute)
    public ResponseEntity<?> getRole(@RequestParam("id") Long id){
        return ResponseEntity.ok(this.roleService.getRoleById(id));

    }

    @PostMapping(RouteConstants.roleCreateRoute)
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDto roleDto){
        return ResponseEntity.ok(this.roleService.addRole(roleDto));
    }

    @GetMapping(RouteConstants.roleFindAllRoute)
    public ResponseEntity<?> allRole(){
        return ResponseEntity.ok(this.roleService.getAllRole());
    }

    @GetMapping(RouteConstants.usersHasRoleRoute)
    public ResponseEntity<?> allUsersByRoleName(@RequestParam("role_name") String name){
        return ResponseEntity.ok(this.roleService.getUsersByRoleName(name));
    }

    @PostMapping(RouteConstants.roleDeleteRoute)
    public ResponseEntity<?> deleteRole(@Valid @PathVariable Long id){
        return ResponseEntity.ok(this.roleService.deleteRole(id));
    }
}
