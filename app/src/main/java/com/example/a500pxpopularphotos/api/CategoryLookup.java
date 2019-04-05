package com.example.a500pxpopularphotos.api;

import android.util.SparseArray;

import java.util.HashMap;

public class CategoryLookup {
    HashMap<Integer, String> mCategory = new HashMap<>();
    public CategoryLookup() {
        mCategory.put(0,"Uncategorized");
        mCategory.put(10,"Abstract");
        mCategory.put(29,"Aerial");
        mCategory.put(11,"Animals");
        mCategory.put(5,"Black and White");
        mCategory.put(1,"Celebrities");
        mCategory.put(9,"City and Architecture");
        mCategory.put(15,"Commercial");
        mCategory.put(16,"Concert");
        mCategory.put(20,"Family");
        mCategory.put(14,"Fashion");
        mCategory.put(2,"Film");
        mCategory.put(24,"Fine Art");
        mCategory.put(23,"Food");
        mCategory.put(3,"Journalism");
        mCategory.put(8,"Landscapes");
        mCategory.put(12,"Macro");
        mCategory.put(18,"Nature");
        mCategory.put(30,"Night");
        mCategory.put(4,"Nude");
        mCategory.put(7,"People");
        mCategory.put(19,"Performing Arts");
        mCategory.put(17,"Sport");
        mCategory.put(6,"Still Life");
        mCategory.put(21,"Street");
        mCategory.put(26,"Transportation");
        mCategory.put(13,"Travel");
        mCategory.put(22,"Underwater");
        mCategory.put(27,"Urban Exploration");
        mCategory.put(25,"Wedding");
    }

    public String getCategory(int id) {
        return mCategory.get(id);
    }
}
