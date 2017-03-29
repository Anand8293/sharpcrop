package com.anandroid.randd.Entity;

import android.content.Context;

import com.anandroid.randd.utils.UserPrefs;


/**
 * Created by HP 15 on 21-03-2016.
 */
public class User {

    private String phone_number;

    private String user_unique_id;

    private String role_id;

    private String imei;

    private String device_id;

    private String profileImageThumbPath;

    private String conversation_count;

    private String country;

    private String ip;

    private String last_login;

    private String id;

    private String first_name;

    private String profileImagePath;

    private String friend_count;

    private String backgroundImagePath;

    private String email;

    private String name;

    private String dob;

    private String last_name;

    private String gender;

    private String login_type;

    private String backgroundImageThumbPath;
    private String totalCrown;
    private String totalPost;
    private String Followers;
    private String Following;



    public String getPhone_number ()
    {
        return phone_number;
    }

    public void setPhone_number (String phone_number)
    {
        this.phone_number = phone_number;
    }

    public String getRole_id ()
    {
        return role_id;
    }

    public void setRole_id (String role_id)
    {
        this.role_id = role_id;
    }

    public String getImei ()
    {
        return imei;
    }

    public void setImei (String imei)
    {
        this.imei = imei;
    }

    public String getDevice_id ()
    {
        return device_id;
    }

    public void setDevice_id (String device_id)
    {
        this.device_id = device_id;
    }

    public String getProfileImageThumbPath ()
    {
        return profileImageThumbPath;
    }

    public void setProfileImageThumbPath (String profileImageThumbPath)
    {
        this.profileImageThumbPath = profileImageThumbPath;
    }

    public String getConversation_count ()
    {
        return conversation_count;
    }

    public void setConversation_count (String conversation_count)
    {
        this.conversation_count = conversation_count;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getIp ()
    {
        return ip;
    }

    public void setIp (String ip)
    {
        this.ip = ip;
    }

    public String getLast_login ()
    {
        return last_login;
    }

    public void setLast_login (String last_login)
    {
        this.last_login = last_login;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getProfileImagePath ()
    {
        return profileImagePath;
    }

    public void setProfileImagePath (String profileImagePath)
    {
        this.profileImagePath = profileImagePath;
    }

    public String getFriend_count ()
    {
        return friend_count;
    }

    public void setFriend_count (String friend_count)
    {
        this.friend_count = friend_count;
    }

    public String getBackgroundImagePath ()
    {
        return backgroundImagePath;
    }

    public void setBackgroundImagePath (String backgroundImagePath)
    {
        this.backgroundImagePath = backgroundImagePath;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getLogin_type ()
    {
        return login_type;
    }

    public void setLogin_type (String login_type)
    {
        this.login_type = login_type;
    }

    public String getBackgroundImageThumbPath ()
    {
        return backgroundImageThumbPath;
    }

    public void setBackgroundImageThumbPath (String backgroundImageThumbPath)
    {
        this.backgroundImageThumbPath = backgroundImageThumbPath;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [phone_number = "+phone_number+", role_id = "+role_id+", imei = "+imei+", device_id = "+device_id+", profileImageThumbPath = "+profileImageThumbPath+", conversation_count = "+conversation_count+", country = "+country+", ip = "+ip+", last_login = "+last_login+", id = "+id+", first_name = "+first_name+", profileImagePath = "+profileImagePath+", friend_count = "+friend_count+", backgroundImagePath = "+backgroundImagePath+", email = "+email+", name = "+name+", dob = "+dob+", last_name = "+last_name+", gender = "+gender+", login_type = "+login_type+", backgroundImageThumbPath = "+backgroundImageThumbPath+"]";
    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    public String getTotalCrown() {
        return totalCrown;
    }

    public void setTotalCrown(String totalCrown) {
        this.totalCrown = totalCrown;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public String getFollowers() {
        return Followers;
    }

    public void setFollowers(String followers) {
        Followers = followers;
    }

    public String getFollowing() {
        return Following;
    }

    public void setFollowing(String following) {
        Following = following;
    }

    public void updateUser(Context context)
    {
        UserPrefs.setFname(getFirst_name(), context);
        UserPrefs.setLname(getLast_name(), context);
        UserPrefs.setUserId(getId(), context);
        UserPrefs.setEmail(getEmail(),context);
        UserPrefs.setProfileImage(getProfileImageThumbPath(), context);
        UserPrefs.setHeaderImage(getBackgroundImageThumbPath(),context);
        UserPrefs.setUserUniqueId(getUser_unique_id().toString(), context);
    }
}
