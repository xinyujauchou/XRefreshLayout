package demokit.chaowang.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gsywc.xrefreshlayout.XRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import demokit.chaowang.demo.adapter.RecyclerViewAdapter;
import demokit.chaowang.demo.model.Book;
import demokit.xrefresh.R;

public class LineaRecyclerViewActivity extends AppCompatActivity implements XRefreshLayout.RefreshListener{
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
        refreshLayout.seteRreshListener(this)
                     .setHeaderView(new MyHeadView(LineaRecyclerViewActivity.this))
                     .setPullRefreshEnable(true);
        addBook();
        initLinearManager();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new RecyclerViewAdapter(this, list));

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
        }, 3000);
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

}
