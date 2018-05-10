package es.lost2found.database;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import es.lost2found.entities.Message;

public class DB_message {

    private static String SERVER_PATH = "http://jcorreas-hp.fdi.ucm.es/lost2found/database/chat/";

    public static Message createNewMsg(String msgText, String msgHour, Boolean msgRead, Integer idChat, Integer idUser) {
        Message msg = null;
        String userName = DB_user.getNameById(idUser);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgText", msgText);
            jsonObject.put("msgHour", msgHour);
            jsonObject.put("msgRead", msgRead);
            jsonObject.put("idChat", idChat);
            jsonObject.put("idUser", idUser);

            List list = new LinkedList();
            list.addAll(Arrays.asList(jsonObject));
            String jsonString = list.toString();

            jsonString = URLEncoder.encode(jsonString, "UTF-8");

            String urlStr = SERVER_PATH + "insertNewChatMsgJSON.php";
            URL url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            try {
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "your user agent");
                con.setRequestProperty("Accept-Language", "sp-SP,sp;q=0.5");

                String urlParameters = "json=" + jsonString;

                con.setDoOutput(true);
                OutputStream outstream = con.getOutputStream();
                DataOutputStream wr = new DataOutputStream(outstream);
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                InputStream instream;

                int status = con.getResponseCode();

                if (status != HttpURLConnection.HTTP_OK)
                    instream = con.getErrorStream();
                else
                    instream = con.getInputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(instream));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);

                if (response.toString().equals("correct"))
                    msg = new Message(msgText, msgHour, msgRead, userName);
            } finally {
                con.disconnect();
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static Integer getNumberChatMsgs(Integer chatId) {
        Integer ret = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", chatId);

            List list = new LinkedList();
            list.addAll(Arrays.asList(jsonObject));
            String jsonString = list.toString();

            String urlStr = SERVER_PATH + "getNumberChatMsgsJSON.php";
            URL url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "your user agent");
            con.setRequestProperty("Accept-Language", "sp,SP;q=0.5");

            String urlParameters = "json=" + jsonString;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            InputStream instream;

            int status = con.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                instream = con.getErrorStream();
            else
                instream = con.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(instream));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            if(Integer.valueOf(response.toString()) >= 0) {
                ret = Integer.valueOf(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ret;
    }

    public static Message[] getMsgs(Integer chatId, Integer numberMsgs) { // Falta crear en el servidor y comprobar que funciona
        Message[] msgsArray = new Message[numberMsgs];
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", chatId);

            List list = new LinkedList();
            list.addAll(Arrays.asList(jsonObject));
            String jsonString = list.toString();

            String urlStr = SERVER_PATH + "getChatMsgsJSON.php";
            URL url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "your user agent");
            con.setRequestProperty("Accept-Language", "sp,SP;q=0.5");

            String urlParameters = "json=" + jsonString;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            InputStream instream;

            int status = con.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                instream = con.getErrorStream();
            else
                instream = con.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(instream));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null)
                response.append(inputLine);

            String res = response.toString();
            String[] msgs = res.split("\\.");
            msgs[0] = msgs[0].replace("[", "");
            msgs[msgs.length-1] = msgs[msgs.length-1].replace("]", "");

            for(int i = 0; i < numberMsgs; i++) {
                char firstChar = msgs[i].charAt(1);
                if(firstChar == ',') {
                    msgs[i] = msgs[i].substring(2, msgs[i].length());
                }
                JSONObject object = new JSONObject(msgs[i]);
                String msgTxt =  object.getString("texto");
                String msgHour =  object.getString("horaMsg");
                Integer leido = object.getInt("leido");
                Integer idUser = object.getInt("idUsuario");
                boolean msgRead = false;
                if(leido.equals(1)) {
                    msgRead = true;
                }
                String userName = DB_user.getNameById(idUser);
                Message msg = new Message(msgTxt, msgHour, msgRead, userName);

                msgsArray[i] = msg;
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgsArray;
    }

}
