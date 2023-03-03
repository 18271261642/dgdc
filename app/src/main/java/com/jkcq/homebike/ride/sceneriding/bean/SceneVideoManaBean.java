package com.jkcq.homebike.ride.sceneriding.bean;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class SceneVideoManaBean {
    ArrayList<String> sceneName;
    ArrayList<String> sceneEnName;
    String url;
    long lenth;
    String path;
    String fileName;
    String picUrl;


    public SceneVideoManaBean(long size, String path) {
        this.lenth = size;
        this.path = path;
        String currentSencFileName = path.substring(path.lastIndexOf("/") + 1);
        this.fileName = currentSencFileName;
        Log.e("SceneVideoManaBean", "path=" + path + "currentSencFileName=" + currentSencFileName);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<String> getSceneEnName() {
        if (sceneEnName == null || sceneEnName.size() == 0) {
            sceneEnName = new ArrayList<>();
        }
        return sceneEnName;
    }

    public void setSceneEnName(ArrayList<String> sceneEnName) {
        this.sceneEnName = sceneEnName;
    }

    public ArrayList<String> getSceneName() {
        Log.e("getSceneName", "" + sceneName);
        if (sceneName == null || sceneName.size() == 0) {
            sceneName = new ArrayList<>();
        }
        return sceneName;
    }

    public void setSceneName(ArrayList<String> sceneName) {
        this.sceneName = sceneName;
    }

    public long getLenth() {
        return lenth;
    }

    public void setLenth(long lenth) {
        this.lenth = lenth;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneVideoManaBean that = (SceneVideoManaBean) o;
        return fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }


    @Override
    public String toString() {
        return "SceneVideoManaBean{" +
                "sceneName=" + sceneName +
                ", sceneEnName=" + sceneEnName +
                ", url='" + url + '\'' +
                ", lenth=" + lenth +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
