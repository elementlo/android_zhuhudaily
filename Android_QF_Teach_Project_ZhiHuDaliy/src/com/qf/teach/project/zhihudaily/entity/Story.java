package com.qf.teach.project.zhihudaily.entity;


public class Story extends AbstractStory {
	private boolean multipic;
	private String[] images;

	public boolean isMultipic() {
		return multipic;
	}

	public void setMultipic(boolean multipic) {
		this.multipic = multipic;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

}
