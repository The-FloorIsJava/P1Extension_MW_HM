package com.revature.P1.Model;

public class User {

        private String userName;
        private String password;
        private String position;

        public User(String userName, String password , String position) {

            this.userName = userName;
            this.password = password;
            this.position = position;
        }

    public User(String userName, String password) {
            this(userName,password,"employee");
    }

    public User() {

    }

    public String getUserName(){
            return this.userName;
        }

        public void setUserName(String userName){
            this.userName = userName;
        }

        public String getPassword(){
            return password;
        }

        public void setPassword(String password){
            this.password = password;
        }

        public String getPosition(){
            return position;
        }

        public void setPosition(String position){
            this.position = position;
        }

//        public boolean isValidUserName(String userName) {
//            if (userName == null) {
//                return false;
//            }
//            return this.userName != null && this.userName.equals(userName);
//        }
//
//        public boolean isValidPassword(String password) {
//            if (password == null) {
//                return false;
//            }
//            return this.password != null && this.password.equals(password);
//        }
//

        @Override
        public String toString() {
            return "UserTable{" +
                    "User Name=" + userName +
                    ", Password='" + password + '\'' +
                    ", Position=" + position +
                    '}';
        }



}
