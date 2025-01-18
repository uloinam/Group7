package fpoly.longlt.duan1.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fpoly.longlt.duan1.R;
import fpoly.longlt.duan1.dao.CartDAO;
import fpoly.longlt.duan1.fragment.CartFragment;
import fpoly.longlt.duan1.model.GioHang;
import fpoly.longlt.duan1.model.SanPham;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context mContext;
    ArrayList<SanPham> arrayList;
    ArrayList<GioHang> lst;
    ArrayList<GioHang> selectedItems;
    CartDAO cartDAO;
    CartFragment cartFragment;
    CartAdapter adapter;
    OnSelectedChangeListener onSelectedChangeListener;
    OnCartUpdateListener listener; // Interface listener
    public interface OnSelectedChangeListener {
        void onSelectionChanged(List<GioHang> selectedItems);
    }
    public CartAdapter(Context mContext, ArrayList<SanPham> arrayList, CartDAO cartDAO, OnCartUpdateListener listener, ArrayList<GioHang> lst, CartFragment cartFragment) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.cartDAO = cartDAO;
        this.listener = listener;
        this.lst = lst;
        this.cartFragment = cartFragment;
    }

    public CartAdapter(ArrayList<GioHang> lst, OnSelectedChangeListener onSelectedChangeListener) {
        this.lst = lst;
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_item, null);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        SanPham sanPham = arrayList.get(position);
        GioHang gioHang = lst.get(position);

        holder.tvNameCart.setText(sanPham.getTenSp());
        holder.tvquantity.setText(String.valueOf(sanPham.getSoLuong()));
        holder.tvPriceCart.setText(String.valueOf(sanPham.getPrice()) + " VND");
        holder.tvColorCart.setText(sanPham.getColors());
        holder.tvSizeCart.setText(sanPham.getSize());


       String imgPath = gioHang.getImgPath();
        if (imgPath != null && !imgPath.isEmpty()) {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                Uri uri = Uri.fromFile(imgFile);
                holder.imgProduct.setImageURI(uri);
            } else {
                holder.imgProduct.setImageResource(R.drawable.img_2); // Ảnh mặc định
            }
        } else {
            holder.imgProduct.setImageResource(R.drawable.img_3); // Ảnh mặc định
        }
        holder.cb_selected.setChecked(sanPham.isSelected());
//        holder.cb_selected.setOnCheckedChangeListener(null); // Xóa lắng nghe cũ
//        holder.cb_selected.setChecked(selectedItems.contains(lst));

        holder.cb_selected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sanPham.setSelected(isChecked);
            cartDAO.updateStatus(sanPham.getSpId(), isChecked?1:0);
//            if (isChecked){
//                selectedItems.add(gioHang);
//            }else {
//                selectedItems.remove(gioHang);
//            }
            updateTotalPrice();
//            onSelectedChangeListener.onSelectionChanged(selectedItems);
        });


        // Button cộng
        // Button cộng
        holder.btn_cong.setOnClickListener(v -> {
            int newQuantity = sanPham.getSoLuong() + 1;

            // Cập nhật cơ sở dữ liệu
            sanPham.setSoLuong(newQuantity);
            boolean isUpdated = cartDAO.updateQuantity(sanPham);

            if (isUpdated) {
                holder.tvquantity.setText(String.valueOf(newQuantity)); // Cập nhật giao diện
                Toast.makeText(mContext, "Cập nhật thành công. Số lượng mới: " + newQuantity, Toast.LENGTH_SHORT).show();
                updateTotalPrice();  // Cập nhật tổng tiền
            } else {
                Toast.makeText(mContext, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_tru.setOnClickListener(v -> {
            if (sanPham.getSoLuong() > 1) {
                int newQuantity = sanPham.getSoLuong() - 1;

                // Cập nhật cơ sở dữ liệu
                sanPham.setSoLuong(newQuantity);
                boolean isUpdated = cartDAO.updateQuantity(sanPham);

                if (isUpdated) {
                    holder.tvquantity.setText(String.valueOf(newQuantity)); // Cập nhật giao diện
                    Toast.makeText(mContext, "Cập nhật thành công. Số lượng mới: " + newQuantity, Toast.LENGTH_SHORT).show();
                    updateTotalPrice();  // Cập nhật tổng tiền
                } else {
                    Toast.makeText(mContext, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Số lượng không thể nhỏ hơn 1", Toast.LENGTH_SHORT).show();
            }
        });



        // Icon xóa sản phẩm
        holder.imgDelete.setOnClickListener(v -> {
            cartDAO = new CartDAO(mContext);
            boolean check = cartDAO.deleteProduct(lst.get(position).getCart_id());  // Xóa sản phẩm khỏi DB
            if (check){
                arrayList.remove(position);  // Xóa sản phẩm khỏi danh sách
                notifyItemRemoved(position);  // Thông báo RecyclerView cập nhật
                updateTotalPrice();  // Cập nhật tổng tiền
                Toast.makeText(mContext, "xóa thành công", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(mContext, "xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        updateTotalPrice();  // Cập nhật tổng tiền khi item được hiển thị
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }

    // Cập nhật tổng tiền
    public void updateTotalPrice() {
        int total = 0;
        for (SanPham sanPham : arrayList) {
            if (sanPham.isSelected()) {
                total += sanPham.getPrice() * sanPham.getSoLuong();
            }
        }
        if (listener != null) {
            listener.updateTotalPrice(total);  // Gọi listener để cập nhật tổng tiền trong Fragment
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameCart, tvPriceCart, tvColorCart, tvSizeCart, tvquantity, btn_cong, btn_tru;
        Button btnBuyNow;
        ImageView imgDelete, imgProduct;
        CheckBox cb_selected;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCart = itemView.findViewById(R.id.tv_product_name);
            tvPriceCart = itemView.findViewById(R.id.tv_product_price);
            tvColorCart = itemView.findViewById(R.id.tv_product_color);
            tvSizeCart = itemView.findViewById(R.id.tv_product_size);
            btn_cong = itemView.findViewById(R.id.btn_congCart);
            btn_tru = itemView.findViewById(R.id.btn_truCart);
//            btnBuyNow = itemView.findViewById(R.id.btn_buy_now);
            imgDelete = itemView.findViewById(R.id.img_remove);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvquantity = itemView.findViewById(R.id.tv_quantity);
            cb_selected = itemView.findViewById(R.id.cb_selected);
        }
    }
    public ArrayList<GioHang> getSelectedItems() {
        return new ArrayList<>(lst);
    }
    public void checkBox(CheckBox checkBox){

    }

    // Interface để Fragment nhận thông báo
    public interface OnCartUpdateListener {
        void updateTotalPrice(int total);
    }
}
