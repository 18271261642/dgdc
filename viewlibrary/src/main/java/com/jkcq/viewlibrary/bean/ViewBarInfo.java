package com.jkcq.viewlibrary.bean;

public class ViewBarInfo {
    int viewHeight;
    int startIndex;
    int endIndex;
    float width;


    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public ViewBarInfo() {

    }
    public ViewBarInfo(int viewHeight,int viewWidth){
        this.viewHeight=viewHeight;
        this.width=viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public ViewBarInfo(int viewHeight, int startIndex, int endIndex) {
        this.viewHeight = viewHeight;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public String toString() {
        return "ViewBarInfo{" +
                "viewHeight=" + viewHeight +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", width=" + width +
                '}';
    }
}
