package com.qf.teach.project.zhihudaily.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qf.teach.project.zhihudaily.R;
import com.qf.teach.project.zhihudaily.c.API;
import com.qf.teach.project.zhihudaily.custom.CustomTitle;
import com.qf.teach.project.zhihudaily.entity.Theme;
import com.qf.teach.project.zhihudaily.entity.ThemeOther;
import com.qf.teach.project.zhihudaily.fragment.ContentFragment;
import com.qf.teach.project.zhihudaily.fragment.MainFragment;

/**
 * ��ҳ
 * @author Lusifer
 *
 * 2014��12��2������10:54:54
 */
public class MainActivity extends FragmentActivity implements Listener<JSONObject>, OnItemClickListener {
	private CustomTitle cTitle;
	
	// SlidingMenu���
	private SlidingMenu menu;
	private ListView lvTheme;
	private Theme theme;
	private MyBaseAdapter adapter;
	
	// �������
	private RequestQueue mQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initSlidingMenu();
	}
	
	/**
	 * ��ʼ�����棨Fragment��
	 */
	private void initView() {
		cTitle = (CustomTitle) findViewById(R.id.custom_title);
		cTitle.setTitle("��ҳ");
		cTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fl_content, MainFragment.newInstance());
		fragmentTransaction.commit();
	}

	/**
	 * ��ʼ��SlidingMenu
	 */
	private void initSlidingMenu() {
		mQueue = Volley.newRequestQueue(getApplicationContext());
		
		// �˵�
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidth(10);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffset(100);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_left);
        
        // ����
        adapter = new MyBaseAdapter();
        lvTheme = (ListView) findViewById(R.id.lv_theme);
        lvTheme.setOnItemClickListener(this);
        lvTheme.setAdapter(adapter);
        mQueue.add(new JsonObjectRequest(Method.GET, API.getThemesUrl(), null, this, null));
	}
	
	/* -------------------- �������� -------------------- */

	@Override
	public void onResponse(JSONObject response) {
		theme = new Theme();
		
		try {
			theme.setLimit(response.getInt("limit"));
			
			// ����Others
			JSONArray jsonArray = response.getJSONArray("others");
			if (jsonArray != null && jsonArray.length() > 0) {
				List<ThemeOther> others = new ArrayList<ThemeOther>();
				
				// �ֶ�������ҳ
				ThemeOther other = new ThemeOther();
				other.setName("��ҳ");
				others.add(other);
				
				// ����
				for (int i = 0 ; i < jsonArray.length() ; i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					other = new ThemeOther();
					other.setColor(obj.getInt("color"));
					other.setDescription(obj.getString("description"));
					other.setId(obj.getLong("id"));
					other.setImage(obj.getString("image"));
					other.setName(obj.getString("name"));
					others.add(other);
				}
				
				theme.setOthers(others);
				
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/* -------------------- �������� -------------------- */
	
	/* -------------------- ListView����¼� -------------------- */
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// �رղ໬�˵�
		menu.toggle();
		
		if (position == 0) { // ������ҳ
			initView();
		} else {
			// ���Fragment
			ThemeOther other = theme.getOthers().get(position);
			cTitle.setTitle(other.getName());
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.fl_content, ContentFragment.newInstance(other.getId(), other.getDescription(), other.getImage()));
			fragmentTransaction.commit();
		}
	}
	
	/* -------------------- ListView����¼� -------------------- */
	
	/**
	 * ThemeAdapter
	 * @author Lusifer
	 *
	 * 2014��12��4������2:11:55
	 */
	class MyBaseAdapter extends BaseAdapter {
		private ViewHolder viewHolder;

		@Override
		public int getCount() {
			return theme == null ? 0 : theme.getOthers().size();
		}

		@Override
		public Object getItem(int position) {
			return theme.getOthers().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_theme, parent, false);
				
				viewHolder = new ViewHolder();
				viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			ThemeOther other = theme.getOthers().get(position);
			viewHolder.txTitle.setText(other.getName());
			
			return convertView;
		}
		
		class ViewHolder {
			public TextView txTitle;
		}
		
	}

}
