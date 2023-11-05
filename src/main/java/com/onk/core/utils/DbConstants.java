package com.onk.core.utils;

import java.util.Arrays;
import java.util.List;

public class DbConstants {

    //BASE MODEL
    public static final String tableUpdatedDate = "updated_date";
    public static final String tableCreatedDate = "created_date";
    public static final String tableId = "id";
    public static final String offSet = "+03:00";

    // USER TABLE
    public static final String userTableName = "users";
    public static final String userEmail = "email";
    public static final String userName = "name";
    public static final String userLastName = "lastName";
    public static final String userPassword = "password";
    public static final String userIsActive = "isActive";
    public static final String userIsDeleted = "isDeleted";
    public static final String userForCancel = "forCancel";
    public static final String userAddressColumnName = "address_id";
    //USER_ROLE TABLE
    public static final String userRoleTableName = "user_roles";
    public static final String userRoleTableNameUserId = "user_id";
    public static final String userRoleTableNameRoleId = "role_id";
    // ROLE TABLE
    public static final String roleTableName = "roles";
    public static final String roleTableColumnName = "name";
    public static final String roleTableColumnDescription = "description";
    // ADDRESS TABLE
    public static final String addressTableName = "addresses";
    public static final String addressCountryColumnName = "country";
    public static final String addressCityColumnName = "city";
    public static final String addressCountyColumnName = "county";
    public static final String addressNeighbourhoodColumnName = "neighbourhood";
    public static final String addressStreetColumnName = "street";
    public static final String addressDetailColumnName = "detail";
    public static final String addressZipCodeColumnName = "zipCode";
    public static final String addressUserColumnName = "user_id";
    // PRODUCT
    public static final String productTableName = "products";
    public static final String productNameColumnName = "name";
    public static final String productTitleColumnName = "title";
    public static final String productDescriptionColumnName = "description";
    public static final String productQuantityColumnName = "quantity";
    public static final String productPriceColumnName = "price";
    public static final String productPurchasePriceColumnName = "purchase_price";
    public static final String productCurrencyColumnName = "currency";
    public static final String productCategoryColumnName = "category";
    public static final String productIsPublishedColumnName = "is_published";
    public static final String mappedProductImage = "product";
    // PRODUCT_IMAGES
    public static final String productImagesTableName = "product_images";
    public static final String productImagesProductColumnName = "product_id";
    public static final String productImagesImageColumnName = "image_id";


    // CATEGORY
    public static final String categoryTableName = "category";
    public static final String categoryNameColumnName = "name";
    public static final String categoryDescriptionColumnName = "description";
    public static final String categoryIsActiveColumnName = "is_active";
    public static final String categoryParentCategoryColumnName = "parent_category_id";
    public static final String mappedCategoryProduct = "category";
    // IMAGE
    public static final String imageTableName = "images";
    public static final String imagePathName = "path";
    //OTHER CONSTANTS
    public static final int minPasswordSize = 8;
    public static final int textShortSize = 30;
    public static final int textTallSize = 50;
    public static final int textGrandeSize = 90;
    public static final int textVentiSize = 250;
    public static final int textLongContentSize=500;
    public static final int currencyPrecisionSize = 4;
    public static final int currencyLengthSize = 10;
    public static final int image_count = 10;
    // FİLE CONSTANTS
    public static final List<String> allowedFileExtension = Arrays.asList("jpeg", "jpg", "gif", "png");
    public static final List<String> allowedVideoFileExtension = Arrays.asList("avi", "mpeg", "3gp", "mp4", "mov");
    public static final List<String> allowedMimeTypeExtension = Arrays.asList("image/jpeg", "image/gif", "image/png", "video/x-msvideo", "video/mpeg", "video/3gpp", "video/mp4", "video/quicktime");

    // RESET PASSWORD TOKEN
    public static final String resetPasswordTokenTableName = "reset_password_token";
    public static final String resetPasswordTokenUserColumnName = "user_id";
    public static final String resetPasswordTokenExpiryDateColumnName = "expiry_date";



}