package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller {
    String path ;
    @FXML
    private ImageView imgBefore;
    
    @FXML
    private Slider sampleSlide;
    
    @FXML
    private Slider bookSlider;
    
    @FXML
    private ImageView imgAfter;
    
    @FXML
    private Button btn;
    @FXML
    void BroWSEIMAGE(ActionEvent event) {
    
       
        
    
        final FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file!=null)
        {
            System.out.println("name is"+file.toString());
            path=file.toString();
            setBeforImag(file.toString());
    
        }
       
    }
    private void setBeforImag(String Path){
    
        try {
            Image image = new Image(new FileInputStream(Path));
            imgBefore.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    public void setCompressedImage (String Path)
    {
    
        try {
            Image image = new Image(new FileInputStream(Path));
            imgAfter.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    @FXML
    void bookR(MouseEvent event) {
        
        if (path==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have to specify a certain image path to able to run the quantizer", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            sampleSlide.setValue(1.0);
            bookSlider.setValue(1.0);
        }
        else
        {
    
        System.out.println("sample slider is"+(int)Math.pow(2,sampleSlide.getValue())+"codebook slider"+(int)Math.pow(2,bookSlider.getValue()));
    
        try {
            Qunatizer qunatizer = new Qunatizer((int)(Math.pow(2,sampleSlide.getValue())),(int)(Math.pow(2,bookSlider.getValue())),path);
            setCompressedImage(qunatizer.CompressedImage());
        
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
    
        }
    }
    
    @FXML
    public void sampleSizeSlider(MouseEvent mouseEvent) {
    
        if (path == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have to specify a certain image path to able to run the quantizer", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            sampleSlide.setValue(1.0);
            bookSlider.setValue(1.0);
        
        } else {
    
    
            System.out.println("sample slider is" + (int) Math.pow(2, sampleSlide.getValue()) + "codebook slider" + (int) Math.pow(2, bookSlider.getValue()));
            
            try {
                Qunatizer qunatizer = new Qunatizer((int) (Math.pow(2, sampleSlide.getValue())), (int) (Math.pow(2, bookSlider.getValue())), path);
                setCompressedImage(qunatizer.CompressedImage());
        
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
        }
    }
    
    
    
}
