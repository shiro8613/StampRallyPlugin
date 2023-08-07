package dev.shiro8613.stamprallyplugin.map;

import dev.shiro8613.stamprallyplugin.utils.json.StampData;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagePosition {

    public static Map<String, ImagePos> getImagePos() {
        Map<String, ImagePos> map = new HashMap<>();
        List<String> names = StampData.getKeyNames();

        int X = 13;
        int Y = 23;

        for (int i = 0; i <= names.size(); i++) {
            if ( i != 0 && i % 3 == 0) {
                X += 37;
                Y += 37;
            }

            map.put(names.get(i), new ImagePos(X, Y));
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
