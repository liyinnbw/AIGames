package com.example.yoyo.firstapp;

/**
 * Created by yoyo on 2015/7/24.
 */
public class Point {
    private float x;
    private float y;

    public Point(int x, int y){
        setX(x);
        setY(y);
    }
    public void setX(int x){
        this.x=x;
    }
    public void setY(int y){
        this.y=y;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    @Override
    public String toString(){
        return "("+getX()+","+getY()+")";
    }
}
