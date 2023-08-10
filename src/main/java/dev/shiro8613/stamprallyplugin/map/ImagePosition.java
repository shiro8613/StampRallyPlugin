package dev.shiro8613.stamprallyplugin.map;

import dev.shiro8613.stamprallyplugin.utils.json.StampData;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagePosition {

    public static Map<Integer, ImagePos> getImagePos() {
        Map<Integer, ImagePos> map = new HashMap<>();
        List<Integer> names = StampData.getKeyNames();

        int X = 0;
        int Y = 0;

        for (int i = 0; i < names.size(); i++) {
            if ( i != 0 && i % 3 == 0) {
                X = 0;
                Y += 42;
            }

            map.put(names.get(i), new ImagePos(X, Y));

            X += 42;
        }

        return map;
    }

    public static class ImagePos {
        public int x;
        public int y;

        public ImagePos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class ImageAndPos {
        public Image image;
        public ImagePos imagePos;

        public ImageAndPos(Image image, ImagePos imagePos) {
            this.image = image;
            this.imagePos = imagePos;
        }
    }
}
