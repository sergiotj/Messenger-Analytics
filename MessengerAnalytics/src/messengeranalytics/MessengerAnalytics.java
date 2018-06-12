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
import javafx.scene.image.Image;
import javafx.scene.control.ListView;
import java.util.stream.Collectors;
import static java.util.Collections.reverseOrder;

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

                Date date1 = sd.parse(s1);
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
        parser.GetMessagesFromFile(myMessages, otherMessages, words);
        
        Map<String, Integer> sortedMap = 
            words.entrySet().stream()
           .sorted(reverseOrder(Map.Entry.comparingByValue()))
           .limit(25)
           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                     (e1, e2) -> e1, LinkedHashMap::new));
        
        String total = null;
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()){
            
            String palavras = entry.getKey() + ":" + entry.getValue() + " ";
            
            total = total + palavras;
        }
        
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

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(subSceneOne);
        
        ArrayList<String> palPlusTimes = new ArrayList<String>();
        
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            
            String word = entry.getKey();
            String times = entry.getValue().toString();
            
            String finalWord = word + " " + times + "x";
            
            palPlusTimes.add(finalWord);
            
        }
        
        
        ListView<String> list = new ListView<>();
        list.getItems().addAll(palPlusTimes);
        
        
        root.getChildren().add(list);
        Scene mainScene = new Scene(root,1024,800);
        
        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();

    }
    
    public static void main(String[] args) {

        launch(args);
        
    }   
    
    
}