package com.onk.api.admin;

import com.onk.core.fileSystem.StorageService;
import com.onk.core.utils.RouteConstants;
import com.onk.security.request.UserRequest;
import com.onk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(RouteConstants.adminBaseRoute)
public class AdminController {

    private final UserService userService;

    private final StorageService storageService;

    @PostMapping(value = RouteConstants.userCreateRoute)
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(this.userService.createUser(userRequest));
    }

    @GetMapping(value = RouteConstants.userFindByEmailRoute)
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(this.userService.getUser(email));
    }

    @GetMapping(value = RouteConstants.userFindByIdRoute)
    public ResponseEntity<?> getUserById(@RequestParam("id") Long id){
        return ResponseEntity.ok(this.userService.findById(id));
    }
    @GetMapping(value = RouteConstants.userFindByNameRoute)
    public ResponseEntity<?> getUserByName(@RequestParam("name") String name){
        return ResponseEntity.ok(this.userService.getUserByName(name));
    }
    @GetMapping(value = RouteConstants.userFindByLastNameRoute)
    public ResponseEntity<?> getUserByLastName(@RequestParam("lastName") String lastName){
        return ResponseEntity.ok(lastName);
    }
    @GetMapping(value = RouteConstants.userFindAllAddressRoute)
    public ResponseEntity<?> getAddressesByUserId(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.userService.getUserAddresses(id));
    }
    @GetMapping(value = RouteConstants.userFindByNameAndLastNameRoute)
    public ResponseEntity<?> getUserByNameAndLastName(@RequestParam("name") String name, @RequestParam("lastName") String lastName){
        return ResponseEntity.ok(this.userService.getUserByNameAndLastName(name, lastName));
    }

    @GetMapping(value = RouteConstants.userFindAllActiveUsersRoute)
    public ResponseEntity<?> getAllActiveUsers(){
        return ResponseEntity.ok(this.userService.getUsersByIsDeletedFalse());
    }

    @GetMapping(value = RouteConstants.userFindRolesRoute)
    public ResponseEntity<?> getAuthorities(@PathVariable Long id){
        return ResponseEntity.ok(this.userService.authorities(id));
    }

    @GetMapping(RouteConstants.fileFindAllRoute)
    public ResponseEntity<?> listUploadedFiles(){
        return ResponseEntity.ok(this.storageService.loadAll());
    }

    @GetMapping(RouteConstants.fileDownloadRoute)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        if(file.exists() && file.isReadable()){
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(RouteConstants.fileUploadRoute)
    public ResponseEntity<?> uploadFile(@Valid @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(this.storageService.store(file));
    }

    @GetMapping(RouteConstants.fileFindRoute)
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        return ResponseEntity.ok(this.storageService.load(filename));
    }

}
