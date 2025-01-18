package fpoly.longlt.duan1.model;

public class DonHangChiTiet {
//    "oddetail_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//        "od_id INTEGER REFERENCES bills," +
//        "chitietsp_id INTEGER REFERENCES sanpham," +
//        "quantity INTEGER," +
//        "price INTEGER)";
    private int od_id, oddetail_id, chitietsp_id, quantity, price;

    public int getOd_id() {
        return od_id;
    }

    public void setOd_id(int od_id) {
        this.od_id = od_id;
    }

    public int getOddetail_id() {
        return oddetail_id;
    }

    public void setOddetail_id(int oddetail_id) {
        this.oddetail_id = oddetail_id;
    }

    public int getChitietsp_id() {
        return chitietsp_id;
    }

    public void setChitietsp_id(int chitietsp_id) {
        this.chitietsp_id = chitietsp_id;
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

    public DonHangChiTiet() {
    }

    public DonHangChiTiet(int od_id, int oddetail_id, int chitietsp_id, int quantity, int price) {
        this.od_id = od_id;
        this.oddetail_id = oddetail_id;
        this.chitietsp_id = chitietsp_id;
        this.quantity = quantity;
        this.price = price;
    }
}
