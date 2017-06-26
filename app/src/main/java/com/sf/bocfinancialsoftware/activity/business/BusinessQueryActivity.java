package com.sf.bocfinancialsoftware.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sf.bocfinancialsoftware.R;
import com.sf.bocfinancialsoftware.activity.base.BaseActivity;
import com.sf.bocfinancialsoftware.adapter.BusinessQueryAdapter;
import com.sf.bocfinancialsoftware.bean.BusinessBean;
import com.sf.bocfinancialsoftware.bean.BusinessTypeBean;
import com.sf.bocfinancialsoftware.util.DataUtil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import static com.sf.bocfinancialsoftware.constant.ConstantConfig.BUSINESS_NAME;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.CONTRACT_ID;
import static com.sf.bocfinancialsoftware.constant.ConstantConfig.SCAN_CODE_REQUEST_BUSINESS_QUERY;

/**
 * 业务查询
 * Created by sn on 2017/6/14.
 */

public class BusinessQueryActivity extends BaseActivity implements View.OnClickListener, ExpandableListView.OnChildClickListener {

    private ImageView ivTitleBarBack;  //返回
    private ImageView ivBusinessQueryScan;  //扫一扫
    private TextView tvTitleBarTitle;  //标题
    private EditText etBusinessQuery; //输入框
    private Button btnBusinessQuery; //查询按钮
    private ExpandableListView elvBusiness; //可扩展列表
    private List<BusinessTypeBean> groups;  //组名集合
    private List<List<BusinessBean>> children; //所有组别下的好友集合
    private BusinessQueryAdapter adapter;  //列表适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_query);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        ivTitleBarBack = (ImageView) findViewById(R.id.ivTitleBarBack);
        ivBusinessQueryScan = (ImageView) findViewById(R.id.ivBusinessQueryScan);
        tvTitleBarTitle = (TextView) findViewById(R.id.tvTitleBarTitle);
        etBusinessQuery = (EditText) findViewById(R.id.etBusinessQuery);
        btnBusinessQuery = (Button) findViewById(R.id.btnBusinessQuery);
        elvBusiness = (ExpandableListView) findViewById(R.id.elvBusiness);
    }

    @Override
    protected void initData() {
        tvTitleBarTitle.setText(getString(R.string.common_business_query));
        ivTitleBarBack.setVisibility(View.VISIBLE);
        groups = new ArrayList<BusinessTypeBean>();
        children = new ArrayList<List<BusinessBean>>();
        DataUtil.setExpandableListData(groups, children); //设置好友列表信息
        adapter = new BusinessQueryAdapter(BusinessQueryActivity.this, groups, children);
        elvBusiness.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        ivTitleBarBack.setOnClickListener(this);
        ivBusinessQueryScan.setOnClickListener(this);
        elvBusiness.setOnChildClickListener(this);
        btnBusinessQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ivTitleBarBack:  //返回
                finish();
                break;
            case R.id.ivBusinessQueryScan:  //扫一扫
                intent = new Intent(BusinessQueryActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_CODE_REQUEST_BUSINESS_QUERY);
                break;
            case R.id.btnBusinessQuery:  //查询
                String contractId = etBusinessQuery.getText().toString();
                if (!TextUtils.isEmpty(contractId)) {
                    intent = new Intent(BusinessQueryActivity.this, BusinessQueryResultActivity.class);
                    intent.putExtra(CONTRACT_ID, contractId);
                    startActivity(intent);
                } else {
                    Toast.makeText(BusinessQueryActivity.this, getString(R.string.common_please_enter_the_contract_id), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(BusinessQueryActivity.this, BusinessQueryCriteriaActivity.class);
        intent.putExtra(BUSINESS_NAME, children.get(groupPosition).get(childPosition).getBusinessName());
        startActivity(intent);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_CODE_REQUEST_BUSINESS_QUERY && resultCode == RESULT_OK) {  //扫描二维码值回调
            Bundle extras = data.getExtras();
            if (null != extras) {
                String result = extras.getString("result");  //获取传回的业务编码
                Intent intent = new Intent(BusinessQueryActivity.this, BusinessQueryResultActivity.class);
                intent.putExtra(CONTRACT_ID, result);
                startActivity(intent);
            }
        }
    }
}
