package com.tv.ui.metro.model;

import java.io.Serializable;

public class DisplayItem implements Serializable{
	private static final long serialVersionUID = 2L;

	public static class UI implements Serializable {
		private static final long serialVersionUID = 4L;
        public String name;
        public int    id;
        public int    row_count;
        public int    display_count;
        public int    show_score;
        public int    show_rank;
        public UI clone(){
            UI item = new UI();
            item.name = name;
            item.id   = id;
            item.row_count = row_count;
            item.show_rank = show_rank;
            item.show_score = show_score;
            return item;
        }
		public String toString() {
			return " type:" + name + "  id:" + id;
		}
	}

    public static class Media implements Serializable {
        private static final long serialVersionUID = 1L;
        public long  likes;
        public long  unlikes;
        public long  comments;
        public long  play_times;
        public float duration;
        public float  rate;
        public String episode; //update to 24
        public String label;   //corner;
        public CP      cp;
        public Fee     fee;
        public Version version;


        public static class CP implements Serializable{
            private static final long serialVersionUID = 1L;
            String name;
            String versrion_code;
        }

        public static class Fee implements Serializable{
            private static final long serialVersionUID = 1L;
            String name;
            String pay_load;
        }

        public static class Version implements Serializable{
            private static final long serialVersionUID = 1L;
            String latest_version;
            String min_version;
        }

        public Media clone(){
            //TODO
            return new Media();
        }
    }

	public static class Times implements Serializable {
		private static final long serialVersionUID = 1L;
		public long updated;
		public long created;

        public Times clone(){
            Times item = new Times();
            item.created = created;
            item.updated = updated;
            return item;
        }
	}
    public static class Target implements Serializable{
        private static final long serialVersionUID = 1L;
        public String type;
        public String params;


        public Target clone(){
            Target item = new Target();
            item.type = type;
            item.params = params;
            return  item;
        }
    }


    public String id;
    public String title;
    public String sub_title;
    public String desc;
	public String ns;
	public String type;
    public Target     action;
	public ImageGroup images;
	public UI         ui_type;
	public Times      times;
    public Media      media;

    public DisplayItem clone(){
        DisplayItem item = new DisplayItem();

        if(action != null)item.action = action.clone();

        item.ns   = this.ns;
        item.type = this.type;
        item.id   = this.id;
        item.title = this.title;
        item.sub_title = this.sub_title;
        item.desc      = this.desc;
        if(images != null)item.images = this.images.clone();
        if(ui_type != null)item.ui_type = this.ui_type.clone();
        if(times!= null)item.times = times.clone();
        if(media!= null)item.media = media.clone();

        return  item;
    }
	public String toString() {
		return " ns:" + ns + " type:" + type + " target=" + action + " id:" + id + " name:" + title + "images:"
				+ images + " _ui:" + ui_type;
	}

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(obj instanceof DisplayItem){
            return  ((DisplayItem)obj).id.equals(id);
        }

        return  false;
    }
}