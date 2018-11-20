package vn.edu.uit.quanlychitieunhom.Adapters;

import android.content.Context;

import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.edu.uit.quanlychitieunhom.R;
import vn.edu.uit.quanlychitieunhom.model.giaodich;

public class List_GiaoDich_Adapter extends BaseAdapter {

    private Context mContext;
    private List<giaodich> mTransactionList;
    private ArrayList<giaodich> list_itemGiaodich;

    private FragmentActivity myContext;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM yyyy");
    NumberFormat  numberFormat = new DecimalFormat("#,###,###");



    public List_GiaoDich_Adapter(Context mContext, List<giaodich> mTransactionList) {
        this.mContext = mContext;
        this.mTransactionList = mTransactionList;
    }

    @Override
    public int getCount() {
        return mTransactionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTransactionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext,R.layout.fragment_giao_dich,null);
        TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
//        TextView tvTransactionName = (TextView) v.findViewById(R.id.tvTransactionName);
//        TextView tvCharge = (TextView) v.findViewById(R.id.tvCharge);
        TextView tvDay = (TextView) v.findViewById(R.id.tvDay);

        tvDay.setText(
                mTransactionList.get(position).getNgaygiaodich().getDay()>=10?
                Integer.toString(mTransactionList.get(position).getNgaygiaodich().getDay()):
                "0"+Integer.toString(mTransactionList.get(position).getNgaygiaodich().getDay())
        );
        tvDate.setText("Tháng " + dateFormat.format(mTransactionList.get(position).getNgaygiaodich()));
//        tvTransactionName.setText(mTransactionList.get(position).getGhichu());
//        tvCharge.setText(numberFormat.format(mTransactionList.get(position).getSotien()));
        v.setTag(mTransactionList.get(position).getMagiaodich());


        ListView lv_item = (ListView) v.findViewById(R.id.lv_item);

        list_itemGiaodich = new ArrayList<>();
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something","no image",1,"Thai Nguyen",1));
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something1","no image",1,"Thai Nguyen",1));
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something2","no image",1,"Thai Nguyen",1));
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something3","no image",1,"Thai Nguyen",1));
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something4","no image",1,"Thai Nguyen",1));
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something5","no image",1,"Thai Nguyen",1));
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something6","no image",1,"Thai Nguyen",1));
        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something7","no image",1,"Thai Nguyen",1));
//        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something8","no image",1,"Thai Nguyen",1));
//        list_itemGiaodich.add(new giaodich(001,new Date(), (double) 2000,"Note something9","no image",1,"Thai Nguyen",1));

        /*Pass param for list_adapter*/
        List_ItemGiaoDich_Adapter list_itemGiaoDich_adapter= new List_ItemGiaoDich_Adapter(v.getContext(),list_itemGiaodich);

        /*Set adapter for listview*/
        lv_item.setAdapter(list_itemGiaoDich_adapter);


        LinearLayout container = (LinearLayout) v.findViewById(R.id.ln_container);
        ViewGroup.LayoutParams param = container.getLayoutParams();
        param.height= 145*list_itemGiaodich.size();
        container.setLayoutParams(param);

        return v;
    }


}
