package fpoly.longlt.duan1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fpoly.longlt.duan1.R;
import fpoly.longlt.duan1.adapter.CartAdapter;
import fpoly.longlt.duan1.adapter.KiemLaiAdapter;
import fpoly.longlt.duan1.dao.CartDAO;
import fpoly.longlt.duan1.dao.QuanLiDonHangDao;
import fpoly.longlt.duan1.dao.SanPhamDAO;
import fpoly.longlt.duan1.model.DonHang;
import fpoly.longlt.duan1.model.GioHang;
import fpoly.longlt.duan1.model.SanPham;
import fpoly.longlt.duan1.screen.KiemLaiDonHang;

public class CartFragment extends Fragment implements CartAdapter.OnCartUpdateListener {
    private ImageView imgBack;
    private CheckBox cb_selected_all;
    private RecyclerView recyleViewCart;
    private CartAdapter adapter;
    private ArrayList<SanPham> sanPhamList;
    ArrayList<GioHang> gioHangList;
    private CartDAO cartDAO;
    Button btnBuy;
    SanPhamDAO dao;
//    private List<GioHang> selectedItems = new ArrayList<>();
    private TextView tvTotalPrice;
    private int user_id = 1; // Giả sử ID người dùng là 1, thay thế bằng ID thực tế của người dùng

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartDAO = new CartDAO(getContext()); // Khởi tạo CartDAO để truy xuất dữ liệu giỏ hàng
        sanPhamList = new ArrayList<>();
        gioHangList = new ArrayList<>();
        loadCartItems(); // Lấy danh sách sản phẩm từ giỏ hàng
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cb_selected_all = view.findViewById(R.id.cb_select_all);
        btnBuy = view.findViewById(R.id.btn_order);
        cb_selected_all.setOnCheckedChangeListener(((buttonView, isChecked) -> {
           for (SanPham sanPham: sanPhamList){
               sanPham.setSelected(isChecked);
               cartDAO.updateStatus(sanPham.getSpId(), isChecked?1:0);

           }
           adapter.notifyDataSetChanged();
           updateTotalPrice(calculateTotalPrice());
       }));


        // Khởi tạo RecyclerView và thiết lập LayoutManager
        recyleViewCart = view.findViewById(R.id.recyleViewCart);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyleViewCart.setLayoutManager(layoutManager);

        // Khởi tạo Adapter và gán dữ liệu giỏ hàng
        adapter = new CartAdapter(getContext(), sanPhamList, cartDAO, this,gioHangList, this);
        recyleViewCart.setAdapter(adapter);

        // Thiết lập sự kiện quay lại (imgBack)
        imgBack = view.findViewById(R.id.imgBack);

        // Thiết lập TextView hiển thị tổng giá trị
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        updateTotalPrice(calculateTotalPrice()); // Hiển thị tổng giá trị ban đầu
// Cập nhật giỏ hàng mỗi khi người dùng quay lại fragment
        sanPhamList.clear();
        loadCartItems();
        adapter.notifyDataSetChanged(); // Cập nhật lại adapter
        updateTotalPrice(calculateTotalPrice());
        // Cập nhật tổng giá trị

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<GioHang> selecteds = adapter.getSelectedItems();
                Intent intent = new Intent(getActivity(), KiemLaiDonHang.class);
//                intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(selecteds));
                startActivity(intent);
            }
        });

        return view;
    }
//    private void loadCartItems() {
//        CartDAO dao1 = new CartDAO(getContext()); // Lấy danh sách sản phẩm từ SQLite
//        selectedItems = dao1.getAll(user_id); // Lấy danh sách sản phẩm từ SQLite
//        CartAdapter adapter = new CartAdapter(selectedItems);
//        recyleViewCart.setAdapter(adapter);
//    }

    private void loadCartItems() {
        gioHangList = cartDAO.getAll(user_id); // Lấy danh sách GioHang từ DAO
        for (GioHang gioHang : gioHangList) {
            SanPham sanPham = cartDAO.getSanPhamChiTiet(gioHang.getSp_id()); // Lấy thông tin SanPham
            if (sanPham != null) {
                sanPham.setSoLuong(gioHang.getQuantity());
                sanPham.setImg(gioHang.getImgPath());
                sanPham.setColors(gioHang.getColor());
                sanPham.setSize(gioHang.getSize());
                sanPham.setSelected(gioHang.getStatus() == 1); // Đồng bộ trạng thái từ `status`
                sanPhamList.add(sanPham); // Thêm sản phẩm vào danh sách
            }
        }
    }




    private int calculateTotalPrice() {
        int total = 0;
        for (SanPham sanPham : sanPhamList) {
            if (sanPham.isSelected()) { // Chỉ cộng giá trị của sản phẩm được chọn
                total += sanPham.getPrice() * sanPham.getSoLuong();
            }
        }
        return total;
    }


    @Override
    public void updateTotalPrice(int total) {
        // Cập nhật tổng giá trị trên TextView
        tvTotalPrice.setText(String.format("%,d VND", total));
    }
    public void updateSelectedAll() {
        if (sanPhamList == null || sanPhamList.isEmpty()) {
            Log.e("CartFragment", "sanPhamList is null or empty!");
            if (cb_selected_all != null) {
                cb_selected_all.setChecked(false); // Nếu danh sách rỗng, bỏ chọn checkbox
            }
            return;
        }

        boolean allSelected = true;
        // Kiểm tra trạng thái của tất cả các sản phẩm
        for (SanPham sanPham : sanPhamList) {
            if (!sanPham.isSelected()) {
                allSelected = false;
                break;
            }
        }

        // Kiểm tra đối tượng cb_selected_all không phải là null trước khi thay đổi trạng thái
        if (cb_selected_all != null) {
            cb_selected_all.setOnCheckedChangeListener(null); // Ngừng lắng nghe sự kiện để tránh loop vô hạn
            cb_selected_all.setChecked(allSelected); // Đặt trạng thái của checkbox theo trạng thái các sản phẩm
            // Thiết lập lại sự kiện cho checkbox
            cb_selected_all.setOnCheckedChangeListener((buttonView, isChecked) -> {
                for (SanPham sanPham : sanPhamList) {
                    sanPham.setSelected(isChecked); // Cập nhật trạng thái selected cho từng sản phẩm
                }
                adapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
                updateTotalPrice(calculateTotalPrice()); // Cập nhật tổng giá trị
            });
        } else {
            Log.e("CartFragment", "CheckBox cb_selected_all is null!");
        }
    }




}
