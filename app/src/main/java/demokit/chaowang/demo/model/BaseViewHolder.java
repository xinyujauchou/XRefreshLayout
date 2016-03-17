package demokit.chaowang.demo.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chao.wang on 2015/7/31.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private RecyclerViewListener recyclerViewListener;

    public BaseViewHolder(View itemView, RecyclerViewListener recyclerViewListener) {
        super(itemView);
        this.recyclerViewListener = recyclerViewListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(getListener() != null){
            recyclerViewListener.OnItemClickListener(v, getLayoutPosition());
        }
    }

    protected RecyclerViewListener getListener(){
        return recyclerViewListener != null ? recyclerViewListener : getRecyclerViewListener();
    }

    public RecyclerViewListener getRecyclerViewListener(){
        return null;
    }
}
