package com.sf.bocfinancialsoftware.activity.message;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sf.bocfinancialsoftware.R;
import com.sf.bocfinancialsoftware.activity.base.BaseActivity;
import com.sf.bocfinancialsoftware.adapter.MessageAdapter;
import com.sf.bocfinancialsoftware.bean.MessageReminderBean;
import com.sf.bocfinancialsoftware.util.DataBaseSQLiteUtil;
import com.sf.bocfinancialsoftware.util.SwipeRefreshUtil;

import java.util.List;

import static com.sf.bocfinancialsoftware.constant.ConstantConfig.IMPORT_RESPONSE;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.MSG_READ;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.MSG_READ_SUM;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.MSG_TYPE_ID;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.QUERY_IMPORT_FINANCIAL_BUSINESS;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.QUERY_IMPORT_INWARD_COLLECTION;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.QUERY_IMPORT_OPEN;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.QUERY_IMPORT_ORDER;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.QUERY_IMPORT_PAY_AT_MATURITY;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.QUERY_IMPORT_SIGHT_PAYMENT;

/**
 * 进口通知列表
 * Created by sn on 2017/6/12.
 */

public class ImportMessageListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    private ImageView ivTitleBarBack;  //返回
    private ImageView ivTitleBarFilter;  //筛选
    private TextView tvTitleBarTitle;  //标题
    private SwipeRefreshLayout swipeRefreshLayoutMessage; //下拉刷新上拉加载
    private ListView lvMessage;  //进口消息列表
    private View headView; //列表头部
    private View footView; //列表尾部
    private LinearLayout lltLoadMore; //列表尾部加载更多
    private LinearLayout lltEmptyViewMessage; //处理空数据
    private MessageAdapter messageAdapter; //列表适配器
    private List<MessageReminderBean> messageBeanList; //已加载的数据列表
    private List<MessageReminderBean> allMessageList; //所有的数据列表
    private Intent mIntent;
    private String typeId;  //消息类型id
    private PopupWindow mPopWindow;
    private LinearLayout lltFilterCondition0;
    private LinearLayout lltFilterCondition1;
    private LinearLayout lltFilterCondition2;
    private LinearLayout lltFilterCondition3;
    private LinearLayout lltFilterCondition4;
    private LinearLayout lltFilterCondition5;
    private LinearLayout lltFilterCondition6;
    private TextView tvFilterCondition0;
    private TextView tvFilterCondition1;
    private TextView tvFilterCondition2;
    private TextView tvFilterCondition3;
    private TextView tvFilterCondition4;
    private TextView tvFilterCondition5;
    private TextView tvFilterCondition6;
    private int msgReadSum; //已读消息数量
    private boolean isLastLine = false;  //列表是否滚动到最后一行
    private int page = 0;
    private String filter = ""; //筛选条件
    private Handler mHandler = new Handler() {  //主线程中的Handler对象
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {  //下拉刷新
                //下拉刷新，三秒睡眠之后   重新加载
                page = 0;
                List<MessageReminderBean> list = DataBaseSQLiteUtil.queryMessageByTypeAndTitle(typeId, filter, page, 4);
                if (list == null || list.size() <= 0) {
                    Toast.makeText(ImportMessageListActivity.this, getString(R.string.common_refresh_failed), Toast.LENGTH_SHORT).show();
                } else {
                    messageBeanList.clear();
                    messageBeanList.addAll(list);
                    messageAdapter.notifyDataSetChanged();
                    Toast.makeText(ImportMessageListActivity.this, getString(R.string.common_refresh_success), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayoutMessage.setRefreshing(false);//加载完毕，设置不刷新
            } else if (msg.what == 11) { //上拉加载更多
                page++; //页数自增
                List<MessageReminderBean> loadMoreList = DataBaseSQLiteUtil.queryMessageByTypeAndTitle(typeId, filter, page, 4);
                messageBeanList.addAll(loadMoreList);
                msgReadSum = messageBeanList.size();
                isLastLine = false;
                for (MessageReminderBean bean : loadMoreList) {
                    //在打开消息列表的时候，将消息设置为已读状态,并将改变的状态存入数据库
                    bean.setMsgIsRead(MSG_READ);
                    DataBaseSQLiteUtil.updateMessageReminder(bean);
                }
                messageAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        ivTitleBarBack = (ImageView) findViewById(R.id.ivTitleBarBack);
        ivTitleBarFilter = (ImageView) findViewById(R.id.ivTitleBarFilter);
        tvTitleBarTitle = (TextView) findViewById(R.id.tvTitleBarTitle);
        swipeRefreshLayoutMessage = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutMessage);
        lvMessage = (ListView) findViewById(R.id.lvMessage);
        headView = LayoutInflater.from(this).inflate(R.layout.layout_list_head, null);
        footView = LayoutInflater.from(this).inflate(R.layout.layout_list_foot, null);
        lltLoadMore = (LinearLayout) footView.findViewById(R.id.lltLoadMore);
        lltEmptyViewMessage = (LinearLayout) findViewById(R.id.lltEmptyViewMessage);
    }

    @Override
    protected void initData() {
        tvTitleBarTitle.setText(getString(R.string.common_message_reminder_import));
        ivTitleBarBack.setVisibility(View.VISIBLE);
        ivTitleBarFilter.setVisibility(View.VISIBLE);
        mIntent = getIntent();
        typeId = mIntent.getStringExtra(MSG_TYPE_ID);
        filter = "";
        messageBeanList = DataBaseSQLiteUtil.queryMessageByTypeAndTitle(typeId, filter, page, 4); //已经加载的数据个数，现在没有筛选条件
        messageAdapter = new MessageAdapter(ImportMessageListActivity.this, messageBeanList);
        lvMessage.addHeaderView(headView);
        lvMessage.addFooterView(footView);
        lvMessage.setAdapter(messageAdapter);
        lvMessage.setEmptyView(lltEmptyViewMessage); //处理空ListView
        for (MessageReminderBean bean : messageBeanList) {
            //在打开消息列表的时候，将消息设置为已读状态,并将改变的状态存入数据库
            bean.setMsgIsRead(MSG_READ);
            DataBaseSQLiteUtil.updateMessageReminder(bean);
        }
        msgReadSum = messageBeanList.size(); //已读消息数量
        allMessageList = DataBaseSQLiteUtil.queryAllMessageByTypeAndTitle(typeId, filter);
        if (messageBeanList.size() >= allMessageList.size()) {
            lltLoadMore.setVisibility(View.GONE);// 如果加载完毕，隐藏掉正在加载图标
        }
        messageAdapter.notifyDataSetChanged();
        //刷新圆圈颜色
        SwipeRefreshUtil.setRefreshCircle(swipeRefreshLayoutMessage);
    }

    @Override
    protected void initListener() {
        ivTitleBarBack.setOnClickListener(this);
        ivTitleBarFilter.setOnClickListener(this);
        swipeRefreshLayoutMessage.setOnRefreshListener(this);
        lvMessage.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleBarBack:  //返回
                Intent intent = new Intent();
                intent.putExtra(MSG_READ_SUM, msgReadSum);
                setResult(IMPORT_RESPONSE, intent);
                finish();
                break;
            case R.id.ivTitleBarFilter: //筛选
                createPopupWindow();
                break;
            case R.id.lltFilterCondition0:  //筛选全部
                filter = "";
                page = 0;
                filterMessage(typeId, filter, page, 4);
                break;
            case R.id.lltFilterCondition1:  //筛选进口信用证开立通知
                filter = QUERY_IMPORT_OPEN;
                page = 0;
                filterMessage(typeId, filter, page, 4);
                break;
            case R.id.lltFilterCondition2: //筛选进口信用证到单通知
                filter = QUERY_IMPORT_ORDER;
                page = 0;
                filterMessage(typeId, filter, page, 4);
                break;
            case R.id.lltFilterCondition3: //筛选进口信用证即期付款提示
                filter = QUERY_IMPORT_SIGHT_PAYMENT;
                page = 0;
                filterMessage(typeId, filter, page, 4);
                break;
            case R.id.lltFilterCondition4: //进口信用证承兑到期付款提示
                filter = QUERY_IMPORT_PAY_AT_MATURITY;
                page = 0;
                filterMessage(typeId, filter, page, 4);
                break;
            case R.id.lltFilterCondition5: //进口贸易融资业务到期提示
                filter = QUERY_IMPORT_FINANCIAL_BUSINESS;
                page = 0;
                filterMessage(typeId, filter, page, 4);
                break;
            case R.id.lltFilterCondition6: //进口代收到单通知
                filter = QUERY_IMPORT_INWARD_COLLECTION;
                  page = 0;
                filterMessage(typeId, filter, page, 4);
                break;
            default:
                break;
        }
    }

    /**
     * 筛选并刷新列表
     */
    private void filterMessage(String id, String filter, int page, int count) {
        allMessageList = DataBaseSQLiteUtil.queryAllMessageByTypeAndTitle(id, filter);
        messageBeanList.clear();
        messageAdapter.notifyDataSetChanged();
        List<MessageReminderBean> list = DataBaseSQLiteUtil.queryMessageByTypeAndTitle(id, filter, page, count);
        messageBeanList.addAll(list);
        if (messageBeanList.size() >= allMessageList.size()) { //加载完成
            lltLoadMore.setVisibility(View.GONE);
        }
        messageAdapter.notifyDataSetChanged();
        mPopWindow.dismiss();
    }

    /**
     * 创建PopupWindow
     */
    private void createPopupWindow() {
        View contentView = LayoutInflater.from(ImportMessageListActivity.this).inflate(R.layout.layout_message_popupwindow, null);
        mPopWindow = new PopupWindow(contentView);
        // 设置宽和高
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 3.获取视图里面的控件进行操作
        lltFilterCondition0 = (LinearLayout) contentView.findViewById(R.id.lltFilterCondition0);
        lltFilterCondition1 = (LinearLayout) contentView.findViewById(R.id.lltFilterCondition1);
        lltFilterCondition2 = (LinearLayout) contentView.findViewById(R.id.lltFilterCondition2);
        lltFilterCondition3 = (LinearLayout) contentView.findViewById(R.id.lltFilterCondition3);
        lltFilterCondition4 = (LinearLayout) contentView.findViewById(R.id.lltFilterCondition4);
        lltFilterCondition5 = (LinearLayout) contentView.findViewById(R.id.lltFilterCondition5);
        lltFilterCondition6 = (LinearLayout) contentView.findViewById(R.id.lltFilterCondition6);
        tvFilterCondition0 = (TextView) contentView.findViewById(R.id.tvFilterCondition0);
        tvFilterCondition1 = (TextView) contentView.findViewById(R.id.tvFilterCondition1);
        tvFilterCondition2 = (TextView) contentView.findViewById(R.id.tvFilterCondition2);
        tvFilterCondition3 = (TextView) contentView.findViewById(R.id.tvFilterCondition3);
        tvFilterCondition4 = (TextView) contentView.findViewById(R.id.tvFilterCondition4);
        tvFilterCondition5 = (TextView) contentView.findViewById(R.id.tvFilterCondition5);
        tvFilterCondition6 = (TextView) contentView.findViewById(R.id.tvFilterCondition6);
        //设置筛选条件
        tvFilterCondition0.setText(getString(R.string.activity_message_filter_condition0));
        tvFilterCondition1.setText(getString(R.string.activity_message_import_filter_condition1));
        tvFilterCondition2.setText(getString(R.string.activity_message_import_filter_condition2));
        tvFilterCondition3.setText(getString(R.string.activity_message_import_filter_condition3));
        tvFilterCondition4.setText(getString(R.string.activity_message_import_filter_condition4));
        tvFilterCondition5.setText(getString(R.string.activity_message_import_filter_condition5));
        tvFilterCondition6.setText(getString(R.string.activity_message_import_filter_condition6));
        //监听事件
        lltFilterCondition0.setOnClickListener(this);
        lltFilterCondition1.setOnClickListener(this);
        lltFilterCondition2.setOnClickListener(this);
        lltFilterCondition3.setOnClickListener(this);
        lltFilterCondition4.setOnClickListener(this);
        lltFilterCondition5.setOnClickListener(this);
        lltFilterCondition6.setOnClickListener(this);
        // 设置是否可以触摸PopupWindow外部的区域
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setTouchable(true);
        //通常来将，PopupWindow里面的控件如果是button，TextView ，ListView，默认是不会获取焦点， 需要设置mPopWindow.setFocusable(true)来提供给控件获取焦点
        mPopWindow.setFocusable(true);
        // 设置背景图片
        mPopWindow.setBackgroundDrawable(new ColorDrawable(0));
        // anchor:是能够触发显示PopupWindow的试图
        mPopWindow.showAsDropDown(ivTitleBarFilter);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000); //睡眠3秒
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && isLastLine) { //停止滚动，且滚动到最后一行
            if (messageBeanList.size() >= allMessageList.size()) { // 如果加载完毕，隐藏掉正在加载图标
                lltLoadMore.setVisibility(View.GONE);
                Toast.makeText(ImportMessageListActivity.this, getString(R.string.common_not_date), Toast.LENGTH_SHORT).show();
            } else {
                lltLoadMore.setVisibility(View.VISIBLE);// 如果未加载完毕，显示掉正在加载图标
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(1000); //睡眠3秒
                            Message msg = new Message();
                            msg.what = 11;
                            mHandler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {  //当滚到最后一行
            isLastLine = true;
        }
    }
}
