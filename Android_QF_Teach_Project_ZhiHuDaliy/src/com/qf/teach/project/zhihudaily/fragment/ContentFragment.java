package com.qf.teach.project.zhihudaily.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.qf.teach.project.zhihudaily.R;
import com.qf.teach.project.zhihudaily.activity.ContentActivity;
import com.qf.teach.project.zhihudaily.c.API;
import com.qf.teach.project.zhihudaily.cache.BitmapCache;
import com.qf.teach.project.zhihudaily.custom.CustomListViewForScrollView;
import com.qf.teach.project.zhihudaily.entity.Editor;
import com.qf.teach.project.zhihudaily.entity.Story;
import com.qf.teach.project.zhihudaily.entity.ThemeStory;

public class ContentFragment extends Fragment implements Listener<JSONObject>, OnItemClickListener {
	private long id;
	private String description;
	private String image;
	
	// 控件相关
	private TextView txDesc;
	private NetworkImageView imgContent;
	private CustomListViewForScrollView lvTheme;
	private MyBaseAdapter adapter;
	private ScrollView svContent;
	
	// 网络相关
	private RequestQueue mQueue;
	
	// 数据相关
	private ThemeStory themeStory;
	
	public static ContentFragment newInstance(long id, String description, String image) {
		ContentFragment f = new ContentFragment();
		Bundle args = new Bundle();
		args.putLong("id", id);
		args.putString("description", description);
		args.putString("image", image);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		id = getArguments().getLong("id");
		description = getArguments().getString("description");
		image = getArguments().getString("image");
		
		mQueue = Volley.newRequestQueue(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_content, container, false);
		
		txDesc = (TextView) view.findViewById(R.id.tx_desc);
		txDesc.setText(description);
		
		imgContent = (NetworkImageView) view.findViewById(R.id.img_content);
		imgContent.setImageUrl(image, new ImageLoader(mQueue, new BitmapCache()));
		
		adapter = new MyBaseAdapter();
		lvTheme = (CustomListViewForScrollView) view.findViewById(R.id.lv_theme);
		lvTheme.setOnItemClickListener(this);
		lvTheme.setAdapter(adapter);
		
		svContent = (ScrollView) view.findViewById(R.id.sv_content);
		
		// 初始化数据
		initData();
		
		return view;
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mQueue.add(new JsonObjectRequest(Method.GET, String.format(API.getTheme(), id), null, this, null));
	}
	
	/* -------------------- 网络请求 -------------------- */
	
	@Override
	public void onResponse(JSONObject response) {
		try {
			themeStory = new ThemeStory();
			themeStory.setDescription(response.getString("description"));
			themeStory.setBackground(response.getString("background"));
			themeStory.setImage(response.getString("image"));
			themeStory.setColor(response.getInt("color"));
			themeStory.setImage_source(response.getString("image_source"));
			themeStory.setName(response.getString("name"));
			
			// 解析stories
			JSONArray arrayStories = response.getJSONArray("stories");
			if (arrayStories != null && arrayStories.length() > 0) {
				List<Story> stories = new ArrayList<Story>();
				for (int i = 0 ; i < arrayStories.length() ; i++) {
					JSONObject obj = arrayStories.getJSONObject(i);
					Story story = new Story();
					story.setType(obj.getInt("type"));
					story.setId(obj.getLong("id"));
					story.setShare_url(obj.getString("share_url"));
					story.setTitle(obj.getString("title"));
					
					if (obj.has("multipic")) {
						story.setMultipic(obj.getBoolean("multipic"));
					}
					
					// 图片数组
					if (obj.has("images")) {
						JSONArray array = obj.getJSONArray("images");
						if (array != null && array.length() > 0) {
							String[] images = new String[array.length()];
							for (int x = 0 ; x < array.length() ; x++) {
								images[x] = array.getString(x);
							}
							story.setImages(images);
						}
					}
					
					stories.add(story);
				}
				
				themeStory.setStories(stories);
			}
			
			// 解析editors
			JSONArray arrayEditors = response.getJSONArray("editors");
			if (arrayEditors != null && arrayEditors.length() > 0) {
				List<Editor> editors = new ArrayList<Editor>();
				for (int i = 0 ; i < arrayEditors.length() ; i++) {
					JSONObject obj = arrayEditors.getJSONObject(i);
					Editor editor = new Editor();
					editor.setAvatar(obj.getString("avatar"));
					editor.setId(obj.getLong("id"));
					editor.setName(obj.getString("name"));
					editors.add(editor);
				}
				
				themeStory.setEditors(editors);
			}
			
			// 通知数据发生改变
			adapter.notifyDataSetChanged();
			
			svContent.smoothScrollTo(0, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/* -------------------- 网络请求 -------------------- */

	class MyBaseAdapter extends BaseAdapter {
		private ViewHolder viewHolder;

		@Override
		public int getCount() {
			return themeStory == null ? 0 : themeStory.getStories().size();
		}

		@Override
		public Object getItem(int position) {
			return themeStory.getStories().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_theme_news, parent, false);
				
				viewHolder = new ViewHolder();
				viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
				viewHolder.imgThumb = (NetworkImageView) convertView.findViewById(R.id.img_thumb);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			Story story = themeStory.getStories().get(position);
			viewHolder.txTitle.setText(story.getTitle());
			viewHolder.imgThumb.setVisibility(View.GONE);
			if (story.getImages() != null && story.getImages().length > 0) {
				viewHolder.imgThumb.setImageUrl(story.getImages()[0], new ImageLoader(mQueue, new BitmapCache()));
				viewHolder.imgThumb.setVisibility(View.VISIBLE);
			}
			
			return convertView;
		}
		
		class ViewHolder {
			public TextView txTitle;
			public NetworkImageView imgThumb;
		}
		
	}

/* -------------------- ListView监听 -------------------- */
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Story story = themeStory.getStories().get(position);
		Intent intent = new Intent(getActivity(), ContentActivity.class);
		intent.putExtra("id", story.getId());
		intent.putExtra("tag", "content");
		getActivity().startActivity(intent);
	}
	
	/* -------------------- ListView监听 -------------------- */
}
