package com.hojung.junchef.util.http;

import com.hojung.junchef.util.http.request.ChatGptRequest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hojung.junchef.util.http.constant.ChatGptRequestConstant.*;

@Service
public class ChatGptHttpService {
    public String search(ChatGptRequest chatGptRequest) throws IOException {
        String response;

        // url 입력
        URL url = new URL(chatGptRequest.getUrl());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(CONNECTION_TIME_OUT_VALUE);
        httpURLConnection.setReadTimeout(READ_TIME_OUT_VALUE);

        // output stream 에 값을 입력 하기 위해 설정 변경
        httpURLConnection.setDoOutput(true);

////             응답 받을 데이터가 있는 경우 true
//            connection.setDoInput(true);
////             요청시 데이터를 보내야 하는 경우 true
//            connection.setDoOutput(true);

        // header 정보 입력
        httpURLConnection.setRequestMethod(POST_METHOD_NAME);
        httpURLConnection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + chatGptRequest.getKey());

        // body 입력
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(getRequestBodyBytes(chatGptRequest.request()));

        // response
        response = getResponseByInputStream(httpURLConnection.getInputStream());

        return response;
    }

    private byte[] getRequestBodyBytes(String string) {
        return string.getBytes();
    }

    private String getResponseByInputStream(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((inputLine = br.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            closeBufferedReader(br, inputStream);
        }

        return stringBuilder.toString();
    }

    private void closeBufferedReader(BufferedReader br, InputStream inputStream) {
        if (br != null) {
            try {
                br.close();
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
