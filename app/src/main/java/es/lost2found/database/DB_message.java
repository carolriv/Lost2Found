package es.lost2found.database;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.InvalidPreferencesFormatException;

import es.lost2found.entities.Message;

public class DB_message {
    private static String SERVER_PATH = "http://jcorreas-hp.fdi.ucm.es/lost2found/database/chat/";

    public static Message createNewMsg(String msgText, String msgHour, Boolean msgRead, Integer idChat, Integer idUser) {
        Message msg = null;
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
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);

                if (response.toString().equals("correct"))
                    msg = new Message(msgText, msgHour, msgRead);
            } finally {
                con.disconnect();
            }
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

    public static Message[] getMsgs(Integer chatId, Integer numberMsgs) {
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

            BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while((inputLine = in.readLine()) != null)
                response.append(inputLine);

            String res = response.toString();
            String[] msgs = res.split("\\},");
            msgs[0] = msgs[0].replace("[", "");
            msgs[msgs.length-1] = msgs[msgs.length-1].replace("]", "");

            for(int i = 0; i < numberMsgs; i++) {
                char lastChar = msgs[i].charAt(msgs[i].length()-1);
                if(lastChar != '}') {
                    msgs[i] += "}";
                }

                JSONObject object = new JSONObject(msgs[i]);
                String msgTxt =  object.getString("texto");
                String msgHour =  object.getString("horaMsg");
                boolean msgRead = false;
                Message msg = new Message(msgTxt, msgHour, msgRead);

                msgsArray[i] = msg;
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgsArray;
    }

    public static Integer getUserIdOwnerOfMsg(Message msg) {
        Integer idUser = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("textMsg", msg.getTextMsg());
            jsonObject.put("hourMsg", msg.getHourMsg());

            List list = new LinkedList();
            list.addAll(Arrays.asList(jsonObject));
            String jsonString = list.toString();

            String urlStr = SERVER_PATH + "getUserIdOwnerOfMsgJSON.php";
            URL url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "your user agent");
            con.setRequestProperty("Accept-Language", "sp,SP;q=0.5");
            //con.setRequestProperty("Content-Type", "application/x-json;charset=UTF-8"); // Si se descomenta no se muestran bien los msgs

            String urlParameters = "json=" + jsonString;

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream (con.getOutputStream());
            //URLEncoder.encode(urlParameters, "ISO-8859-1"); // Si se descomenta no se muestran bien los msgs
            wr.writeBytes (urlParameters);
            wr.flush();
            wr.close();

            InputStream instream;

            int status = con.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                instream = con.getErrorStream();
            else
                instream = con.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while((inputLine = in.readLine()) != null)
                response.append(inputLine);

            JSONObject object = new JSONObject(response.toString());
            Boolean correct = object.getBoolean("correct");
            if (correct) {
                //if(!idUser.toString().equals(""))
                try {
                    idUser = Integer.valueOf(object.getString("id"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  idUser;
    }
}
