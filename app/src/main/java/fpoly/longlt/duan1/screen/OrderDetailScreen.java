package fpoly.longlt.duan1.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import fpoly.longlt.duan1.R;
import fpoly.longlt.duan1.adapter.BillDetailAdapter;
import fpoly.longlt.duan1.dao.QuanLiDonHangDao;
import fpoly.longlt.duan1.fragment.HomeFragment;
import fpoly.longlt.duan1.model.DonHangChiTiet;

public class OrderDetailScreen extends AppCompatActivity {
    TextView txtNameUserDT, txtMaDT, txtSoLuongDT, txtDateDT,txtGia, txtFeeVCDt, txtGiamGiaDT, txtThanhTien;
    ImageView imgAvatarDT, imgOrderDetailProduct;
    ListView lvOrderDetail;
    BillDetailAdapter billDetailAdapter;
    QuanLiDonHangDao dao;
    ArrayList<DonHangChiTiet> lst = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        anhXa();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            dao = new QuanLiDonHangDao(OrderDetailScreen.this);
            lst = dao.getDonHangChiTiet(bundle.getInt("id"));
            billDetailAdapter = new BillDetailAdapter(lst, OrderDetailScreen.this, bundle.getString("ma_bills"), bundle.getInt("tongtien"));
            lvOrderDetail.setAdapter(billDetailAdapter);
////            txtNameUserDT.setText(bundle.getString("name"));
//            txtMaDT.setText(bundle.getString("ma_bills"));
////            txtSoLuongDT.setText(bundle.getString("soLuong"));
//            txtDateDT.setText(bundle.getString("date_bills"));
//            txtGia.setText(bundle.getString("gia"));
//            txtFeeVCDt.setText(bundle.getString("feeVC"));
//            txtGiamGiaDT.setText(bundle.getString("giamGia"));
//            txtThanhTien.setText(bundle.getString("thanhTien"));
        }
        imgAvatarDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutODDT, HomeFragment.newInstance()).commit();
                finish();
            }
        });

    }

    private void anhXa() {
        txtNameUserDT = findViewById(R.id.tvUserNameDT);
        txtMaDT = findViewById(R.id.tvMaOrderTD);
        txtSoLuongDT = findViewById(R.id.tvOrderQuantity);
        txtDateDT = findViewById(R.id.tvOrderDateDT);
        txtGia = findViewById(R.id.tvOrderPriceDT);
//        txtFeeVCDt = findViewById(R.id.tvShippingFeeDT);
//        txtGiamGiaDT = findViewById(R.id.tvDiscountDT);
        lvOrderDetail = findViewById(R.id.lv_orderdetail);
        txtThanhTien = findViewById(R.id.tvTotalAmountDT);
        imgAvatarDT = findViewById(R.id.imgUserAvatarDT);
        imgOrderDetailProduct = findViewById(R.id.imgOrderProductDT);
    }
}