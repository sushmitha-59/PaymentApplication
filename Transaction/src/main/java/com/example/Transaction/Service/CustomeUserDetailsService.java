package com.example.Transaction.Service;

import com.example.Transaction.Model.User;
import com.example.Transaction.utils.Constants;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomeUserDetailsService implements UserDetailsService {
/*for /transaction/initiate api , we have implemented to access to users who has authority "user",so we have to check
whether the user is authenticated and authorized ,but we already has userDB and storing the users in the User-service,
so will we also save that info in this service also? well answer is NO, users will be stored in the userDB userService only
   we have to make interCommunication between transaction service and the user service to fetch the user data
   and check whether that user is authenticated and authorized ,we know for authentication spring uses loadByUsername function
   we can implement this function to fetch the data from the userDb which is in another service ,called UserService
 */

    //restTemplate is the object we use for send the request and get the response ,it acts as a client used to call apis
    // it's like postman but its automated and programmed
    private RestTemplate restTemplate=new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //create headers to make API call to user-service
        try{
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.setBasicAuth(Constants.TXN_TO_USER_USERNAME,Constants.TXN_TO_USER_PASSWORD);
            //now create a http request using those headers , httpEntity is used to create the API requests
            HttpEntity<String> httpEntity=new HttpEntity<>(httpHeaders);
            //create the url means user service and api path
            String url="http://localhost:10001/user/username/" + username;
            //now lets call the api and get the response
            ResponseEntity<JSONObject> response=restTemplate.exchange(
                    url,//api path to send the request
                    HttpMethod.GET,//which HTTP method to call by the restTemplate
                    httpEntity,//which contains actual headers for authentication
                    JSONObject.class//which return type we are expecting
            );
            if(response.getStatusCode().is2xxSuccessful()){
                JSONObject ResponsejsonObject =response.getBody();
                assert ResponsejsonObject != null;
                return User.builder()
                        .password((String)ResponsejsonObject.get("password"))
                        .username((String)ResponsejsonObject.get("username"))
                        .email((String)ResponsejsonObject.get("email"))
                        .authorities("user")
                        .build();
            }else{
                throw new RuntimeException("Got NON 200 response from the users API http://localhost:8080/user/username/ in transaction service");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
