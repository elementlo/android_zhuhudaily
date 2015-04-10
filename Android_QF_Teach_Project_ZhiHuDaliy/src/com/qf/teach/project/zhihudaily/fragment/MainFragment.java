package com.qf.teach.project.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qf.teach.project.zhihudaily.R;
import com.qf.teach.project.zhihudaily.c.API;
import com.qf.teach.project.zhihudaily.custom.CustomSlideAndList;


public class MainFragment extends Fragment {
	// ¿Ø¼þÏà¹Ø
	private CustomSlideAndList cSlideList;
	
	public static MainFragment newInstance() {
		MainFragment f = new MainFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		
		cSlideList = (CustomSlideAndList) view.findViewById(R.id.custom_slide_list);
		cSlideList.init(API.getLatestUrl());
		
		return view;
	}
}
