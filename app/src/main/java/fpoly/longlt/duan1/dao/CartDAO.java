package fpoly.longlt.duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;

import fpoly.longlt.duan1.database.DBHelper;
import fpoly.longlt.duan1.model.GioHang;
import fpoly.longlt.duan1.model.SanPham;

public class CartDAO {
    private DBHelper fileHelper;
    private SQLiteDatabase db;

    public CartDAO(Context context){
        fileHelper = new DBHelper(context);
        db = fileHelper.getWritableDatabase();
    }

    public boolean addToCart(int user_id, int sp_id, int chitietsp_id, int quantity, int price, String imgPath, String color, String size) {
        // Truy vấn kiểm tra sản phẩm đã tồn tại trong giỏ hàng hay chưa (dựa trên user_id, sp_id, color, size)
        Cursor cursor = db.rawQuery(
                "SELECT quantity, price FROM cart WHERE user_id = ? AND sp_id = ? AND color = ? AND size = ?",
                new String[]{String.valueOf(user_id), String.valueOf(sp_id), color, size});

        boolean result = false;

        if (cursor.moveToFirst()) {
            // Nếu sản phẩm đã tồn tại, tăng số lượng và cập nhật tổng tiền
            int currentQuantity = cursor.getInt(0); // Lấy số lượng hiện tại
            int newQuantity = currentQuantity + quantity; // Tăng số lượng
            int newTotalPrice = newQuantity * price; // Tổng giá mới

            // Cập nhật số lượng và tổng tiền trong cơ sở dữ liệu
            ContentValues values = new ContentValues();
            values.put("quantity", newQuantity);
            values.put("total_price", newTotalPrice);

            int rowsUpdated = db.update(
                    "cart",
                    values,
                    "user_id = ? AND sp_id = ? AND color = ? AND size = ?",
                    new String[]{String.valueOf(user_id), String.valueOf(sp_id), color, size});
            result = rowsUpdated > 0;
        } else {
            // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới vào giỏ hàng
            ContentValues values = new ContentValues();
            values.put("user_id", user_id);
            values.put("sp_id", sp_id);
            values.put("chitietsp_id", chitietsp_id);
            values.put("quantity", quantity);
            values.put("price", price);
            values.put("total_price", price * quantity);
            values.put("img_path", imgPath);
            values.put("color", color);
            values.put("size", size);
            long rowInserted = db.insert("cart", null, values); // Thực hiện thêm vào DB
            result = rowInserted != -1;
        }

        cursor.close(); // Đảm bảo đóng con trỏ để tránh rò rỉ bộ nhớ
        return result; // Trả về true nếu thành công, false nếu thất bại
    }


    public boolean delete(int user_id, int sp_id){
        long check = db.delete("cart", "user_id = ? AND sp_id = ?",
                new String[]{String.valueOf(user_id), String.valueOf(sp_id)});
        return check != -1;
    }

    public ArrayList<GioHang> getAll(int user_id) {
        ArrayList<GioHang> arrayList = new ArrayList<>();

        // Truy vấn SQL với điều kiện WHERE
        Cursor cursor = db.rawQuery("SELECT * FROM cart WHERE user_id = ?",
                new String[]{String.valueOf(user_id)});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst(); // Di chuyển con trỏ đến dòng đầu tiên
            do {
                // Tạo đối tượng GioHang và gán giá trị
                GioHang gioHang = new GioHang();
                gioHang.setCart_id(cursor.getInt(0));
                gioHang.setUser_id(cursor.getInt(1));
                gioHang.setSp_id(cursor.getInt(2));
                gioHang.setChitietsp_id(cursor.getInt(3));
                gioHang.setQuantity(cursor.getInt(4));
                gioHang.setPrice(cursor.getInt(5));
                gioHang.setTotal_price(cursor.getInt(6));
                gioHang.setImgPath(cursor.getString(7));
                gioHang.setColor(cursor.getString(8));
                gioHang.setSize(cursor.getString(9));
                gioHang.setStatus(cursor.getInt(10));


                // Thêm vào danh sách
                arrayList.add(gioHang);
            } while (cursor.moveToNext()); // Lặp qua các dòng tiếp theo
        }

        return arrayList; // Trả về danh sách giỏ hàng
    }
    public SanPham getSanPhamById(int sp_id) {
        SanPham sanPham = null;

        // Truy vấn bảng sanpham để lấy thông tin sản phẩm
        Cursor cursor = db.query("sanpham", null, "sp_id = ?", new String[]{String.valueOf(sp_id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Khởi tạo đối tượng SanPham và gán giá trị từ cursor
            sanPham = new SanPham();
            sanPham.setSpId(cursor.getInt(0));
            sanPham.setTenSp(cursor.getString(1));
            sanPham.setImg(cursor.getString(2));
            sanPham.setStatus(cursor.getInt(3));
            sanPham.setPrice(cursor.getInt(4));
            sanPham.setDescription(cursor.getString(5));

            cursor.close();  // Đảm bảo đóng cursor sau khi sử dụng
        }

        return sanPham;
    }

    public SanPham getSanPhamChiTiet(int sp_id) {
        SanPham sanPham = getSanPhamById(sp_id);  // Giả sử bạn đã có phương thức lấy thông tin sản phẩm cơ bản từ bảng sanpham

        if (sanPham != null) {
            // Truy vấn chi tiết sản phẩm từ bảng chitietsp
            Cursor cursorChiTiet = db.query("chitietsp", null, "sp_id = ?", new String[]{String.valueOf(sp_id)}, null, null, null);

            if (cursorChiTiet != null && cursorChiTiet.moveToFirst()) {
                // Lấy thông tin chi tiết và gán vào đối tượng SanPham
                sanPham.setSize(cursorChiTiet.getString(2));
                sanPham.setColors(cursorChiTiet.getString(3));
                sanPham.setSoLuong(cursorChiTiet.getInt(4));
                cursorChiTiet.close();  // Đảm bảo đóng cursor
            }
        }

        return sanPham;
    }
    public boolean updateQuantity(SanPham sanPham) {
        ContentValues values = new ContentValues();
        values.put("quantity", sanPham.getSoLuong());
        values.put("total_price", sanPham.getSoLuong() * sanPham.getPrice());

        int result = db.update("cart", values, "sp_id = ? AND color = ? AND size = ?",
                new String[]{String.valueOf(sanPham.getSpId()), sanPham.getColors(), sanPham.getSize()});
        return result > 0; // Trả về true nếu cập nhật thành công
    }


    public boolean deleteProduct(int cart_id) {
        int result = db.delete("cart", "cart_id = ?", new String[]{String.valueOf(cart_id)});
        return result!=-1;
    }

    public void updateStatus(int sp_id, int status) {
        ContentValues values = new ContentValues();
        values.put("status", status);  // Cập nhật trạng thái
        db.update("cart", values, "sp_id = ?", new String[]{String.valueOf(sp_id)});
    }




}
