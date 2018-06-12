/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengeranalytics;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.text.SimpleDateFormat;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javax.imageio.ImageIO;
import javafx.scene.image.Image ;
import javax.swing.JFrame;


public class MessengerAnalytics extends Application {

    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final StackedBarChart<String, Number> sbc = new StackedBarChart<>(xAxis, yAxis);
    final XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    final XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    
    LinkedHashMap<String, ArrayList<Message>> myMessages = new LinkedHashMap(); 
    
    LinkedHashMap<String, ArrayList<Message>> otherMessages = new LinkedHashMap(); 
    
    String diaMaior;
    
    Integer maior;
    
    Integer messagesNumber;
    
    String pessoa;
    
    
    @Override
    public void start(Stage stage) {
        
        this.getMessages();
        
        sbc.setStyle("CHART_COLOR_1: #fe9b75;CHART_COLOR_2: #865171;");
        stage.setTitle("Mensagens com o utilizador");
        
        sbc.setHorizontalGridLinesVisible(false);
        sbc.setVerticalGridLinesVisible(false);
        
        
        sbc.setTitle("Mensagens");
        xAxis.setLabel("Meses");
        String[] years = myMessages.keySet().toArray(new String[myMessages.size()]);
        
        CategoryAxis xAxis = new CategoryAxis();
        
        xAxis.setCategories(FXCollections.<String>observableArrayList(years));
        
        yAxis.setLabel("Nº de mensagens");
        series1.setName("Eu");
        series2.setName(pessoa);
        
        for (Map.Entry<String,ArrayList<Message>> myM : myMessages.entrySet()){
            series1.getData().add(new XYChart.Data<>(myM.getKey(), myM.getValue().size()));
        }
        
        for (Map.Entry<String,ArrayList<Message>> otherM : otherMessages.entrySet()){
            
            series2.getData().add(new XYChart.Data<>(otherM.getKey(), otherM.getValue().size()));
        }
                
        sbc.getData().addAll(series1, series2);
        
        SubScene subSceneOne = new SubScene(sbc,720,500);

        StackPane layoutTwo = new StackPane();
        
        Label label1 = new Label("O mês com mais mensagens foi o " + diaMaior + " e o " + "número de mensagens desse mês: " + maior + System.lineSeparator() + "Número de mensagens total: " + messagesNumber + System.lineSeparator());
        layoutTwo.getChildren().add(label1);
        SubScene subSceneTwo = new SubScene(layoutTwo,720,100);

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(subSceneOne,subSceneTwo);
        Scene mainScene = new Scene(root,720,600);
        
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();

    }
    
    public void getMessages() {
        
        JSONParser parser = new JSONParser();
  
        SimpleDateFormat sd = new SimpleDateFormat("MM/yyyy");
        
        try {     
            
            Object obj = parser.parse(new FileReader("C:\\Users\\Sergi\\Desktop\\message.json"));
            
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray numbers = (JSONArray) jsonObject.get("messages");

            for (Object number : numbers) {

                String contentISO;
                
                JSONObject jsonNumber = (JSONObject) number;
                
                String sender = (String) jsonNumber.get("sender_name");
                String senderISO = new String(sender.getBytes("ISO-8859-1"));
                
                Long timestamp = (Long) jsonNumber.get("timestamp") * 1000L;
                Date date = new Date(timestamp);
                String month = sd.format(date);
                
                String content = (String) jsonNumber.get("content");
                
                if(content != null) {
                    contentISO = new String(content.getBytes("ISO-8859-1"));
                }
                
                else contentISO = "Sem conteúdo";
                
                Message mensagem = new Message (timestamp, contentISO);
                
                if (senderISO.equals("Sérgio Jorge")) {
                
                    if (myMessages.containsKey(month)) {

                        myMessages.get(month).add(mensagem);
                    }

                    else {
                        
                        ArrayList<Message> mensagens = new ArrayList<>();
                        mensagens.add(mensagem);
                        myMessages.put(month, mensagens);
                    }
                }
                
                else {
                    
                    pessoa = senderISO;
                    
                    if (otherMessages.containsKey(month)) {

                        otherMessages.get(month).add(mensagem);
                    }

                    else {

                        ArrayList<Message> mensagens = new ArrayList<>();
                        mensagens.add(mensagem);
                        otherMessages.put(month, mensagens);
                    }
                    
                }
                
            }

        } catch (FileNotFoundException e) {
        } catch (IOException | ParseException e) {
        }
        
        Integer myMaior = 0;   
        Integer otherMaior = 0; 
        diaMaior = null;
        messagesNumber = 0;
        
        
        for (Map.Entry<String,ArrayList<Message>> par : otherMessages.entrySet()){
            
            if (par.getValue().size() > otherMaior) {
                otherMaior = par.getValue().size();
                diaMaior = par.getKey();
            }
            
            messagesNumber += par.getValue().size();
        }
        
        for (Map.Entry<String,ArrayList<Message>> par : myMessages.entrySet()){
            
            if (par.getValue().size() > myMaior) {
                myMaior = par.getValue().size();
                diaMaior = par.getKey();
            }
            
            messagesNumber += par.getValue().size();
        }
        
        maior = myMaior + otherMaior;
    
    }
    
    
    public static void main(String[] args) {

        launch(args);
        
    }   
    
    
}