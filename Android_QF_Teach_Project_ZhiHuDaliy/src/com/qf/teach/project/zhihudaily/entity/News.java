package com.qf.teach.project.zhihudaily.entity;

import java.util.List;

public class News {
	private String date;
	private List<Story> stories;
	private List<TopStory> topStories;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

	public List<TopStory> getTopStories() {
		return topStories;
	}

	public void setTopStories(List<TopStory> topStories) {
		this.topStories = topStories;
	}
}
