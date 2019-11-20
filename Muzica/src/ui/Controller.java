package ui;

import domain.Muzica;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import service.Service;

//import javax.swing.text.TableView;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller implements Observer<MuzicaEvent>{
    private Service service;
    private ObservableList<Muzica> modelMuz;

    @FXML
    private BorderPane bp;
    @FXML private TableView<Muzica> tableV;

    @FXML private TableColumn<Muzica,String> artCol;
    @FXML private TableColumn<Muzica,String> albCol;
    @FXML private TableColumn<Muzica,String> verCol;
    @FXML private TableColumn<Muzica,String> melCol;
    @FXML private TableColumn<Muzica,String> recCol;

    @FXML private Button btn_add, btn_rem, btn_upd;

    @FXML private ComboBox<String> comboBoxVerdict;
    @FXML private ComboBox<String> comboBoxRecom;

    @FXML private TextField muzica_artistTxt, muzica_albumTxt, muzica_melodiiTxt,cautareArtist;

    private void loadData() throws Exception {
        modelMuz = FXCollections.observableList(StreamSupport.stream(service.allMusic().spliterator(), false).collect(Collectors.toList()));
        modelMuz.sort(Comparator.comparing(Muzica::getArtist).thenComparing(Muzica::getAlbum));
        tableV.setItems(modelMuz);

        comboBoxVerdict.getItems().addAll("-","Deplorabil" , "Se putea si mai bine", "Meh", "Okay", "Bun", "Foarte Bun", "Dandy");
        comboBoxVerdict.getSelectionModel().selectFirst();

        comboBoxRecom.getItems().addAll("-","Da","Da, dar pentru un anumit public", "Nu");
        comboBoxRecom.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void initialize(){
        artCol.setCellValueFactory(new PropertyValueFactory<Muzica,String>("artist"));
        albCol.setCellValueFactory(new PropertyValueFactory<Muzica,String>("album"));
        verCol.setCellValueFactory(new PropertyValueFactory<Muzica,String>("verdict"));
        melCol.setCellValueFactory(new PropertyValueFactory<Muzica,String>("melodii"));
        recCol.setCellValueFactory(new PropertyValueFactory<Muzica,String>("recomandare"));
        tableV.getSelectionModel().selectedItemProperty().addListener((observer,oldData,newData)->showDetailsMuzica(newData));
    }

    public void setFXMLController(Service serv) throws Exception {
        this.service = serv;
        this.service.addObserver(this);
        loadData();
    }

    private void showDetailsMuzica(Muzica muzica){
        if(muzica!=null){
            muzica_artistTxt.setText(muzica.getArtist());
            muzica_albumTxt.setText(muzica.getAlbum());
            comboBoxVerdict.setValue(muzica.getVerdict());
            muzica_melodiiTxt.setText(muzica.getMelodii());
            comboBoxRecom.setValue(muzica.getRecomandare());
        }
    }

    @FXML private void addUI(ActionEvent event){
        if(event.getSource()==btn_add){
            try{
                service.addMuzica(muzica_artistTxt.getText(),muzica_albumTxt.getText(),comboBoxVerdict.getValue().toString(),muzica_melodiiTxt.getText(),comboBoxRecom.getValue().toString());
            }catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Eroare");
                alert.setContentText(e.getMessage());
                alert.show();
            }
            muzica_artistTxt.clear();
            muzica_albumTxt.clear();
            comboBoxVerdict.getSelectionModel().selectFirst();
            muzica_melodiiTxt.setText("-");
            comboBoxRecom.getSelectionModel().selectFirst();
        }
    }

    @FXML private void removeUI(ActionEvent event){
        if(event.getSource()==btn_rem){
            try{
                service.removeMuzica(muzica_artistTxt.getText(),muzica_albumTxt.getText());
            }catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Eroare");
                alert.setContentText(e.getMessage());
                alert.show();
            }
            muzica_artistTxt.clear();
            muzica_albumTxt.clear();
            comboBoxVerdict.getSelectionModel().selectFirst();
            muzica_melodiiTxt.setText("-");
            comboBoxRecom.getSelectionModel().selectFirst();
        }
    }

    @FXML private void updateUI(ActionEvent event){
        if(event.getSource()==btn_upd){
            try{
                service.updateMuzica(muzica_artistTxt.getText(),muzica_albumTxt.getText(),comboBoxVerdict.getValue(),muzica_melodiiTxt.getText(),comboBoxRecom.getValue());
                tableV.getSelectionModel().clearSelection();
            }catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Eroare");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }

    public void filtrareArtist() throws Exception {
        Predicate<Muzica> m = n ->{
            try {
                return service.findMuzica(n.getId()).getArtist().toLowerCase().contains(cautareArtist.getText().toLowerCase());
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        };
        modelMuz.setAll(service.allMusic().stream().filter(m).collect(Collectors.toList()));
    }
    
    @Override
    public void update(MuzicaEvent e){
        if(e.getType() == ChangeEventType.ADD){
            modelMuz.add(e.getData());
            modelMuz.sort(Comparator.comparing(Muzica::getArtist).thenComparing(Muzica::getAlbum));
        }
        else if (e.getType() == ChangeEventType.UPDATE){
            modelMuz.set(modelMuz.indexOf(e.getOldData()),e.getData());
            modelMuz.sort(Comparator.comparing(Muzica::getArtist).thenComparing(Muzica::getAlbum));
        }
        else if (e.getType() == ChangeEventType.DELETE){
            modelMuz.remove(e.getData());
        }
    }
}
