package com.example.toron.Retrofit.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person
{
    @Expose
    @SerializedName("id") private int id;

    @Expose
    @SerializedName("name") private String name;

    @Expose
    @SerializedName("hobby") private String hobby;

    @Expose
    @SerializedName("success") private Boolean success;

    @Expose
    @SerializedName("message") private String message;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getHobby()
    {
        return hobby;
    }

    public void setHobby(String hobby)
    {
        this.hobby = hobby;
    }

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}