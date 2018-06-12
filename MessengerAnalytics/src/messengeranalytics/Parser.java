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
    
    private String pessoa;
    
    public String getDiaMaior() {
        return diaMaior;
    }
    
    public Integer getMaior() {
        return maior;
    }
    
    public Integer getMessagesNumber() {
        return messagesNumber;
    }
    
    public String getPessoa() {
        return pessoa;
    }
    
    public void GetMessagesFromFile(Map<String, ArrayList<Message>> myMessages, Map<String, ArrayList<Message>> otherMessages) {
        
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
}
