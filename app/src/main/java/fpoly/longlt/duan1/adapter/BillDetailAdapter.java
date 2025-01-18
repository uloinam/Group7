package fpoly.longlt.duan1.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fpoly.longlt.duan1.R;
import fpoly.longlt.duan1.dao.BillsDAO;
import fpoly.longlt.duan1.dao.QuanLiDonHangDao;
import fpoly.longlt.duan1.model.DonHang;
import fpoly.longlt.duan1.model.DonHangChiTiet;

public class BillDetailAdapter extends BaseAdapter {
    ArrayList<DonHangChiTiet> list = new ArrayList<>();
    Context context;
    String madonhang;
    int total;

    public BillDetailAdapter(ArrayList<DonHangChiTiet> list, Context context, String madonhang, int total) {
        this.list = list;
        this.context = context;
        this.madonhang = madonhang;
        this.total = total;
    }

    public BillDetailAdapter(ArrayList<DonHangChiTiet> list, Context context, String madonhang) {
        this.list = list;
        this.context = context;
        this.madonhang = madonhang;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.list_item_orderdetail, null);
        TextView tv_madon = convertView.findViewById(R.id.tvMaOrderTD);
        TextView tv_oderdate = convertView.findViewById(R.id.tvOrderDateDT);
        TextView tv_soluong = convertView.findViewById(R.id.tvOrderQuantity);
        TextView tv_gia = convertView.findViewById(R.id.tvOrderPriceDT);
        TextView tv_thanhtien = convertView.findViewById(R.id.tvTotalAmountDT);
        ImageView img_product = convertView.findViewById(R.id.imgOrderProductDT);
        TextView tv_ship = convertView.findViewById(R.id.tvShippingFeeDT);

//        QuanLiDonHangDao daok = new QuanLiDonHangDao(context);
//        list = daok.getDonHangChiTiet(od_id);
        DonHangChiTiet donHangChiTiet = list.get(position);
        tv_soluong.setText("Số lượng: "+donHangChiTiet.getQuantity());
        tv_gia.setText("Giá: "+donHangChiTiet.getPrice());
        tv_thanhtien.setText("Tổng tiền: "+total+" VND");
        tv_ship.setText("Phí vận chuyển: "+20000);
        tv_madon.setText(madonhang);

        return convertView;
    }
}
