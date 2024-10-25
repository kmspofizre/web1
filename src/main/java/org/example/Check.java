package org.example;

public class Check {
    public static boolean checkCoordinates(float x, int y, float r){
        return checkCircle(x, y, r) || checkRect(x, y, r) || checkTriangle(x, y, r);
    }

    public static boolean checkTriangle(float x, int y, float r){
        return (x <= 0) && (y >= 0) && (x >= -r) && (y <= r / 2) && (((Math.abs(x) + 2 * Math.abs(y))) <= r);
    }

    public static boolean checkRect(float x, int y, float r){
        return (x >= 0) && (y >= 0) && (x <= r) && (y <= r);
    }

    public static boolean checkCircle(float x, int y, float r){
        return (x <= 0) && (y <= 0) && (Math.sqrt(x * x + y * y) <= r / 2);
    }

    public static boolean checkValues(float x, int y, float r){
        return (-3 <= x) && (x <= 3) && (-5 <= y) && (y <= 3) && (1 <= r) && (r <= 3);
    }
}
