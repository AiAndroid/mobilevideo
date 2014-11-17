package com.tv.ui.metro.model;

import java.io.Serializable;

public class DisplayItem implements Serializable{
	private static final long serialVersionUID = 1L;

	public static class UI implements Serializable {
		private static final long serialVersionUID = 1L;
        public String name;
        public int    id;
        public UI clone(){
            UI item = new UI();
            item.name = name;
            item.id   = id;
            return item;
        }
		public String toString() {
			return " type:" + name + "  id:" + id;
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