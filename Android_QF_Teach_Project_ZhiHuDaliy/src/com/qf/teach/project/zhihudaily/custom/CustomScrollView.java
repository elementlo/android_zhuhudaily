package com.qf.teach.project.zhihudaily.custom;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	/**
	 * ��Ҫ�������û���ָ�뿪MyScrollView��MyScrollView���ڼ���������������������Y�ľ��룬Ȼ�����Ƚ�
	 */
	private int lastScrollY;

	public CustomScrollView(Context context) {
		this(context, null);
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * ���ù����ӿ�
	 * 
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	/**
	 * �����û���ָ�뿪MyScrollView��ʱ���ȡMyScrollView������Y���룬Ȼ��ص���onScroll������
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = CustomScrollView.this.getScrollY();

			// ��ʱ�ľ���ͼ�¼�µľ��벻��ȣ��ڸ�5�����handler������Ϣ
			if (lastScrollY != scrollY) {
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);
			}
			if (onScrollListener != null) {
				onScrollListener.onScroll(scrollY);
			}

		};

	};

	/**
	 * ��дonTouchEvent�� ���û�������MyScrollView�����ʱ��
	 * ֱ�ӽ�MyScrollView������Y�������ص���onScroll�����У����û�̧���ֵ�ʱ��
	 * MyScrollView���ܻ��ڻ��������Ե��û�̧�������Ǹ�5�����handler������Ϣ����handler����
	 * MyScrollView�����ľ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (onScrollListener != null) {
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			handler.sendMessageDelayed(handler.obtainMessage(), 5);
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 
	 * �����Ļص��ӿ�
	 * 
	 * @author xiaanming
	 * 
	 */
	public interface OnScrollListener {
		/**
		 * �ص������� ����MyScrollView������Y�������
		 * 
		 * @param scrollY
		 *            ��
		 */
		public void onScroll(int scrollY);
	}
	
	/**
	 * �Ƿ񵽴ﶥ��
	 * @return
	 */
	public boolean isAtTop(){
	  return getScrollY()<=0;
	}
	  
	/**
	 * �Ƿ񵽴�ײ�
	 * @return
	 */
	public boolean isAtBottom(){
	  return getScrollY()==getChildAt(getChildCount()-1).getBottom()+getPaddingBottom()-getHeight();
	}
	
	/**
	 * 
	 * @param child
	 * @return
	 */
	public boolean isChildVisible(View child){
	  if(child==null){
	      return false;
	  }
	  Rect scrollBounds = new Rect();
	  getHitRect(scrollBounds);
	  return child.getLocalVisibleRect(scrollBounds);
	}
}
