package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Application;

import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        HexTile a = new HexTile(0,0,0);
        HexTile b = new HexTile(1,0,-1);

        Set<HexTile>  s = new TreeSet<>();



        System.out.println("Hello world!");
        Application.launch(App.class, args);
    }
}