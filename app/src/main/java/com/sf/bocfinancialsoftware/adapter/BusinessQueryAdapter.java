package com.sf.bocfinancialsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sf.bocfinancialsoftware.R;
import com.sf.bocfinancialsoftware.bean.BusinessBean;
import com.sf.bocfinancialsoftware.bean.BusinessTypeBean;

import java.util.List;

/**
 * 业务查询可扩展列表适配器
 * Created by sn on 2017/6/14.
 */

public class BusinessQueryAdapter extends BaseExpandableListAdapter {

    private Context context; //上下文
    private List<BusinessTypeBean> groups;  //好友分组
    private List<List<BusinessBean>> children;  //分组下的好友子项

    public BusinessQueryAdapter(Context context, List<BusinessTypeBean> groups, List<List<BusinessBean>> children) {
        this.context = context;
        this.groups = groups;
        this.children = children;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_business_group, parent, false);
            groupViewHolder.tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvGroupName.setText(groups.get(groupPosition).getTypeName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildItemViewHolder childItemViewHolder;
        if (convertView == null) {
            childItemViewHolder = new ChildItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_business_group_children, parent, false);
            childItemViewHolder.tvChildItemName = (TextView) convertView.findViewById(R.id.tvChildItemName);
            convertView.setTag(childItemViewHolder);
        } else {
            childItemViewHolder = (ChildItemViewHolder) convertView.getTag();
        }
        childItemViewHolder.tvChildItemName.setText(children.get(groupPosition).get(childPosition).getBusinessName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupViewHolder {
        private TextView tvGroupName;
    }

    private static class ChildItemViewHolder {
        private TextView tvChildItemName; //二级目录名称
    }
}
