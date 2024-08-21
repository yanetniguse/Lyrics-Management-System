package com.example.projectclient;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class MyUIController implements Initializable {

    @FXML
    private TextArea lyricsTextArea;
    @FXML
    private TextField songTitle;
    @FXML
    private TextField artistName;
    @FXML
    private ListView<String> lyricsList;
    private int selectedSongId = 0; // Initialize with a default value
    @FXML
    private Button sav;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateListView();

        lyricsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Extract song ID from the selected item
                String[] parts = newValue.split(" - ");
                selectedSongId = Integer.parseInt(parts[0]);
                sav.setDisable(false);
            } else {
                sav.setDisable(true);
            }
        });

        // Double-click event to populate song details for editing
        lyricsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = lyricsList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String[] parts = selectedItem.split(" - ");
                    selectedSongId = Integer.parseInt(parts[0]);
                    String title = parts[1].trim();
                    String artist = parts[2].trim();
                    String lyrics = parts[3].trim();

                    // Populate song details in text fields
                    songTitle.setText(title);
                    artistName.setText(artist);
                    lyricsTextArea.setText(lyrics);
                }
            }
        });
    }

    private void populateListView() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/api/lyrics/get-all");

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(result);

                for (JsonNode node : jsonNode) {
                    int id = node.get("id").asInt();
                    String title = node.get("songTitle").asText();
                    String artist = node.get("songArtist").asText();
                    String lyrics = node.get("songLyrics").asText();
                    lyricsList.getItems().add(id + " - " + title + " - " + artist + " - " + lyrics);
                }
            }

            EntityUtils.consume(entity);
            response.close();
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void updateSong() {
        if (selectedSongId != 0) {
            String newTitle = songTitle.getText().trim();
            String newArtist = artistName.getText().trim();
            String newLyrics = lyricsTextArea.getText().trim();

            if (newTitle.isBlank() || newArtist.isBlank() || newLyrics.isBlank()) {
                // Display message
                Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                newAlert.setTitle("No null texts");
                newAlert.setHeaderText(null);
                newAlert.setContentText("Kindly ensure that all texts are filled in");
                newAlert.showAndWait();
            } else {
                Lyrics lyrics = new Lyrics();
                lyrics.setId(selectedSongId);
                lyrics.setSongTitle(newTitle);
                lyrics.setSongArtist(newArtist);
                lyrics.setSongLyrics(newLyrics);

                // Update the song details in your data source
                ProjectClientApplication.updateLyrics(lyrics);

                // Show a confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Song Updated");
                alert.setHeaderText(null);
                alert.setContentText("Song details have been updated.");
                alert.showAndWait();

                // Clear the input fields
                artistName.clear();
                songTitle.clear();
                lyricsTextArea.clear();

                // Refresh the ListView after updating the song
                populateListView();

                // Update the specific item in the list
                String updatedSong = selectedSongId + " - " + newTitle + " - " + newArtist + " - " + newLyrics;
                int selectedIndex = lyricsList.getSelectionModel().getSelectedIndex();
                lyricsList.getItems().set(selectedIndex, updatedSong);

                selectedSongId = 0;
            }
        } else {
            // No song selected, display an error message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Song Selected");
            alert.setHeaderText(null);
            alert.setContentText("Double click song to update");
            alert.showAndWait();
        }
    }

    @FXML
    public void saveSong() {
        sav.setDisable(false);

        String songArtist = artistName.getText().trim();
        String title = songTitle.getText().trim();
        String songLyrics = lyricsTextArea.getText().trim();

        if (songArtist.isBlank() || title.isBlank() || songLyrics.isBlank()) {
            // Display message
            Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
            newAlert.setTitle("No null texts");
            newAlert.setHeaderText(null);
            newAlert.setContentText("Kindly ensure that all texts are filled in");
            newAlert.showAndWait();
        } else {
            Lyrics lyrics = new Lyrics(title, songLyrics, songArtist);

            ProjectClientApplication.saveLyrics(lyrics);

            artistName.clear();
            songTitle.clear();
            lyricsTextArea.clear();

            // Refresh the ListView after saving a song
            populateListView();

            // Display message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Song Saved");
            alert.setHeaderText(null);
            alert.setContentText("Your song's lyrics have been added");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteSong() {
        if (selectedSongId != 0) {
            // Prompt the user for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this song?");
            alert.setContentText("This action cannot be undone.");

            // Handle user's response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ProjectClientApplication.deleteLyrics(selectedSongId);

                    // Remove the deleted song from the ListView
                    String selectedSong = selectedSongId + " - ";
                    lyricsList.getItems().removeIf(item -> item.startsWith(selectedSong));

                    selectedSongId = 0;
                }
            });

            // Display message
            Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
            newAlert.setTitle("Song Deleted");
            newAlert.setHeaderText(null);
            newAlert.setContentText("Selected song has been deleted");
            newAlert.showAndWait();
        } else {
            // No song selected, display an error message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Song Selected");
            alert.setHeaderText(null);
            alert.setContentText("Click on a song to delete");
            alert.showAndWait();
        }
    }
}

