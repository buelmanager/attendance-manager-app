package com.buel.holyhelper.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 19001283 on 2018-06-01.
 */

public class HolyModel {
    @Override
    public String toString() {
        return "HolyModel{" +
                "password='" + password + '\'' +
                ", adminUid='" + adminUid + '\'' +
                ", adminName='" + adminName + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", adminPhone='" + adminPhone + '\'' +
                ", address='" + address + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", owner='" + owner + '\'' +
                ", phone='" + phone + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", point=" + point +
                ", cumulativePoint=" + cumulativePoint +
                ", groupModel=" + group +
                ", memberModel=" + members +
                '}';
    }

    public String password;
    public String adminUid;
    public String adminName;
    public String adminEmail;
    public String adminPhone;
    public String address;
    public String uid;
    public String name;
    public String addressDetail;
    public String owner;
    public String phone;
    public String imgUrl;
    public int point;
    public int cumulativePoint;
    public Map<String, groupModel> group;
    public Map<String, memberModel> members;

    public Map<String, Object> convertMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("password", password);
        map.put("owner", owner);
        map.put("phone", phone);
        map.put("address", address);
        map.put("adminUid", adminUid);
        map.put("adminName", adminName);
        map.put("adminEmail", adminEmail);
        map.put("adminPhone", adminPhone);
        map.put("addressDetail", addressDetail);
        map.put("uid", uid);
        map.put("name", name);
        map.put("imgUrl", imgUrl);
        map.put("point", point);
        map.put("cumulativePoint", cumulativePoint);
        return map;
    }

    public static class memberModel {
        public String name;
        public String attend;
        public String phone;
        public String address;
        public String gender;
        public String position;
        public String birth;
        public String isNew;
        public String town;
        public String memberRegistDate;
        public String leader;
        public String uid;
        public String imgUrl;
        public String corpsUID;
        public String corpsName;
        public String groupUID;
        public String groupName;
        public String teamUID;
        public String teamName;
        public String userPhotoUri;
        public String noAttendReason;
        public String isExecutives;

        @Override
        public String toString() {
            return "memberModel{" +
                    "name='" + name + '\'' +
                    ", attend='" + attend + '\'' +
                    ", phone='" + phone + '\'' +
                    ", address='" + address + '\'' +
                    ", gender='" + gender + '\'' +
                    ", position='" + position + '\'' +
                    ", birth='" + birth + '\'' +
                    ", isNew='" + isNew + '\'' +
                    ", town='" + town + '\'' +
                    ", memberRegistDate='" + memberRegistDate + '\'' +
                    ", leader='" + leader + '\'' +
                    ", uid='" + uid + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", corpsUID='" + corpsUID + '\'' +
                    ", corpsName='" + corpsName + '\'' +
                    ", groupUID='" + groupUID + '\'' +
                    ", groupName='" + groupName + '\'' +
                    ", teamUID='" + teamUID + '\'' +
                    ", teamName='" + teamName + '\'' +
                    ", userPhotoUri='" + userPhotoUri + '\'' +
                    ", noAttendReason='" + noAttendReason + '\'' +
                    ", isExecutives='" + isExecutives + '\'' +
                    '}';
        }

        public Map<String, Object> convertMap() {
            //memberRegistDate 와 uid 는 제외한다.
            Map<String, Object> map = new HashMap<>();
            map.put("attend", attend);
            map.put("name", name);
            map.put("phone", phone);
            map.put("address", address);
            map.put("gender", gender);
            map.put("position", position);
            map.put("birth", birth);
            map.put("isNew", isNew);
            map.put("town", town);
            map.put("memberRegistDate", memberRegistDate);
            map.put("leader", leader);
            map.put("uid", uid);
            map.put("imgUrl", imgUrl);
            map.put("corpsUID", corpsUID);
            map.put("corpsName", corpsName);
            map.put("groupUID", groupUID);
            map.put("groupName", groupName);
            map.put("teamUID", teamUID);
            map.put("teamName", teamName);
            map.put("userPhotoUri", userPhotoUri);
            map.put("noAttendReason", noAttendReason);
            map.put("isExecutives", isExecutives);
            return map;
        }

        ;
    }

    public static class groupModel {
        @Override
        public String toString() {
            return "groupModel{" +
                    "uid='" + uid + '\'' +
                    ", etc='" + etc + '\'' +
                    ", leader='" + leader + '\'' +
                    ", name='" + name + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", teamModel=" + team +
                    '}';
        }

        public String uid;
        public String etc;
        public String leader;
        public String name;
        public String imgUrl;
        public Map<String, teamModel> team;

        public Map<String, Object> convertMap() {
            //memberRegistDate 와 uid 는 제외한다.
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            map.put("etc", etc);
            map.put("leader", leader);
            map.put("name", name);
            map.put("imgUrl", imgUrl);
            return map;
        }

        public static class teamModel {
            @Override
            public String toString() {
                return "teamModel{" +
                        "uid='" + uid + '\'' +
                        ", etc='" + etc + '\'' +
                        ", leader='" + leader + '\'' +
                        ", name='" + name + '\'' +
                        ", imgUrl='" + imgUrl + '\'' +
                        '}';
            }

            public String uid;
            public String etc;
            public String leader;
            public String name;
            public String imgUrl;

            public Map<String, Object> convertMap() {
                //memberRegistDate 와 uid 는 제외한다.
                Map<String, Object> map = new HashMap<>();
                map.put("uid", uid);
                map.put("etc", etc);
                map.put("leader", leader);
                map.put("name", name);
                map.put("imgUrl", imgUrl);
                return map;
            }
        }
    }
}
