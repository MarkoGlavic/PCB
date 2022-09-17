package com.example.images;


import javafx.fxml.FXML;


import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.*;


public class ImagesController {


    @FXML
    MenuBar menu;
    @FXML
    ImageView imageView, imageView1, imageView3;
    @FXML
    AnchorPane pane, pane1, pane3;
    @FXML
    Text text1, text2;
    @FXML
    ChoiceBox<String> choiceBox;
    @FXML
    TextField size5, R, G, B, HUE, BRI, SAT, name;
    @FXML
    CheckBox comp, colourCheck;
    double bri = 0.2;
    double r = 0.2;
    double g = 0.2;
    double b = 0.2;
    int hue = 20;
    double sat = 0.4;
    int size = 100;
    int[][] pixels;
    int[] pixel;
    HashMap<Rectangle, String> names = new HashMap<>();
    HashMap<Rectangle, Integer> rectangles = new HashMap<>();
    HashSet<Integer> allRoots = new HashSet<>();

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        File selectedFile = fileChooser.showOpenDialog(menu.getScene().getWindow());
        Image image = new Image(selectedFile.toURI().toString(), 512, 512, false, false);
        imageView.setImage(image);


    }


    @FXML
    public void initialize() {
//Make black and white image on left click
        imageView.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                Image image = imageView.getImage();
                PixelReader pr = imageView.getImage().getPixelReader();
                WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
                PixelWriter writer = newImage.getPixelWriter();
                Color color = pr.getColor((int) mouseEvent.getX(), (int) mouseEvent.getY());
                System.out.println(color.getBlue() + " " + color.getRed() + " " + color.getGreen() + " " + color.getBrightness() + " " + color.getHue() + " " + color.getSaturation());
                for (int y = 0; y < image.getHeight(); ++y) {
                    for (int x = 0; x < image.getWidth(); ++x) {
                        Color color1 = pr.getColor(x, y);
                        if ((color1.getGreen() >= color.getGreen() - g && color1.getGreen() <= color.getGreen() + g) && (color1.getBlue() >= color.getBlue() - b && color1.getBlue() <= color.getBlue() + b) && (color1.getRed() >= color.getRed() - r && color1.getRed() <= color.getRed() + r) && (color1.getBrightness() >= color.getBrightness() - bri && color1.getBrightness() <= color.getBrightness() + bri) && (color1.getHue() >= color.getHue() - hue && color1.getHue() <= color.getHue() + hue) && (color1.getSaturation() >= color.getSaturation() - sat && color1.getSaturation() <= color.getSaturation() + sat)) {
                            writer.setColor(x, y, color1.BLACK);

                        } else {
                            writer.setColor(x, y, color1.WHITE);

                        }

                    }
                }

                imageView1.setImage(newImage);
            }
            //Make two black and white images combine
            else {
                PixelReader pixelReader = imageView1.getImage().getPixelReader();
                Image image = imageView.getImage();
                PixelReader pr = imageView.getImage().getPixelReader();

                WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
                PixelWriter writer = newImage.getPixelWriter();
                Color color = pr.getColor((int) mouseEvent.getX(), (int) mouseEvent.getY());
                System.out.println(color.getBlue() + " " + color.getRed() + " " + color.getGreen() + " " + color.getBrightness() + " " + color.getHue() + " " + color.getSaturation());
                for (int y = 0; y < image.getHeight(); ++y) {
                    for (int x = 0; x < image.getWidth(); ++x) {
                        Color color1 = pr.getColor(x, y);
                        if ((color1.getGreen() >= color.getGreen() - g && color1.getGreen() <= color.getGreen() + g) && (color1.getBlue() >= color.getBlue() - b && color1.getBlue() <= color.getBlue() + b) && (color1.getRed() >= color.getRed() - r && color1.getRed() <= color.getRed() + r) && (color1.getBrightness() >= color.getBrightness() - bri && color1.getBrightness() <= color.getBrightness() + bri) && (color1.getHue() >= color.getHue() - hue && color1.getHue() <= color.getHue() + hue) && (color1.getSaturation() >= color.getSaturation() - sat && color1.getSaturation() <= color.getSaturation() + sat)) {
                            writer.setColor(x, y, color1.BLACK);

                        } else {
                            writer.setColor(x, y, color1.WHITE);

                        }

                    }
                }

                imageView3.setImage(newImage);
                imageView3.setVisible(false);
                PixelReader pixelReaderr = imageView3.getImage().getPixelReader();
                WritableImage newImagee = new WritableImage((int) image.getWidth(), (int) image.getHeight());
                PixelWriter writerr = newImagee.getPixelWriter();
                for (int y = 0; y < image.getHeight(); ++y) {
                    for (int x = 0; x < image.getWidth(); ++x) {
                        Color color1 = pr.getColor(x, y);
                        if (pixelReader.getArgb(x, y) != pixelReaderr.getArgb(x, y)) {
                            writerr.setColor(x, y, color1.BLACK);
                        } else {
                            writerr.setColor(x, y, color1.WHITE);
                        }
                    }
                }
                imageView1.setImage(null);
                imageView1.setImage(newImagee);

            }

        });
        choiceBox.setOnAction(actionEvent -> choice());

    }

    //Setting the white pixels to -1 and everything else to their own ID and then transfering it into a 1d array
    public void unionFind() {

        Image image = imageView1.getImage();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        pixels = new int[height][width];
        PixelReader pr = imageView1.getImage().getPixelReader();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (pr.getArgb(x, y) == 0xffffffff) {
                    pixels[y][x] = -1;
                } else {
                    pixels[y][x] = getId(y, x, pixels[0].length);
                }
            }
        }
        pixel = new int[width * height];
        for (int i = 0; i < pixels.length; ++i) {
            System.arraycopy(pixels[i], 0, pixel, i * pixels[0].length, pixels[i].length);
        }

        union();

    }

    //Option to let user deal with noise
    public void update() {
        if (!size5.getText().isEmpty()) {
            size = Integer.parseInt(size5.getText());
        }
        if (!R.getText().isEmpty()) {
            r = Double.parseDouble(R.getText());
        }
        if (!G.getText().isEmpty()) {
            g = Double.parseDouble(G.getText());
        }
        if (!B.getText().isEmpty()) {
            b = Double.parseDouble(B.getText());
        }
        if (!HUE.getText().isEmpty()) {
            hue = Integer.parseInt(HUE.getText());
        }
        if (!SAT.getText().isEmpty()) {
            sat = Double.parseDouble(SAT.getText());
        }
        if (!BRI.getText().isEmpty()) {
            bri = Double.parseDouble(BRI.getText());
        }

    }

    //Actually unionize all pixels that are not white or -1
    public void union() {

        if (name.getText().isEmpty()) {

            Alert err = new Alert(Alert.AlertType.ERROR, "ENTER A NAME BEFORE UNION");
            err.showAndWait();
        } else {
            choiceBox.getItems().add(name.getText());
            int width = (int) imageView1.getImage().getWidth();
            for (int i = 0; i < pixel.length; i++) {
                if (pixel[i] >= 0) {
                    if ((i + 1) % width != 0 && pixel[i + 1] >= 0) {
                        UnionFind.union(pixel, i, i + 1);

                    }
                    if ((i) + width < pixel.length && pixel[i + width] >= 0) {
                        UnionFind.union(pixel, i, i + width);

                    }


                }


            }

            drawRectangle();


        }
    }

    //Draw and display rectangles
    public void drawRectangle() {
        int width = (int) imageView1.getImage().getWidth();
        for (int i = 0; i < pixel.length; i++) {
            if (pixel[i] != -1 && !allRoots.contains(UnionFind.find(pixel, 1))) {
                allRoots.add(UnionFind.find(pixel, i));

            }
        }


        for (int id : allRoots) {
            int left = width + 1, right = -2, top = width + 1, bot = -2, compSize = 0;
            for (int i = 0; i < pixel.length; i++) {
                if (pixel[i] != -1 && UnionFind.find(pixel, i) == id) {
                    if (i % width < left) left = i % width;
                    if (i % width > right) right = i % width;
                    if (i / width < top) top = i / width;
                    if (i / width > bot) bot = i / width;
                    compSize = compSize + 1;
                }
            }

            Rectangle rectangle = new Rectangle(left, top, right - left, bot - top);
            if (compSize > size) {
                rectangle.setStroke(Color.RED);
                rectangle.setStrokeWidth(3);
                rectangle.setFill(Color.TRANSPARENT);
                pane.getChildren().add(rectangle);
                rectangles.put(rectangle, compSize);
                names.put(rectangle, name.getText());
            }
        }

        System.out.println(allRoots.size());


    }
    //Clear everything including rectangles

    public void clear() {
        Map<Rectangle, Integer> sortedMap = sortByComparator(rectangles, false);
        for (Map.Entry<Rectangle, Integer> rectangle : sortedMap.entrySet()) {
            pane.getChildren().remove(rectangle.getKey());
        }
        pane3.getChildren().clear();
        choiceBox.getItems().clear();
        text1.setText("");
        text2.setText("");
        rectangles.clear();
        names.clear();
    }

    //Make the tooltips and numbers on rectangles appear
    public void labeling() {
        pane3.getChildren().clear();
        Map<Rectangle, Integer> sortedMap = sortByComparator(rectangles, false);
        int rectangleCount = 1;

        for (Map.Entry<Rectangle, Integer> rectangle : sortedMap.entrySet()) {
            Text text = new Text(Integer.toString(rectangleCount));
            TextFlow label = new TextFlow();
            text.setFont(new Font("Arial", 14));
            text.setFill(Color.BLUE);
            label.getChildren().add(text);
            label.setStyle("-fx-font-weight: 900;");
            label.setLayoutX(rectangle.getKey().getX());
            label.setLayoutY(rectangle.getKey().getY());
            for (Map.Entry<Rectangle, String> rectangleStringEntry : names.entrySet()) {
                if (rectangleStringEntry.getKey() == rectangle.getKey()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(rectangleStringEntry.getValue() + "\r\n"
                            + "COMPONENT NUMBER: " + rectangleCount + "\r\n" +
                            "COMPONENT SIZE: " + rectangle.getValue());
                    Tooltip.install(rectangle.getKey(), tooltip);

                }

            }
            if (comp.isSelected()) {
                pane3.getChildren().add(label);
                pane3.setMouseTransparent(false);
                pane3.setVisible(true);
            } else {
                pane3.setMouseTransparent(true);
                pane3.setVisible(false);
            }
            rectangleCount = rectangleCount + 1;
        }


    }

    //Sort the hashmap with rectangles and sizes
    private static Map<Rectangle, Integer> sortByComparator(HashMap<Rectangle, Integer> unsortMap, final boolean order) {
        List<Map.Entry<Rectangle, Integer>> list = new LinkedList<>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Rectangle, Integer>>() {
            public int compare(Map.Entry<Rectangle, Integer> o1,
                               Map.Entry<Rectangle, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });
        Map<Rectangle, Integer> sortedMap = new LinkedHashMap<Rectangle, Integer>();
        for (Map.Entry<Rectangle, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    //Change the color of components in the black and white picture

    public void colouring() {
        Image originalImage = imageView.getImage();
        PixelReader pixelReader = originalImage.getPixelReader();
        Image image = imageView1.getImage();
        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = newImage.getPixelWriter();

        for (int id : allRoots) {

            Color color = Color.color(Math.random(), Math.random(), Math.random());
            for (int i = 0; i < pixel.length; i++) {
                if (pixel[i] != -1 && UnionFind.find(pixel, i) == id) {
                    if (colourCheck.isSelected()) {
                        Color colord = pixelReader.getColor(i % (int) originalImage.getWidth(), i / (int) image.getWidth());
                        writer.setColor(i % (int) image.getWidth(), i / (int) image.getWidth(), colord);
                    } else {


                        writer.setColor(i % (int) image.getWidth(), i / (int) image.getWidth(), color);
                    }
                }
            }
        }

        imageView1.setImage(newImage);
    }

    //Calculate total number and number of specific component, based on name
    public void choice() {
        text1.setVisible(true);
        text2.setVisible(true);
        text1.setText("");
        text2.setText("");
        int specificComp = 0;
        for (Map.Entry<Rectangle, String> rectangleStringEntry : names.entrySet()) {
            if (choiceBox.getValue().equals(rectangleStringEntry.getValue())) {
                specificComp = specificComp + 1;
                text1.setText(choiceBox.getValue() + ": " + specificComp);
            }
        }

        text2.setText("TOTAL: " + names.size());
    }
//get ID in a 2d array

    private int getId(int row, int col, int nCols) {
        return row * nCols + col;
    }
}

