package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liuhuadong on 12/1/14.
 */
public class VideoItem extends DisplayItem {
    private static final long serialVersionUID = 1L;

    public ArrayList<Category>    category;
    public ArrayList<Director>    directors;
    public ArrayList<Actor>       actors;
    public String                 description;
    public String                 score;
    public ArrayList<DisplayItem> related;

    public ArrayList<Video>       videos;
    public String                 producer;
    public String                 publisher;
    public String                 phrase;

    public Object                 clickObject;

    public Media      media;

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
        public String  poster;


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

    public static class Actor implements Serializable {
        private static final long serialVersionUID = 1L;
        public String id;
        public String name;

        public Actor clone(){
            Actor item = new Actor();
            item.id = id;
            item.name = name;

            return item;
        }
    }

    public static class Director implements Serializable{
        private static final long serialVersionUID = 1L;
        public String id;
        public String name;

        public Director clone(){
            Director item = new Director();
            item.id = id;
            item.name = name;

            return item;
        }
    }

    public static class Category implements Serializable{
        private static final long serialVersionUID = 1L;
        public String id;
        public String name;

        public String toString(){
            return "\ncategory:  id="+id + " name="+name;
        }
    }

    public static class Score implements Serializable{
        private static final long serialVersionUID = 1L;
        public String average;
        public int    count;
    }

    public static class Video implements Serializable{
        private static final long serialVersionUID = 1L;
        public String                  id;
        public int                     play_time;
        public String                  name;
        public String                  desc;
        public Times                   times;
        public HashMap<String, String> urls;
    }
}
