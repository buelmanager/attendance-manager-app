package com.buel.holyhelper.model;

public class TutorialModel {
    public String title;
    public String content;
    public String summary;
    public int color;
    public int drawable;

    @Override
    public String toString() {
        String s = "title : " + title +
                "\ncontent : " + content +
                "\nsummary : " + summary +
                "\ncolor : " + color +
                "\ndrawable : " + drawable;

        return  s;
    }
}
