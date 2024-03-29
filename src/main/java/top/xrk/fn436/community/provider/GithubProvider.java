package top.xrk.fn436.community.provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import top.xrk.fn436.community.dto.AccesstokenDTO;
import top.xrk.fn436.community.dto.GithubUser;

import java.io.IOException;

@Component
public class GithubProvider {

    public String getAccesstoken(AccesstokenDTO accesstokenDTO) {

        MediaType mediatype = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(mediatype, JSON.toJSONString(accesstokenDTO));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();

        try (Response response = client.newCall(request).execute()) {
            try {
                String string = response.body().string();
                String[] split = string.split("&");
                String token = split[0].split("=")[1];
                return token;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //System.out.print(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return  githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
