package demokit.chaowang.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import demokit.chaowang.demo.model.BaseViewHolder;
import demokit.chaowang.demo.model.Book;
import demokit.chaowang.demo.model.RecyclerViewListener;
import demokit.xrefresh.R;

/**
 * RecyclerView 适配器
 * Created by chao.wang on 2016/3/14.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private Context mContext;
    private List<Book> list;
    private RecyclerViewListener mListener;

    public RecyclerViewAdapter(Context context, List<Book> list){
        this.mContext = context;
        this.list = list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.linear_manager_item, parent, false);
        return new MyViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = list.get(position);
        holder.tvContent.setText(book.getName());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setListener(RecyclerViewListener mListener) {
        this.mListener = mListener;
    }

    class MyViewHolder extends BaseViewHolder{
        private TextView tvContent;


        public MyViewHolder(View itemView, int viewType) {
            super(itemView, mListener);
            tvContent = (TextView)itemView.findViewById(R.id.tv_content);
        }

        @Override
        public RecyclerViewListener getRecyclerViewListener() {
            return mListener;
        }
    }

}
