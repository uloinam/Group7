package fpoly.longlt.duan1.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import fpoly.longlt.duan1.R;
import fpoly.longlt.duan1.adapter.ThongKeAdapter;
import fpoly.longlt.duan1.dao.SanPhamDAO;
import fpoly.longlt.duan1.dao.ThongKeDao;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThongKeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThongKeFragment extends Fragment {
    private ListView lv_thongke;
    private ThongKeAdapter adapter;
    private List<HashMap<String, Object>> statisticsList;
    private ThongKeDao sanPhamDAO;
    public ThongKeFragment() {
        // Required empty public constructor
    }

    public static ThongKeFragment newInstance() {
        ThongKeFragment fragment = new ThongKeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thong_ke, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_thongke = view.findViewById(R.id.lv_thongke);

        sanPhamDAO = new ThongKeDao(getContext()); // Khởi tạo DAO

        // Lấy dữ liệu thống kê từ DAO
        statisticsList = sanPhamDAO.getMonthlyStatistics();

        // Gắn Adapter cho ListView
        adapter = new ThongKeAdapter(getContext(), statisticsList);
        lv_thongke.setAdapter(adapter);
    }
}