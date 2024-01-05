package com.onk.core.utils;

public class RouteConstants {

    // AUTHENTİCATION
    public static final String authBaseRoute = "/api/v1/auth";
    public static final String authRegisterRoute = "/register";
    public static final String authLoginRoute = "/login";
    public static final String authActivationRoute=  "/{id}/activation";
    public static final String authRefreshTokenRoute = "/refresh-token";
    public static final String authLogoutRoute = "/api/v1/user/logout";

    // ROOT ROLE
    public static final String requiredRootAuthority = "hasAuthority('ROLE_ROOT')";
    // =========== users ================================== //
    public static final String primaryRoleBaseRoute = "/api/v1/manager";
    public static final String userDeleteRoute = "/users/{id}/delete";
    public static final String userAddRoleRoute = "/users/{id}/add-role";
    public static final String userDeleteRoleRoute = "/users/{id}/delete-role";
    public static final String userFindAllRoute = "/users/all";
    public static final String userFindAllDeletedUsersRoute = "/users/deleted-users";
    // ============ roles ================================ //
    public static final String roleFindByNameRoute = "/roles/search-by-name";
    public static final String roleFindByIdRoute = "/roles/search-by-id";
    public static final String roleCreateRoute = "/roles/create";
    public static final String roleFindAllRoute = "/roles/all";
    public static final String usersHasRoleRoute = "/roles/users";
    public static final String roleUpdateRoute = "/roles/{id}/update";
    public static final String roleDeleteRoute = "/roles/{id}/delete";

    // ADMIN ROLE
    public static final String requiredAdminAuthority = "hasAuthority('ROLE_ADMIN')";
    public static final String secondaryRoleBaseRoute = "/api/v1/admin";
    public static final String userCreateRoute = "/users/create";
    public static final String userFindByEmailRoute = "/users/search-by-email";
    public static final String userFindByIdRoute = "/users/search-by-id";
    public static final String userFindByNameRoute = "/users/search-by-name";
    public static final String userFindByLastNameRoute = "/users/search-by-lastname";
    public static final String userFindByNameAndLastNameRoute = "users/search-by-name-and-lastname";
    public static final String userFindAllActiveUsersRoute = "/users/active-users";
    public static final String userFindRolesRoute = "/users/{id}/roles";
    public static final String userFindAllAddressRoute = "/users/{id}/addresses";
    // ============ categories ============================ //
    public static final String categoryBaseRoute = "/categories";
    public static final String categoryCreateRoute = "/create";
    public static final String categoryFindAllRoute = "/all-categories";
    public static final String categoryChangeActivationRoute = "/{id}/change-activation";
    public static final String categoryFindAllActiveRoute = "/active-categories";
    public static final String categoryFindAllNotActiveRoute = "/not-active-categories";
    public static final String categoryFindSubCategoriesRoute = "/{id}/sub-categories";
    public static final String categoryCreateSubCategory = "/add-sub-category";
    public static final String categoryDeleteRoute = "/{id}/delete";
    public static final String categoryFindByIdRoute = "/{id}";
    public static final String categoryFindByNameRoute = "/search-by-name";
    public static final String categoryChangeParentCategoryRoute = "/change-parent-category";
    // ============ products ============================== //
    public static final String productRoute = "/products";
    public static final String productFindAllRoute = "/all-products";
    public static final String productFindByIdRoute = "/search-by-id";
    public static final String productFindByNameRoute = "/search-by-name";
    public static final String productFindByTitleRoute = "/search-by-title";
    public static final String productChangePublishRoute = "/{id}/change-product-publish";
    public static final String productCreateRoute = "/create";
    public static final String productUpdateRoute = "/{id}/update";
    public static final String productAddImageRoute = "{id}/add-images";
    public static final String productDeleteRoute = "{id}/delete";
    public static final String productFindAllIsPublishedRoute = "/all-published-products";
    public static final String productFindAllNotPublishedRoute = "/all-not-published-products";
    public static final String productFindByCategoryNameRoute = "/category/{id}/products";
    public static final String productDeleteImageRoute = "{id}/images/delete";





    // ============ files ================================= //
    public static final String fileFindAllRoute = "files/all-files";
    public static final String fileDownloadRoute = "files/{filename:.+}";
    public static final String fileUploadRoute = "files/create";
    public static final String fileFindRoute = "/files{filename}";


    // USER ROLE
    public static final String requiredUserAuthority = "hasAuthority('ROLE_USER')";
    public static final String userBaseRoute = "/api/v1/user";
    public static final String userProfileRoute = "/api/v1/user/profile";
    public static final String userSettingsChangePassword = "/settings/change-password";
    public static final String userSettingsCancelRegister = "/settings/cancel-register";
    public static final String userSettingsUpdateRoute = "/settings/update-user";
    public static final String userOwnRolesRoute = "/settings/roles";
    public static final String userSettingsUploadProfileImageRoute = "/settings/upload/profile-image";
    public static final String userAddAddressRoute = "/add-address";
    public static final String userFindAllAddress = "/addresses";

}
