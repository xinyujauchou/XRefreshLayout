package demokit.chaowang.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gsywc.xrefreshlayout.XRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import demokit.chaowang.demo.adapter.RecyclerViewAdapter;
import demokit.chaowang.demo.model.Book;
import demokit.chaowang.demo.model.RecyclerViewListener;
import demokit.xrefresh.R;

public class LineaRecyclerViewActivity extends AppCompatActivity implements XRefreshLayout.RefreshListener,RecyclerViewListener{
    private List<Book> list = new ArrayList<>();
    private XRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private int loadCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linea_recycler_view);
        refreshLayout = (XRefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshListener(this)    //接受刷新、加载 回调
//                     .setHeaderView(new MyHeadView(LineaRecyclerViewActivity.this))  //设置自定义header,也可以用默认的header
                     .setPullRefreshEnable(true)    //打开刷新开关
                     .setPullLoadMoreEnable(true);  //打开上拉加载开关
        initLinearManager();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, list);
        mRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
        refreshLayout.setRefreshing(true);

        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 100);

}

    private void initLinearManager(){
        layoutManager = new LinearLayoutManager(this);
    }

    private void addBook(){
        int bookCount = list.size();
        for(int index = bookCount; index < bookCount + 20; index++){
            list.add(new Book("book "+index));
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                addBook();
                loadCount = 1;
                refreshLayout.setHasLoadOver(false);
                refreshLayout.setRefreshing(false);
                refreshLayout.setRefreshTime(System.currentTimeMillis());
            }
        }, 1000);

    }

    @Override
    public void onLoadMore() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadCount >= 2){
                    refreshLayout.setHasLoadOver(true);
                }else{
                    loadCount++;
                    addBook();
                    refreshLayout.setLoadingMore(false);
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }, 3000);
    }

    @Override
    public void OnItemClickListener(View view, int position) {
        refreshLayout.setRefreshing(true);
    }
}
