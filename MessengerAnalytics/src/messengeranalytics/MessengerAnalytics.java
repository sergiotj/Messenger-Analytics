package messengeranalytics;

import java.util.*;
import java.text.SimpleDateFormat;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image ;

public class MessengerAnalytics extends Application {

    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final StackedBarChart<String, Number> sbc = new StackedBarChart<>(xAxis, yAxis);
    final XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    final XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    
    Map<String, Integer> words = new HashMap<String, Integer>();
    
    Map<String, ArrayList<Message>> myMessages = new TreeMap<String, ArrayList<Message>>(new Comparator<String>() {
        
        @Override
        public int compare(String s1, String s2) {
            SimpleDateFormat sd = new SimpleDateFormat("MM/yyyy");

            try {

                Date date1 =  sd.parse(s1);
                Date date2 = sd.parse(s2);

                return date1.compareTo(date2);

            } catch(Exception e){};

            return -1;

        }
    });
    
    Map<String, ArrayList<Message>> otherMessages = new TreeMap<String, ArrayList<Message>>(new Comparator<String>() {
       
        @Override    
        public int compare(String s1, String s2) {
                SimpleDateFormat sd = new SimpleDateFormat("MM/yyyy");

                try {
                    
                    Date date1 = sd.parse(s1);
                    Date date2 = sd.parse(s2);
                    
                    return date1.compareTo(date2);
                    
                } catch(Exception e){};
                
                return -1;
            
            }
    });
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parser parser = new Parser();
        parser.GetMessagesFromFile(myMessages, otherMessages);
        
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
        series2.setName(parser.getPessoa());
        
        for (Map.Entry<String,ArrayList<Message>> myM : myMessages.entrySet()){
            series1.getData().add(new XYChart.Data<>(myM.getKey(), myM.getValue().size()));
        }
        
        for (Map.Entry<String,ArrayList<Message>> otherM : otherMessages.entrySet()){
            
            series2.getData().add(new XYChart.Data<>(otherM.getKey(), otherM.getValue().size()));
        }
                
        sbc.getData().addAll(series1, series2);
        
        SubScene subSceneOne = new SubScene(sbc,1024,500);

        StackPane layoutTwo = new StackPane();
        
        Label label1 = new Label("O mês com mais mensagens foi o " + parser.getDiaMaior() + " e o " + "número de mensagens desse mês: " + parser.getMaior() + System.lineSeparator() + "Número de mensagens total: " + parser.getMessagesNumber() + System.lineSeparator());
        layoutTwo.getChildren().add(label1);
        SubScene subSceneTwo = new SubScene(layoutTwo,1024,100);

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(subSceneOne,subSceneTwo);
        Scene mainScene = new Scene(root,1024,600);
        
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();

    }
    
    public static void main(String[] args) {

        launch(args);
        
    }   
    
    
}