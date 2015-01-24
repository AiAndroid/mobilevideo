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
        public int    show_rank = 1;
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

    public static class Filter  implements Serializable{
        private static final long serialVersionUID = 1L;
        public ArrayList<FilterItem> filters(){return filters;}

        public ArrayList<FilterItem> filters;
        public ArrayList<FilterType> all;
        public String                custom_filter_id_format;

        public static class FilterType implements Serializable{
            private static final long serialVersionUID = 1L;

            public String type;
            public String title;
            public ArrayList<String> tags;
        }
    }

    public static class FilterItem implements Serializable{
        private static final long serialVersionUID = 1L;
        public String title;
        public Target target;
        public String type;

        public static String custom_filter = "custom_filter";

        @Override
        public boolean equals(Object obj) {
            if(obj == null)
                return false;

            if(obj instanceof FilterItem){
                return  ((FilterItem)obj).title.equals(title);
            }

            return  false;
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

    public String     value;

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