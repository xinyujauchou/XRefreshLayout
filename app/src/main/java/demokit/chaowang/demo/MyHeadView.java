package demokit.chaowang.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsywc.xrefreshlayout.model.HeaderState;
import com.gsywc.xrefreshlayout.model.IHeaderCallBack;

/**
 * Created by chao.wang on 2016/3/17.
 */
public class MyHeadView extends RelativeLayout implements IHeaderCallBack{
    TextView textView = null;

    public MyHeadView(Context context) {
        super(context);
        init(context);
    }

    public MyHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 400));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.GREEN);
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(90);
        textView.setText("下拉可以刷新");
        addView(textView);
    }

    @Override
    public void onStateChange(HeaderState headerState) {
        switch (headerState){
            case NOMAL:
                onStateNormal();
                break;
            case READY:
                onStateReady();
                break;
            case FRESHING:
                onStateRefreshing();
                break;
            case COMPLETE:
                onStateFinish();
                break;
        }
    }

    @Override
    public void onStateNormal() {
        textView.setText("下拉可以刷新");
    }

    @Override
    public void onStateReady() {
        textView.setText("松手可以刷新");
    }

    @Override
    public void onStateRefreshing() {
        textView.setText("正在刷新");
    }

    @Override
    public void onStateFinish() {
        textView.setText("刷新完成");
    }

    @Override
    public void onHeaderMove(double offset, float percent) {

    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public int getHeaderHeight() {
        return 0;
    }
}
