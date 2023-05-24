import engine.utils.ImageStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestImageStorage {
    @Test
    void testEmpty() {
        ImageStorage imageStorage = new ImageStorage();
        assertEquals(0, imageStorage.getStorageSize());
    }

    @Test
    void testOneImage() {
        ImageStorage imageStorage = new ImageStorage("src/main/resources/img/heal/herb.png");
        assertEquals(1, imageStorage.getStorageSize());
    }

    @Test
    void testListOfImages() {
        ArrayList<String> imagesNames = new ArrayList<>();
        imagesNames.add("src/main/resources/img/weapons/airBook.png");
        imagesNames.add("src/main/resources/img/weapons/fireBook.png");
        imagesNames.add("src/main/resources/img/weapons/SRankBlade.png");
        ImageStorage imageStorage = new ImageStorage(imagesNames);
        assertEquals(3, imageStorage.getStorageSize());
    }

    @Test
    void testImageAdditionByName() {
        ImageStorage imageStorage = new ImageStorage("src/main/resources/img/weapons/airBook.png");
        assertEquals(1, imageStorage.getStorageSize());
        imageStorage.addImageByName("src/main/resources/img/weapons/fireBook.png");
        assertEquals(2, imageStorage.getStorageSize());
    }

    @Test
    void testImageAdditionByObject() throws IOException {
        ImageStorage imageStorage = new ImageStorage();
        String imageName = "src/main/resources/img/heal/herb.png";
        BufferedImage image = ImageIO.read(new File(imageName));
        assertEquals(0, imageStorage.getStorageSize());
        imageStorage.addImageByObject(image, imageName);
        assertEquals(1, imageStorage.getStorageSize());
    }

    @Test
    void testImagesNameIterator() {
        ArrayList<String> imagesNames = new ArrayList<>();
        imagesNames.add("src/main/resources/img/weapons/airBook.png");
        imagesNames.add("src/main/resources/img/weapons/fireBook.png");
        imagesNames.add("src/main/resources/img/weapons/SRankBlade.png");
        ImageStorage imageStorage = new ImageStorage(imagesNames);
        Iterator<String> imageIterator = imageStorage.getImagesNameIterator();

        for (int i = 0; imageIterator.hasNext(); i++) {
            String imageName = imageIterator.next();
            assertEquals(imagesNames.get(i), imageName);
        }
    }
}
