package demokit.chaowang.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gsywc.xrefreshlayout.XRefreshLayout;

import demokit.xrefresh.R;

public class ScrollViewActivity extends AppCompatActivity implements XRefreshLayout.RefreshListener,
        View.OnClickListener{
    private XRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);

        refreshLayout = (XRefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.seteRreshListener(this);

        findViewById(R.id.id1).setOnClickListener(this);
        findViewById(R.id.id2).setOnClickListener(this);
        findViewById(R.id.id3).setOnClickListener(this);
        findViewById(R.id.id4).setOnClickListener(this);
        findViewById(R.id.id5).setOnClickListener(this);
        findViewById(R.id.id6).setOnClickListener(this);
        findViewById(R.id.id7).setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setLoadingMore(false);
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id1:
                Toast.makeText(this, "id1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id2:
                Toast.makeText(this, "id2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id3:
                Toast.makeText(this, "id3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id4:
                Toast.makeText(this, "id4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id5:
                Toast.makeText(this, "id5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id6:
                Toast.makeText(this, "id6", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id7:
                Toast.makeText(this, "id7", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
