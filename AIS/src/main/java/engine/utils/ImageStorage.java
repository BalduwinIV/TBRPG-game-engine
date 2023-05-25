package engine.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Class, that helps while working with object textures and animations.
 */
public class ImageStorage {
    private Logger logger;
    private final List<String> imagesNameList;
    private final List<BufferedImage> imagesList;

    /**
     * Initialize empty storage for images.
     */
    public ImageStorage() {
        imagesNameList = new ArrayList<>(1);
        imagesList = new ArrayList<>(1);
    }

    /**
     * Saves one image to the storage.
     * @param   imageName   Name of the image to store.
     */
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

    /**
     * Saves a series of images to storage.
     * @param   imagesNameList  Names of the images to store.
     */
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

    public ImageStorage(BufferedImage image) {
        this.imagesNameList = new ArrayList<>(1);
        this.imagesList = new ArrayList<>(1);
        this.imagesNameList.add("bufferedImage");
        this.imagesList.add(image);
    }

    /**
     * Connects logger to current object.
     * @param   logger  Logger, that will be used for logging current class actions.
     */
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

    /**
     * Reads an image and adds to the storage.
     * @param   imageName   Image filename
     */
    public void addImageByName(String imageName) {
        try {
            imagesList.add(ImageIO.read(new File(imageName)));
            imagesNameList.add(imageName);
        } catch (IOException e) {
            printError("An error occurred while reading image \"" + imageName + "\".");
            e.printStackTrace();
        }
    }

    /**
     * Reads series of images and adds them to the storage.
     * @param   imagesNameList  List of images filenames
     */
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

    /**
     * Adds an image and its name directly to storage.
     * @param   image       BufferedImage object to add to storage.
     * @param   imageName   Image filename.
     */
    public void addImageByObject(BufferedImage image, String imageName) {
        imagesList.add(image);
        imagesNameList.add(imageName);
    }

    /**
     * Adds series of images to storage.
     * @param   imagesList      List of BufferedImage objects to add to storage.
     * @param   imagesNameList  List of its names to add to storage.
     */
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

    /**
     * If storage is not empty returns first image from it.
     * Returns null otherwise.
     * @return  First image from storage or null, if storage is empty.
     */
    public BufferedImage getImage() {
        if (this.imagesList.size() == 0) {
            printError("ImageList is empty.");
            return null;
        }
        return this.imagesList.get(0);
    }

    /**
     * If index is in range <0, storageSize), then returns image of given index.
     * Returns null otherwise.
     * @param   index   Index of image in storage to return.
     * @return  Image of given index or null, if index is out of storage size range.
     */
    public BufferedImage getImage(int index) {
        if (index >= this.imagesList.size()) {
            printError("ImageList index out of range.");
            return null;
        }
        return this.imagesList.get(index);
    }

    /**
     * If image of given name does exist in storage, returns it.
     * Returns null otherwise.
     * @param   imageName   Filename of image in storage to return.
     * @return  Image with given filename or null, if there is no image with that name in storage.
     */
    public BufferedImage getImage(String imageName) {
        int imageIndex = imagesNameList.indexOf(imageName);
        if (imageIndex == -1) {
            printError("ImageList does not contain image \"" + imageName + "\".");
            return null;
        }
        return imagesList.get(imageIndex);
    }

    /**
     *  Returns name of image of given index.
     * @param index Image index.
     * @return Image name.
     */
    public String getImageName(int index) {
        if (index < 0 || index >= imagesNameList.size()) {
            printError("Image index is out of range.");
            return null;
        }
        return imagesNameList.get(index);
    }

    public int getStorageSize() {
        return imagesList.size();
    }
}
