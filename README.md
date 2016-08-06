# XRefreshLayout
 对应类： XRefreshLayout    类型：LinearLayout

             功能：刷新/加载框架

             特点：1.支持刷新/加载（考虑到项目没有此场景，暂时不支持静默加载，节省资源，后面可以拓展）

                       2.可定制Header 以及  Footer（遵从接口实现）

                       3.无数据时支持 overScroll 操作

                       4.兼容  ListView、ScrollView、WebView、RecyclerView(LinearLayoutManager/GridLayoutManager/StaggeredGridLayoutManager)、以及 CoordinatorLayout

             使用方法：

                       和SwipeLayout一致：套在根布局，且只允许有一个child，如下：

              XML:

            <com.gsywc.xrefreshlayout.XRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
                      xmlns:tools="http://schemas.android.com/tools"
                      android:id="@+id/refreshLayout"
                      android:layout_width="match_parent"
                      android:layout_height="match_parent"
                      android:fitsSystemWindows="true"
                      tools:context="demokit.chaowang.demo.LineaRecyclerViewActivity">

                          <android.support.v7.widget.RecyclerView
                                  android:id="@+id/recycler_view"
                                  android:layout_width="match_parent"
                                  android:layout_height="match_parent"
                                  android:background="#bbccaa"
                                  android:scrollbars="vertical" />

             </com.gsywc.xrefreshlayout.XRefreshLayout>

           Java:  

             refreshLayout = (XRefreshLayout)findViewById(R.id.refreshLayout);
             refreshLayout.seteRreshListener(this)            //接受刷新、加载 回调
                          .setHeaderView(new MyHeadView(LineaRecyclerViewActivity.this)) //设置自定义header,也可以用默认的header
                          .setPullRefreshEnable(true)      //打开刷新开关
                          .setPullLoadMoreEnable(true); //打开上拉加载开关

                          refreshLayout支持首次自动下拉加载，使用方法 refreshLayout.setRefreshing(true); 会出现刷新等一系列动画，同时会回调onRefresh()方法。

            在刷新完成后使用refreshLayout.setRefreshing(false)来结束刷新动画。

            同样可以使用refreshLayout.setLoadingMore(false)结束加载更多动画，使用refreshLayout.setLoadOver()来通知控件全部加载完成，或出现特殊footer提示。
            
            
            
