package dev.shiro8613.stamprallyplugin.map;

import dev.shiro8613.stamprallyplugin.memory.DataStore;
import dev.shiro8613.stamprallyplugin.utils.json.StampData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomMapRenderer extends MapRenderer {

    private final List<ImagePosition.ImageAndPos> imageAndPosList;

    public CustomMapRenderer(List<ImagePosition.ImageAndPos> imageAndPosList) {
        this.imageAndPosList = imageAndPosList;
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        Image backImage = DataStore.getImageMap().get("background");
        mapCanvas.drawImage(0, 0, backImage);

        imageAndPosList.forEach(imageAndPos ->
                mapCanvas.drawImage(imageAndPos.imagePos.x, imageAndPos.imagePos.y, imageAndPos.image));
    }

    public static void LoadRenderer() {
        Map<Integer, String> map = DataStore.getMapStamp();
        map.forEach((key, value) -> {
            List<ImagePosition.ImageAndPos> imageAndPoses = new ArrayList<>();
            MapView mapView = Bukkit.getMap(key);
            Objects.requireNonNull(StampData.DecodeStamps(value)).forEach((stampName, has) -> {
                if (has) {
                    Image image = DataStore.getImageMap().get(stampName);
                    ImagePosition.ImagePos pos =  ImagePosition.getImagePos().get(stampName);
                    imageAndPoses.add(new ImagePosition.ImageAndPos(image,pos));
                }
            });

            if (mapView != null) {
                removeAndAddRenderer(mapView, new CustomMapRenderer(imageAndPoses));
            }
        });
    }

    public static void ReloadRenderer(int mapId) {
        Map<Integer, String> map = DataStore.getMapStamp();

        List<ImagePosition.ImageAndPos> imageAndPoses = new ArrayList<>();
        MapView mapView = Bukkit.getMap(mapId);
        Objects.requireNonNull(StampData.DecodeStamps(map.get(mapId))).forEach((stampName, has) -> {
            if (has) {
                Image image = DataStore.getImageMap().get(stampName);
                ImagePosition.ImagePos pos =  ImagePosition.getImagePos().get(stampName);
                imageAndPoses.add(new ImagePosition.ImageAndPos(image,pos));
            }
        });

        if (mapView != null) {
            removeAndAddRenderer(mapView, new CustomMapRenderer(imageAndPoses));
        }
    }

    private static void removeAndAddRenderer(MapView mapView, MapRenderer renderer) {
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.addRenderer(renderer);
    }
}
