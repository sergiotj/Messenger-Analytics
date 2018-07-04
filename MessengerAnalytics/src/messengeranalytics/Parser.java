package messengeranalytics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Parser {
    
    private String diaMaior;
    
    private Integer maior;
    
    private Integer messagesNumber;
    
    private String person1;
    
    private String person2;
    
    public String getDiaMaior() {
        return diaMaior;
    }
    
    public Integer getMaior() {
        return maior;
    }
    
    public Integer getMessagesNumber() {
        return messagesNumber;
    }
    
    public String getPerson1() {
        return person1;
    }
    
    public String getPerson2() {
        return person2;
    }
    
    public void GetMessagesFromFile(Map<String, ArrayList<Message>> person1Messages, Map<String, ArrayList<Message>> person2Messages, Map<String, Integer> words) {
        
        JSONParser parser = new JSONParser();
  
        SimpleDateFormat sd = new SimpleDateFormat("MM/yyyy");
        
        try {     
            
            Object obj = parser.parse(new FileReader("message.json"));
            
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray numbers = (JSONArray) jsonObject.get("messages");
            
            int counter = 0;
            
            for (Object number : numbers) {

                String contentISO;
                
                JSONObject jsonNumber = (JSONObject) number;
                
                String sender = (String) jsonNumber.get("sender_name");
                String senderISO = new String(sender.getBytes("ISO-8859-1"));
                
                if (counter == 0) {
                    person1 = senderISO;
                }
                
                counter++;
                
                Long timestamp = (Long) jsonNumber.get("timestamp") * 1000L;
                Date date = new Date(timestamp);
                String month = sd.format(date);
                
                String content = (String) jsonNumber.get("content");
                
                if(content != null) {
                    contentISO = new String(content.getBytes("ISO-8859-1"));
                }
                
                else contentISO = "Sem conteúdo";
                
                Message mensagem = new Message (timestamp, contentISO);
                
                String[] wordsArray = contentISO.split("\\W+");
                
                for (int i = 0; i < wordsArray.length; i++) {
                
                    if (words.containsKey(wordsArray[i])) {
                        
                        if (wordsArray[i].length() > 3)
                        words.put(wordsArray[i], words.get(wordsArray[i]) + 1);
                    }

                    else {
                        if (wordsArray[i].length() > 4)
                        words.put(wordsArray[i], 1);
                    }
                }
                
                if (senderISO.equals(person1)) {
                
                    if (person1Messages.containsKey(month)) {

                        person1Messages.get(month).add(mensagem);
                    }

                    else {
                        
                        ArrayList<Message> mensagens = new ArrayList<>();
                        mensagens.add(mensagem);
                        person1Messages.put(month, mensagens);
                    }
                }
                
                else {
                    
                    person2 = senderISO;
                    
                    if (person2Messages.containsKey(month)) {

                        person2Messages.get(month).add(mensagem);
                    }

                    else {

                        ArrayList<Message> mensagens = new ArrayList<>();
                        mensagens.add(mensagem);
                        person2Messages.put(month, mensagens);
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
        
        
        for (Map.Entry<String,ArrayList<Message>> par : person2Messages.entrySet()){
            
            if (par.getValue().size() > otherMaior) {
                otherMaior = par.getValue().size();
                diaMaior = par.getKey();
            }
            
            messagesNumber += par.getValue().size();
        }
        
        for (Map.Entry<String,ArrayList<Message>> par : person1Messages.entrySet()){
            
            if (par.getValue().size() > myMaior) {
                myMaior = par.getValue().size();
                diaMaior = par.getKey();
            }
            
            messagesNumber += par.getValue().size();
        }
        
        maior = myMaior + otherMaior;
    
    }
}
