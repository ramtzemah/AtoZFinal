package com.example.atoz2;

import java.util.Map;

public class User {


        private String name;
        private String email;
        private String password;
        private String userId;
        private Map<Integer,Integer> scores;
        private boolean premium;

        public User(String name, String email, String password, boolean premium) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.premium = premium;
        }

        public User() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isPremium() {
            return premium;
        }

        public void setPremium(boolean premium) {
            this.premium = premium;
        }

        public Map<Integer, Integer> getScores() {
            return scores;
        }

        public void setScores(Map<Integer, Integer> scores) {
            this.scores = scores;
        }
}
