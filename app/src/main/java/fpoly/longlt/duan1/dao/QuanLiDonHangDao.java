package fpoly.longlt.duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fpoly.longlt.duan1.database.DBHelper;
import fpoly.longlt.duan1.model.DonHang;
import fpoly.longlt.duan1.model.DonHangChiTiet;
import fpoly.longlt.duan1.model.GioHang;

public class QuanLiDonHangDao {
    DBHelper dbHelper;
    SQLiteDatabase database;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public QuanLiDonHangDao(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }
    public ArrayList<DonHang> getAllDonHang(){
        ArrayList<DonHang> lst = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from bills", null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                DonHang donHang = new DonHang();
                donHang.setOd_id(cursor.getInt(0));
                donHang.setUser_id(cursor.getInt(1));
                donHang.setVc_id(cursor.getInt(2));
                try {
                    donHang.setOd_date(sdf.parse(cursor.getString(3)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                donHang.setTotal_price(cursor.getInt(4));
                donHang.setStatus(cursor.getInt(5));
                lst.add(donHang);
            } while (cursor.moveToNext());
        }
        return lst;
    }

//    public boolean createBillAndBillDetail(DonHang donHang, int chitietsp_id, int quantity, int price){
//        try {
//            ContentValues values = new ContentValues();
//            values.put("user_id",donHang.getUser_id());
//            values.put("od_date",sdf.format(donHang.getOd_date()));
//            values.put("total_price",donHang.getTotal_price());
//            values.put("status",donHang.getStatus());
//            long check = database.insert("bills", null, values);
//            if (check!=-1){
//                throw new Exception("Thêm không thành công");
//            }
//            for (DonHangChiTiet chiTiet : ){
//
//            }
//        } catch (Exception e){
//
//        }
//    }
    public boolean createBill(DonHang donHang, int chitietsp_id, int quantity, int price){
        ContentValues values = new ContentValues();
        values.put("user_id",donHang.getUser_id());
        values.put("od_date",sdf.format(donHang.getOd_date()));
        values.put("total_price",donHang.getTotal_price());
        values.put("status",donHang.getStatus());
        long check = database.insert("bills", null, values);
        if (check!=-1){
            ContentValues values1 = new ContentValues();
            values1.put("od_id",check);
            values1.put("chitietsp_id",chitietsp_id);
            values1.put("quantity",quantity);
            values1.put("price",price);
            long oddetail = database.insert("orderdetail", null, values1);
            return oddetail!=-1;
        }
        return false;
    }
    public int getIdSPDetail(String color, String size, int sp_id){
        int id = 0;
        Cursor cursor = database.rawQuery("select chitietsp_id from chitietsp where color = ? and size = ? and sp_id = ?", new String[]{color, size, String.valueOf(sp_id)});
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        return id;
    }
    public boolean createBillDetail(int od_id, int chitietsp_id, int quantity, int price){
        ContentValues values = new ContentValues();
        values.put("od_id",od_id);
        values.put("chitietsp_id",chitietsp_id);
        values.put("quantity",quantity);
        values.put("price",price);
        long check = database.insert("orderdetail", null, values);
        return check!=-1;
    }
    public ArrayList<DonHangChiTiet> getDonHangChiTiet(int od_id){
        ArrayList<DonHangChiTiet> lst = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from orderdetail where od_id = ?", new String[]{String.valueOf(od_id)});
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                DonHangChiTiet donHangChiTiet = new DonHangChiTiet();
                donHangChiTiet.setOddetail_id(cursor.getInt(0));
                donHangChiTiet.setOd_id(cursor.getInt(1));
                donHangChiTiet.setChitietsp_id(cursor.getInt(2));
                donHangChiTiet.setQuantity(cursor.getInt(3));
                donHangChiTiet.setPrice(cursor.getInt(4));
                lst.add(donHangChiTiet);
            } while (cursor.moveToNext());
        }
        return lst;
    }
