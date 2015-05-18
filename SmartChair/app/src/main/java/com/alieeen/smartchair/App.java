package com.alieeen.smartchair;

/**
 * Created by alinekborges on 18/05/15.
 */
public class App {
    private static App ourInstance = new App();

    public static App getInstance() {
        return ourInstance;
    }

    private App() {
    }
}
