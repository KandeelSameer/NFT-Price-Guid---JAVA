package application;
	


import gui.MainPageController;
import javafx.application.Application;
import javafx.stage.Stage;



public class Main extends Application {
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		// TODO Auto-generated method stub
		MainPageController MPC = new MainPageController();
		MPC.start(stage);
		
	}
	@Override
	public void stop() throws Exception {
		System.exit(0);
	}
}
