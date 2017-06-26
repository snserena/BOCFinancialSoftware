package com.sf.bocfinancialsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sf.bocfinancialsoftware.R;
import com.sf.bocfinancialsoftware.bean.MessageReminderBean;
import com.sf.bocfinancialsoftware.bean.PleasantMessageBean;

import java.util.List;

/**
 * 温馨提示列表适配器
 * Created by sn on 2017/6/12.
 */

public class PleasantMessageAdapter extends BaseAdapter {

    private Context context;
    private List<PleasantMessageBean> pleasantMessageBeanList; //消息集合

    public PleasantMessageAdapter(Context context, List<PleasantMessageBean> pleasantMessageBeanList) {
        this.context = context;
        this.pleasantMessageBeanList = pleasantMessageBeanList;
    }

    @Override
    public int getCount() {
        return pleasantMessageBeanList != null ? pleasantMessageBeanList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return pleasantMessageBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PleasantMessageViewHolder messageViewHolder = null;
        if (convertView == null) {
            messageViewHolder = new PleasantMessageViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pleasant_message, null);
            messageViewHolder.tvPleasantMsgDateAndTime = (TextView) convertView.findViewById(R.id.tvPleasantMsgDateAndTime);
            messageViewHolder.tvPleasantMsgContent = (TextView) convertView.findViewById(R.id.tvPleasantMsgContent);
            convertView.setTag(messageViewHolder);
        } else {
            messageViewHolder = (PleasantMessageViewHolder) convertView.getTag();
        }
        PleasantMessageBean pleasantMessageBean = pleasantMessageBeanList.get(position);
        messageViewHolder.tvPleasantMsgDateAndTime.setText(pleasantMessageBean.getMsgDateAndTime());
        messageViewHolder.tvPleasantMsgContent.setText(pleasantMessageBean.getMsgContent());
        return convertView;
    }

    class PleasantMessageViewHolder {
        private TextView tvPleasantMsgDateAndTime; //消息日期和时间
        private TextView tvPleasantMsgContent; //消息内容
    }
}
