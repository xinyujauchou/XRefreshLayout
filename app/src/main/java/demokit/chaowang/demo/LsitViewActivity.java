package demokit.chaowang.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsywc.xrefreshlayout.XRefreshLayout;

import demokit.xrefresh.R;

public class LsitViewActivity extends Activity implements AdapterView.OnItemClickListener,
        XRefreshLayout.RefreshListener{
    private ListView mListView;
    private XRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsitview);

        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(new MyAdapter());

        refreshLayout = (XRefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "开始刷新了", Toast.LENGTH_LONG).show();
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 4000);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView content = new TextView(LsitViewActivity.this);
            content.setLayoutParams(new AbsListView.LayoutParams(-1, 360));
            content.setBackgroundColor(Color.WHITE);
            content.setGravity(Gravity.CENTER_VERTICAL);
            content.setTextSize(30);
            content.setText("content: "+position);
            return content;
        }
    }

}
