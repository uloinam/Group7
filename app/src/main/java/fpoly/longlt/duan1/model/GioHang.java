package fpoly.longlt.duan1.model;

import android.os.Parcelable;

public class GioHang {
    int chitietsp_id;

    public int getChitietsp_id() {
        return chitietsp_id;
    }

    public void setChitietsp_id(int chitietsp_id) {
        this.chitietsp_id = chitietsp_id;
    }

    private int user_id;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public GioHang(int user_id, int cart_id, int sp_id, int quantity, int price, int total_price) {
        this.user_id = user_id;
        this.cart_id = cart_id;
        this.sp_id = sp_id;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
    }

    private int cart_id;
    private int sp_id;
    private int quantity;
    private int price;
    private int total_price;
    private String imgPath;
    private String color;
    private String size;

    public GioHang(int user_id, int cart_id, int sp_id, int quantity, int price, int total_price, String imgPath, String color, String size) {
        this.user_id = user_id;
        this.cart_id = cart_id;
        this.sp_id = sp_id;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
        this.imgPath = imgPath;
        this.color = color;
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImgPath() {
        return imgPath;
    }

    public GioHang(int user_id, int cart_id, int sp_id, int quantity, int price, int total_price, String imgPath) {
        this.user_id = user_id;
        this.cart_id = cart_id;
        this.sp_id = sp_id;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
        this.imgPath = imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public GioHang() {
    }

    public GioHang(int user_id, int sp_id, int quantity, int price, int total_price) {
        this.user_id = user_id;
        this.sp_id = sp_id;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSp_id() {
        return sp_id;
    }

    public void setSp_id(int sp_id) {
        this.sp_id = sp_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
