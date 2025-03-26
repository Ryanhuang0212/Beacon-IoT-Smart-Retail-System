package com.example.groupproject;
import android.app.Application;
public class GlobalVariable extends Application{
    private  String Name;

    private  String Sum;
    public void setName(String name){
        this.Name = name;
    }
    public String getName(){
        return Name;
    }
    public void setSum(String sum){
        this.Sum = sum;
    }
    public String getSum(){
        return Sum;
    }

}
