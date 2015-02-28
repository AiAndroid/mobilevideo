package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DisplayItem implements Serializable{
	private static final long serialVersionUID = 2L;

	public static class UI implements Serializable {
		private static final long serialVersionUID = 4L;
        public String name;
        public int    id;
        public int    row_count;
        public int    display_count;
        public int    show_value = 1;
        public int    show_rank  = 1;
        public UI clone(){
            UI item = new UI();
            item.name = name;
            item.id   = id;
            item.row_count = row_count;
            item.show_rank = show_rank;
            item.show_value = show_value;
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
        public String mid()  {return  get("center");}
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
    public Media      media;//why put here, for media detail episode list UI create, actually we should put this in VideoItem

    public Settings   settings;//for server key-value settings
    public Meta       meta;

    public static class Meta extends HashMap<String, String> implements Serializable{
        private static final long serialVersionUID = 1L;
        public String page(){String page = get("page"); if(page == null) page="0"; return page;}
    }

    public static class Settings  extends HashMap<String, String> implements Serializable{
        private static final long serialVersionUID = 1L;

        public static final String edit_mode = "edit_mode";
        public static final String selected  = "selected";
        public static final String offset    = "offset";
    }

    public static class Media implements Serializable {
        private static final long serialVersionUID = 1L;

        public String description;
        public float  score;
        public Tags   tags;

        public ArrayList<Episode>  items;
        public Stuff               stuff;
        public String              poster;
        public String              date;
        public String              phrase;
        public ArrayList<CP>       cps;
        public String              id;
        public String              name;
        public DisplayLayout       display_layout;
        public String              category_name;

        public static class DisplayLayout implements Serializable{
            private static final long serialVersionUID = 1L;
            public String type        = "tv";
            public int    max_display = 8;

            public static final String TYPE_TV      = "tv";
            public static final String TYPE_OFFLINE = "offline";
            public static final String TYPE_VARIETY = "variety";
        }

        public static class Tags extends HashMap<String, ArrayList<String>> implements Serializable{
            private static final long serialVersionUID = 1L;
            public ArrayList<String> genre(){return  get("director");}
            public ArrayList<String> year(){return get("writer");}
            public ArrayList<String> language(){return get("actor");}
            public ArrayList<String> area(){return get("area");}
        }

        public static class CP /*extends HashMap<String, String>*/ implements Serializable{
            private static final long serialVersionUID = 1L;
            public String cp;
            public String name;
            public String icon;
            public String app_icon; //for download
            public String apk_url;  //for download
            public boolean vitem_offline; //
        }

        public static class Episode /*extends HashMap<String, String> */implements Serializable{
            private static final long serialVersionUID = 1L;
            public String                  date;
            public int                     episode;
            public String                  id;
            public String                  name;
            public int                     download_trys=0;

            @Override
            public boolean equals(Object obj) {
                if(obj == null)
                    return false;

                if(obj instanceof Episode){
                    return  ((Episode)obj).id.equals(id);
                }

                return  false;
            }

            public String toString(){
                return "episode:"+episode + " id:"+id + " name:"+name+ " date:"+date;
            }
        }

        public static class Stuff extends HashMap<String, ArrayList<Stuff.Star>>  implements Serializable{
            private static final long serialVersionUID = 1L;
            public ArrayList<Stuff.Star> director(){return  get("director");}
            public ArrayList<Stuff.Star> writer()  {return get("writer");}
            public ArrayList<Stuff.Star> actor()   {return get("actor");}

            public static class Star implements Serializable {
                private static final long serialVersionUID = 1L;
                public String id;
                public String name;
            }
        }
    }

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