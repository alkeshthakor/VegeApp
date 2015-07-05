package com.cb.vmss.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.cb.vmss.database.VegAppDatabase.VegAppColumn;
import com.cb.vmss.model.CartItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class VegAppDatabaseHelper
{
    private DatabaseHelper dataHelper;
    private SQLiteDatabase mDb;

    // Database Path
     private static String DATABASE_PATH = "/data/data/com.cb.vmss/databases/";

    // Database Name
    public static final String DATABASE_NAME = "VegAppDatabase.db";

    // Database Version
    public static final int DATABASE_VERSION = 1;

    public boolean isOpen = false;

    private static String TAG = "DbManager";

    public VegAppDatabaseHelper(Context ctx)
    {
        dataHelper = new DatabaseHelper(ctx);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper
    {
        private Context context = null;

        private boolean checkDataBase()
        {
            File f = new File(DATABASE_PATH + DATABASE_NAME);
            return f.exists();
        }

        // Copy Database into package from Assets Folder
        private void copyDataBase() throws IOException
        {
            try
            {
                // Open your local db as the input stream
                InputStream myInput = context.getAssets().open(DATABASE_NAME);
                // Path to the just created empty db
                String outFileName = DATABASE_PATH + DATABASE_NAME;
                OutputStream myOutput = new FileOutputStream(outFileName);
                // transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0)
                {
                    myOutput.write(buffer, 0, length);
                }
                // Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }

        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            String CREATE_CART_MASTER_TABLE = "CREATE TABLE " + VegAppColumn.CART_MASTER_TABLE + "(" 
                    + VegAppColumn.CART_ID + " INTEGER PRIMARY KEY," 
            		+ VegAppColumn.CART_PRODUCT_ID + " TEXT," 
            		+ VegAppColumn.CART_PRODUCT_NAME + " TEXT," 
            		+ VegAppColumn.CART_PRODUCT_IMAGE_URL + " TEXT,"
                    + VegAppColumn.CART_PRODUCT_MAIN_PRICE + " TEXT," 
            		+ VegAppColumn.CART_PRODUCT_DISPLAY_PRICE + " TEXT," 
                    + VegAppColumn.CART_PRODUCT_UNIT_ID + " TEXT,"
                    + VegAppColumn.CART_PRODUCT_UNIT_KEY + " TEXT, " 
                    + VegAppColumn.CART_PRODUCT_UNIT_VALUE + " TEXT,"
                    + VegAppColumn.CART_PRODUCT_QTY + " TEXT,"
                    + VegAppColumn.CART_PRODUCT_SUB_TOTAL + " TEXT,"
                    + VegAppColumn.CART_PRODUCT_CAT_ID + " TEXT, "
                    + VegAppColumn.CART_PRODUCT_CAT_NAME + " TEXT" + ")";
            
            db.execSQL(CREATE_CART_MASTER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w("DBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            onCreate(db);
        }
    }

    // Open Database
    public VegAppDatabaseHelper open() throws SQLException
    {
        try
        {
            boolean isExist = dataHelper.checkDataBase();
            if (!isExist)
            {
                mDb = dataHelper.getWritableDatabase();
                dataHelper.copyDataBase();
                if (mDb.isOpen())
                {
                    mDb.close();
                }
            }
            mDb = dataHelper.getWritableDatabase();
            isOpen = true;
        }
        catch (Exception e)
        {
            isOpen = false;
        }
        return this;
    }

    // Close Database
    public void close()
    {
        try
        {
            mDb.close();
            Log.e("Data base close success fully=======>>", "Data base close success fully");
        }
        catch (Exception e)
        {
            Log.e("Exception is close data base=======>>", e.toString());
        }
    }

    public void clearDatabase()
    {
        mDb.delete(VegAppColumn.CART_MASTER_TABLE, null, null);
    }

    public void addInToMyCart(ContentValues insertValues)
    {
        long rowid = 0;
        try
        {    
           rowid = mDb.insert(VegAppColumn.CART_MASTER_TABLE, null, insertValues);
            Log.d(TAG, "Inseert in database: " + insertValues + "Row id:" + rowid);
        }
        catch (Exception e)
        {
            Log.e("Error: ", "e.getMessage() :" + e.getMessage());
        }
    }

    public int updateMyCart(String productId,ContentValues updateValues)
    {
        String args[] = { productId.toString() };
        int rowid=mDb.update(VegAppColumn.CART_MASTER_TABLE, updateValues, VegAppColumn.CART_PRODUCT_ID+ "=?", args);
        return rowid;
    }
    
    public long deleteMyCartItem(String productId)
    {
        String args[] = { productId.toString() };
        long rowid = mDb.delete(VegAppColumn.CART_MASTER_TABLE, VegAppColumn.CART_PRODUCT_ID + "=?", args);
        return rowid;        
        
    }
    

    
    public int getCartProductQty(String productId)
    {
    	Cursor cartCur = null;
        String args[] = { productId.toString() };
        cartCur=mDb.query(VegAppColumn.CART_MASTER_TABLE,new String[]{VegAppColumn.CART_PRODUCT_QTY}, VegAppColumn.CART_PRODUCT_ID+"=?", args, null,null,null);
        
        if(cartCur.getCount()>0){
        	cartCur.moveToFirst();
        	return Integer.parseInt(cartCur.getString(0));
        }else{
        	return -1;
        }

    }
    
    
    public List<CartItem> getMyCartList()
    {
        Cursor cartCursor = null;
        List<CartItem> myCartList = new ArrayList<CartItem>();
        
        try
        {
            String str = "SELECT * from "+VegAppColumn.CART_MASTER_TABLE;
            cartCursor = mDb.rawQuery(str, null);
            cartCursor.moveToFirst();
            if (cartCursor != null && cartCursor.getCount() > 0)
            {
                while (!cartCursor.isAfterLast())
                {
                   
                	CartItem mCartItem=new CartItem();
                	
                	mCartItem.setCartId(cartCursor.getString(0));
                	mCartItem.setCartProductId(cartCursor.getString(0));
                	mCartItem.setCartProductCatName(cartCursor.getString(0));
                	mCartItem.setCartProductImageUrl(cartCursor.getString(0));
                	mCartItem.setCartProductMainPrice(cartCursor.getString(0));
                	mCartItem.setCartProductDisplayPrice(cartCursor.getString(0));
                	
                	mCartItem.setCartProductUnitId(cartCursor.getString(0));
                	mCartItem.setCartProductUnitKey(cartCursor.getString(0));
                	mCartItem.setCartProductUnitValue(cartCursor.getString(0));
                	
                	mCartItem.setCartProductCatId(cartCursor.getString(0));
                	mCartItem.setCartProductCatName(cartCursor.getString(0));
                	mCartItem.setCartProductQty(cartCursor.getString(0));
                	mCartItem.setCartProductSubTotal(cartCursor.getString(0));

                    cartCursor.moveToNext();
                    
                }
            }
        }
        catch (Exception e)
        {
            Log.e("Error: ", "e.getMessage() :" + e.getMessage());
        }
        return myCartList;
    } 
}
