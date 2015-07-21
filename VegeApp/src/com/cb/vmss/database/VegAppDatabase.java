package com.cb.vmss.database;

import android.provider.BaseColumns;

public class VegAppDatabase 
{
    // Recording Master Table
    public static final class VegAppColumn implements BaseColumns
    {
        public static final String CART_MASTER_TABLE = "cartmaster";
        public static final String CART_ID = "cart_id";
        public static final String CART_PRODUCT_ID = "cproduct_id";
        public static final String CART_PRODUCT_NAME = "cproduct_name";
        public static final String CART_PRODUCT_IMAGE_URL = "cproduct_image_url";
        public static final String CART_PRODUCT_MAIN_PRICE = "cproduct_mprice";
        public static final String CART_PRODUCT_DISPLAY_PRICE = "cprodcut_display_price";
        public static final String CART_PRODUCT_UNIT_ID = "cproduct_unit_id";
        public static final String CART_PRODUCT_UNIT_KEY = "cproduct_unit_key";
        public static final String CART_PRODUCT_UNIT_VALUE = "cproduct_unit_value";
        public static final String CART_PRODUCT_CAT_ID = "cproduct_cat_id";
        public static final String CART_PRODUCT_CAT_NAME = "cproduct_catName";
        public static final String CART_PRODUCT_QTY = "cproduct_qty";
        public static final String CART_PRODUCT_SUB_TOTAL = "cproduct_sub_total";
        
        
        
        public static final String NOTIFICATION_MASTER_TABLE = "notification_master";
        public static final String NOTI_ID = "noti_id";
        public static final String NOTI_TITLE = "noti_title";
        public static final String NOTI_MESSAGE = "noti_message";
        public static final String NOTI_PROMOCODE = "noti_promocode";
        public static final String NOTI_FROM = "noti_from";
        public static final String NOTI_DATE = "noti_date";
        
        
    }
}



