package demokit.chaowang.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import demokit.chaowang.demo.adapter.RecyclerViewAdapter;
import demokit.chaowang.demo.model.Book;
import demokit.chaowang.demo.model.RecyclerViewListener;
import demokit.xrefresh.R;

public class MainActivity extends AppCompatActivity implements RecyclerViewListener{

    private RecyclerView recyclerView;
    private List<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, books);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initData(){
        books.add(new Book("listView"));
        books.add(new Book("scrollView"));
        books.add(new Book("recyclerview"));
    }

    @Override
    public void OnItemClickListener(View view, int position) {
        Intent intent = new Intent();
        switch (position){
            case 0:
                intent.setClass(MainActivity.this, LsitViewActivity.class);
                break;
            case 1:
                intent.setClass(MainActivity.this, ScrollViewActivity.class);
                break;
            case 2:
                intent.setClass(MainActivity.this, LineaRecyclerViewActivity.class);
                break;
        }
        startActivity(intent);
    }
}