//    String createOrderDetail = "CREATE TABLE orderdetail(" +
//            "orderdetail INTEGER PRIMARY KEY AUTOINCREMENT," +
//            "od_id INTEGER REFERENCES bills," +
//            "chitietsp_id INTEGER REFERENCES sanpham," +
//            "quantity INTEGER," +
//            "price INTEGER)";
//        db.execSQL(createOrderDetail);
//    od_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//            "user_id INTEGER REFERENCES user," +
//            "vc_id INTEGER REFERENCES voucher," +
////                "chitietsp_id INTEGER REFERENCES chitietsp," +
////                "oddetail_id INTEGER REFERENCES orderdetail," +
////                "oddetail_id INTEGER REFERENCES orderdetail," +
//            "od_date DATE NOT NULL," +
//            "total_price integer," +
//            "status INTEGER)";
    public String getNameKH(int id){
        String name = null;
        Cursor cursor = database.rawQuery("select u.name from bills b " +
                "join user u on b.user_id = u.user_id " +
                "where u.user_id = ?",new String[]{String.valueOf(id)});
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            name = cursor.getString(0);
        }
        return name;
    }

    public boolean updateStatusDonHang(int id, int status){
        ContentValues values = new ContentValues();
        values.put("status", status);
        int check = database.update("bills", values, "od_id = ?", new String[]{String.valueOf(id)});
        return check!=-1;
    }
    public int getQuantity(int id){
        int quantity = 0;
        Cursor cursor = database.rawQuery("select sum(quantity) from orderdetail where od_id = ? group by od_id", new String[]{String.valueOf(id)});
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            quantity = cursor.getInt(0);
        }
        return quantity;
    }
//    public long createOrder(int userId, List<GioHang> items, int totalPrice, int discount, int shippingFee, int finalPrice, String paymentMethod) {
//
//        ContentValues orderValues = new ContentValues();
//        orderValues.put("user_id", userId);
//        orderValues.put("od_date", System.currentTimeMillis()); // Giờ đặt đơn
//        orderValues.put("total_price", finalPrice);
//        orderValues.put("status", 0); // 0: Đang chờ xác nhận
//        long orderId = database.insert("bills", null, orderValues);
//
//        if (orderId > 0) {
//            for (GioHang item : items) {
//                ContentValues detailValues = new ContentValues();
//                detailValues.put("od_id", orderId);
//                detailValues.put("chitietsp_id", item.getSp_id());
//                detailValues.put("quantity", item.getQuantity());
//                detailValues.put("price", item.getPrice());
//                database.insert("orderdetail", null, detailValues);
//            }
//        }
//        return orderId;
//    }
//    public boolean placeOrder(List<GioHang> items, int discount, String paymentMethod) {
//        database.beginTransaction();
//        try {
//            int totalPrice = 0;
//            for (GioHang item : items) {
//                totalPrice += item.getQuantity() * item.getPrice();
//            }
//            int finalPrice = totalPrice - discount;
//
//            ContentValues billValues = new ContentValues();
//            billValues.put("user_id", getCurrentUserId());
////            billValues.put("vc_id", getVoucherId(voucherCode));
//            billValues.put("od_date", getCurrentDate());
//            billValues.put("total_price", finalPrice);
//            billValues.put("status", 0); // Chờ xác nhận
//            long billId = database.insert("bills", null, billValues);
//
//            for (GioHang item : items) {
//                ContentValues detailValues = new ContentValues();
//                detailValues.put("od_id", billId);
//                detailValues.put("chitietsp_id", item.getSp_id());
//                detailValues.put("quantity", item.getQuantity());
//                detailValues.put("price", item.getPrice());
//                database.insert("orderdetail", null, detailValues);
//            }
//
//            database.setTransactionSuccessful();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            database.endTransaction();
//        }
//    }
//    "od_id integer primary key autoincrement," +
//            "user_id integer references user," +
//            "od_date date not null," +
//            "total_price integer," +
//            "status integer)";

//     "user_id integer primary key autoincrement," +
//             "username text," +
//             "password text," +
//             "name text," +
//             "sdt text," +
//             "address text default 'ha noi'," +
//             "moneyonl integer default 0," +
//             "role integer default 0," +
//             "status bit default 1," +
//             "imgavatar text)";
}
