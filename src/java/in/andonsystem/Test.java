/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.andonsystem;

import in.andonsystem.services.MiscService;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.sql.Timestamp;

/**
 *
 * @author Md Zahid Raza
 */
public class Test {
    
     public static void main(String[] args){
        String x = "0000";
        System.out.println(Integer.parseInt(x));
    }
    
    /*
    public static final String TIME_SERVER = "time-a.nist.gov";
    
    public static void main(String[] args) throws Exception {
        long timenow = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        Date date1 = new Date(timenow);
        String time = df.format(date1);
        
        Date date3 = df.parse(time);
        long timeNow2 = date3.getTime();
        
        
        
        Date date2 = new Date(timeNow2);
        String time2 = df.format(date2);
        
        System.out.println(timenow);
        System.out.println(timeNow2);
        System.out.println(timenow-timeNow2);
        System.out.println(time);
        System.out.println(time2);
        System.out.println(date1.toString());
        System.out.println(date2.toString());
        
        
    }
    */
    /*
    public static void display(TreeSet<User> set){
        
        Iterator<User> itr = set.iterator();
        User user;
        while(itr.hasNext()){
            user = itr.next();
            System.out.println("userId : " + user.getUserId() + ", Age : " + user.getAge());
        }
        System.out.println();
    }*/
    
    //ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
   
    
    
}
 

class Schedular{
    private final ScheduledExecutorService scheduler =
     Executors.newScheduledThreadPool(1);

    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() { System.out.println("beep"); }
        };
        final ScheduledFuture<?> beeperHandle =
            scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
        
            scheduler.schedule(new Runnable() {
                public void run() { beeperHandle.cancel(true); }
            }, 60, SECONDS);
   }
}
        /* 
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable loginMessage = new Runnable(){
                public void run(){
                    System.out.println("Welcome");
                }
            };
        
        
        final ScheduledFuture<?> loginHandle = scheduler.scheduleAtFixedRate(loginMessage,0, 2, TimeUnit.SECONDS);
        scheduler.schedule(new Runnable(){
            public void run(){
                loginHandle.cancel(true);
            }
        }, 1, TimeUnit.DAYS);
        
        */

class User implements Comparable{
    
    private int userId;
    private int age;
    
    public User(){}

    public User(int userId, int age) {
        this.userId = userId;
        this.age = age;
    } 
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User)obj;
        if(this.userId == user.userId)
            return true;
        else
            return false;
    }
    
    

    @Override
    public int compareTo(Object obj) {
        User user = (User)obj;
        int result = this.userId - user.userId;
        if(result == 0){
            result = this.age - user.age;
        }
        return result;
    }
    
}
