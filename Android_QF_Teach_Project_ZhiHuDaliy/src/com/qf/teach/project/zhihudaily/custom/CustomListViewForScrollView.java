package com.qf.teach.project.zhihudaily.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListViewForScrollView extends ListView {

	public CustomListViewForScrollView(Context context) {
		super(context);
	}

	public CustomListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
