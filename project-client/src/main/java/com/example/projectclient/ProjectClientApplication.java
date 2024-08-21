package com.example.projectclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ProjectClientApplication extends Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectClientApplication.class, args);
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectclient/MyUI.fxml"));
        Parent root = loader.load();

        // Set the FXML Content in the Scene
        Scene scene = new Scene(root, 900, 665);

        stage.setTitle("Music Lyrics GUI Client");
        stage.setScene(scene);
        stage.show();
    }

    private static RestTemplate createTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public static void saveLyrics(Lyrics lyrics) {
        RestTemplate restTemplate = createTemplate();
        String serviceUrl = "http://localhost:8080/api/lyrics/save";
        try {
            restTemplate.postForObject(serviceUrl, lyrics, Lyrics.class);
            System.out.println("Lyrics saved successfully.");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED) {
                System.err.println("HTTP 405 Error: Method Not Allowed. Check if the server supports the POST method for this endpoint.");
            } else {
                System.err.println("An error occurred while saving lyrics: " + e.getMessage());
            }
        } catch (RestClientException e) {
            System.err.println("An error occurred while communicating with the server: " + e.getMessage());
        }
    }

    public static void updateLyrics(Lyrics lyrics) {
        RestTemplate restTemplate = createTemplate();
        String serviceUrl = "http://localhost:8080/api/lyrics/update/" + lyrics.getId();
        restTemplate.put(serviceUrl, lyrics);

        System.out.println("Lyrics updated successfully.");
    }

    public static void deleteLyrics(int songId) {
        RestTemplate restTemplate = createTemplate();
        String serviceUrl = "http://localhost:8080/api/lyrics/delete/" + songId;
        restTemplate.delete(serviceUrl);

        System.out.println("Lyrics deleted successfully.");
    }
}
