package editor;

import javax.swing.*;

public class IconManager {


    private final ImageIcon openFileImageIcon;
    private final ImageIcon saveFileImageIcon;
    private final ImageIcon searchImageIcon;
    private final ImageIcon previousImageIcon;
    private final ImageIcon nextImageIcon;


    public IconManager() {
        openFileImageIcon = new ImageIcon(
                "C:\\Users\\Pawel\\IdeaProjects\\Text Editor\\Text Editor\\icons\\open-file.jpg",
                "open file image-icon");

        saveFileImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\save.png",
                "save file image-icon");

        searchImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\search.png",
                "search image-icon");

        previousImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\previous.png",
                "previous image-icon");

        nextImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\next.png",
                "nest image-icon");
    }

    public ImageIcon getOpenFileImageIcon() {
        return openFileImageIcon;
    }

    public ImageIcon getSaveFileImageIcon() {
        return saveFileImageIcon;
    }

    public ImageIcon getSearchImageIcon() {
        return searchImageIcon;
    }

    public ImageIcon getPreviousImageIcon() {
        return previousImageIcon;
    }

    public ImageIcon getNextImageIcon() {
        return nextImageIcon;
    }
}
