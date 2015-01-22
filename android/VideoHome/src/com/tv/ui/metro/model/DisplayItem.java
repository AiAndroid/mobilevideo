package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    public static class Filter extends LinkedHashMap<String, ArrayList<FilterItem>> implements Serializable{
        private static final long serialVersionUID = 1L;
        public ArrayList<FilterItem> filters(){return get("filters");};
    }

    public static class FilterItem implements Serializable{
        private static final long serialVersionUID = 1L;
        public String name;
        public String fid;
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
        public String entity;
        public String params;
        public String url;

        public String toString(){
            return "url: "+url + " entity:" +entity;
        }
    }

    public static class Hint extends LinkedHashMap<String, String> implements Serializable{
        public String left() {return  get("left");}
        public String mid()  {return  get("mid");}
        public String right(){return  get("right");}

        public String toString(){
            return  "hint left:"+left() + " mid:"+mid() + " right:"+right();
        }
    }


    public String id;
    public String title;
    public String sub_title;
    public Hint   hint;
    public String desc;
	public String ns;
	public String type;
    public Target     target;
	public ImageGroup images;
	public UI         ui_type;
	public Times      times;
    public Media      media;

	public String toString() {
		return " ns:" + ns + " type:" + type + " target=" + target + " id:" + id + " name:" + title + "images:"
				+ images + " _ui:" + ui_type + " hint: "+hint;
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