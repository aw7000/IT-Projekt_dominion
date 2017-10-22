package Dominion.Client.ClientClasses;

import java.util.ArrayList;

import Dominion.ServiceLocator;
import Dominion.Client.abstractClasses.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards (MVC Pattern), Joel Henz (GUI)
 */
public class Client_View_lobby extends View<Client_Model> {
    Button send;
    Button profile;
    Button newGame;
    Button enterGame;
    
    TextField tf_message;

    TextArea ta;
    Scene scene;
    ServiceLocator sl = ServiceLocator.getServiceLocator();
    String PlayerName;
    Label name;
    Label yourSign;
    
    ListView <String> gameList;
    ObservableList<String> names;

	public Client_View_lobby(Stage stage, Client_Model model) {
		super(stage, model);
        stage.setTitle("Dominion");
    }
	
	/**
     * @author Joel Henz
     */
	protected Scene create_GUI() {

	    ServiceLocator sl = ServiceLocator.getServiceLocator();  
	    
		BorderPane root = new BorderPane();
		
		//resources
		
		names = FXCollections.observableArrayList(
		          "Julia", "Ian", "Sue", "Matthew", "Hannah", "Stephan", "Denise");
		gameList = new ListView<String>(names);

		send = new Button("senden");
		profile = new Button ("Profil");
		newGame = new Button ("neues Spiel");
		enterGame = new Button ("Spiel beitreten");
		enterGame.setDisable(true);
		
		tf_message = new TextField();
		name = new Label();
		name.setText("Ihr Name: "+model.getName());
		
		Label message = new Label();
		message.setText("Ihre Nachricht");
		sl.setTextArea();
		ta = sl.getTextArea();
		ta.setEditable(false);
		ta.setMaxWidth(500);
		ta.setMaxHeight(200);
		
		/**
	     * auto resize when window is resized
	     */
		ta.prefWidthProperty().bind(root.widthProperty());
		ta.prefHeightProperty().bind(root.heightProperty());
		
		VBox vb1 = new VBox();
		HBox hb2 = new HBox();
		hb2.getChildren().addAll(message,tf_message,send);
		
		vb1.getChildren().addAll(name,hb2);
		
		HBox hb1 = new HBox();
		hb1.getChildren().addAll(profile,vb1,ta);
		HBox.setMargin(profile, new Insets(0,20,0,0));
		//hb1.setAlignment(Pos.CENTER);
		
		HBox hb3 = new HBox();
		hb3.getChildren().addAll(gameList,enterGame);
		
		root.setTop(hb1);
		root.setLeft(newGame);
		root.setBottom(hb3);

		this.scene = new Scene (root, 800,800);

        return scene;
	}

}