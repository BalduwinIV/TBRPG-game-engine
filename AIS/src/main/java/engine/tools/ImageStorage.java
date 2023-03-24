package engine.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ImageStorage {
    Logger logger;
    private final List<String> imagesNameList;
    private final List<BufferedImage> imagesList;

    public ImageStorage() {
        imagesNameList = new ArrayList<>(1);
        imagesList = new ArrayList<>(1);
    }

    public ImageStorage(String imageName) {
        imagesNameList = new ArrayList<>(1);
        imagesList = new ArrayList<>(1);
        imagesNameList.add(imageName);
        try {
            imagesList.add(ImageIO.read(new File(imageName)));
        } catch (IOException e) {
            printError("An error occurred while reading image \"" + imageName + "\".");
            e.printStackTrace();
        }
    }

    public ImageStorage(List<String> imagesNameList) {
        this.imagesNameList = imagesNameList;
        imagesList = new ArrayList<>(this.imagesNameList.size());
        for (String imageName : imagesNameList) {
            try {
                imagesList.add(ImageIO.read(new File(imageName)));
            } catch (IOException e) {
                printError("An error occurred while reading image \"" + imageName + "\".");
                e.printStackTrace();
            }
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private void printError(String errorMessage) {
        if (Objects.nonNull(logger)) {
            logger.error(this, errorMessage);
        } else {
            System.err.println(errorMessage);
        }
    }

    public void addImageByName(String imageName) {
        try {
            imagesList.add(ImageIO.read(new File(imageName)));
            imagesNameList.add(imageName);
        } catch (IOException e) {
            printError("An error occurred while reading image \"" + imageName + "\".");
            e.printStackTrace();
        }
    }

    public void addImagesByName(List<String> imagesNameList) {
        for (String imageName : imagesNameList) {
            try {
                imagesList.add(ImageIO.read(new File(imageName)));
                this.imagesNameList.add(imageName);
            } catch (IOException e) {
                printError("An error occurred while reading image \"" + imageName + "\".");
                e.printStackTrace();
            }
        }
    }

    public void addImageByObject(BufferedImage image, String imageName) {
        imagesList.add(image);
        imagesNameList.add(imageName);
    }

    public void addImagesByObject(List<BufferedImage> imagesList, List<String> imagesNameList) {
        this.imagesList.addAll(imagesList);
        this.imagesNameList.addAll(imagesNameList);
    }

    public Iterator<BufferedImage> getImagesIterator() {
        return this.imagesList.iterator();
    }

    public Iterator<String> getImagesNameIterator() {
        return this.imagesNameList.iterator();
    }

    public BufferedImage getImage(int index) {
        if (index >= this.imagesList.size()) {
            printError("ImageList index out of range.");
            return null;
        }
        return this.imagesList.get(index);
    }

    public BufferedImage getImage(String imageName) {
        int imageIndex = imagesNameList.indexOf(imageName);
        if (imageIndex == -1) {
            printError("ImageList does not contain image \"" + imageName + "\".");
            return null;
        }
        return imagesList.get(imageIndex);
    }
}
