package com.onk.api;

import com.onk.core.fileSystem.FileSystemStorageService;
import com.onk.core.utils.RouteConstants;
import com.onk.dto.AddressDto;
import com.onk.dto.ChangePasswordRequest;
import com.onk.dto.UserDto;
import com.onk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteConstants.userBaseRoute)
public class UserController {

    private final UserService userService;

    private final FileSystemStorageService storageService;

    @PostMapping(value = RouteConstants.userSettingsChangePassword)
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.ok(this.userService.changeUserPassword(changePasswordRequest.getPassword()));
    }
    @PostMapping(value = RouteConstants.userSettingsCancelRegister)
    public ResponseEntity<?> userCancelRegister(){
        return ResponseEntity.ok(userService.deleteOwnUser());
    }

    @PostMapping(value = RouteConstants.userSettingsUpdateRoute)
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(this.userService.updateUser(userDto));
    }
    @PostMapping(value = RouteConstants.userAddAddressRoute)
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressDto addressDto){
        return ResponseEntity.ok(this.userService.addAddressToUser(addressDto));
    }
    @GetMapping(value = RouteConstants.userFindAllAddress)
    public ResponseEntity<?> getAddresses(){
        return ResponseEntity.ok(userService.getAddresses());
    }

    @GetMapping(value = RouteConstants.userOwnRolesRoute)
    public ResponseEntity<?> getUserRole(){
        return ResponseEntity.ok(userService.getUserRole());
    }

    @PostMapping(value = RouteConstants.userSettingsUploadProfileImageRoute)
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file")MultipartFile file){
        return ResponseEntity.ok(this.storageService.store(file));
    }
}
