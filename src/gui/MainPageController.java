package gui;

import Model.*;
import application.EmailApi;
import application.ExcelApi;
import application.Logic;
import application.LogicforFiles;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainPageController implements Initializable, Runnable {

	@FXML
	private Button saveListBtn;

	@FXML
	private Button uploadListBtn;

	@FXML
	private Button saveRefBtn;

	@FXML
	private Button saveEmailSecBtn;

	@FXML
	private Button saveThesholdbtn;

	@FXML
	private Button saveSendToBtn;

	@FXML
	private ComboBox<String> howManyid;

	@FXML
	private TextField refTxtSec;

	@FXML
	private TextField emailTxtSec;

	@FXML
	private TextField emailThresholdTxt;

	@FXML
	private TextField emailsTxtUpd;

	@FXML
	private TextField SerachTxt;
	@FXML
	private Label currencyETH;
	@FXML
	private ProgressBar progBarid;
	@FXML 
	private Label LoadFinish;

	// for the table
	@FXML
	private TableView<ModelForTable> tbData;
	@FXML
	public TableColumn<ModelForTable, String> collectionName;
	@FXML
	public TableColumn<ModelForTable, Double> openSeaPrice;
	@FXML
	public TableColumn<ModelForTable, Double> magicEdenPrice;
	@FXML
	public TableColumn<ModelForTable, Double> differnt;

	private ObservableList<ModelForTable> studentsModelsObserv = FXCollections.observableArrayList();
	public String SOLtoUSD = "";

	ArrayList<CollectionsNFT> myCollectionMagicEden = new ArrayList<CollectionsNFT>();// for magic eden
	ArrayList<ModelForTable> myCollection = new ArrayList<ModelForTable>();// for boths

	MainPageController instance = this;
	// thread
	Thread thread = new Thread(this);

	Set<String> emails = new HashSet<String>();
	Set<String> savedCollectionNames = new HashSet<String>();
	int timeToRefresh = 360;
	int sendEmailEvery = 100;
	int threshold = 100;
	EmailApi emailHolder =  EmailApi.getInstance();
	ExcelApi excelHolder = ExcelApi.getInstance();
	// timer to send an email
	Timer timerForEmail = new Timer();
	HelperCallEmail helperMail = new HelperCallEmail();

	public MainPageController() {

	}

	public void saveList() {
		// thread.stop();
		// create a text input dialog
		TextInputDialog td = new TextInputDialog();
		td.setTitle("Save File as name");
		td.setContentText("File Name:");

		// show the text input dialog
		td.showAndWait();

		// set the text of the label
		String FileName = td.getEditor().getText();
		if (!FileName.equals(""))
			excelHolder.exportToExcel(FileName, myCollection);
		System.out.println("There is no list to be Saved");
	}

	// a array list to hold the name of the symbols that we get from a file
	ArrayList<String> symbolsFromFile;

	public void UloadList() {

		symbolsFromFile = LogicforFiles.readFromFile("LogicforFiles.txt");
		// System.out.println("List uploaded");
		if (!thread.isAlive())
		{
		thread = new Thread(instance);
		thread.start();
		System.out.println("Finish.................................");
		}
	}

	@SuppressWarnings("unchecked")
	public void UdateRefreshRate() {
		try {
			timeToRefresh = Integer.parseInt(refTxtSec.getText().toString());
			if (thread.isAlive()) {

			} else {
				thread = new Thread(this);
				thread.start();
			}
			System.out.println("Refresh Rate updated" + timeToRefresh);
		} catch (Exception e) {
			System.out.println("not a number");
		}

	}

	public void UdateEmailRate() {
		try {
			sendEmailEvery = Integer.parseInt(emailTxtSec.getText().toString());
			timerForEmail.cancel();
			helperMail.cancel();
			timerForEmail.purge();

			timerForEmail = new Timer();
			helperMail = new HelperCallEmail();
			timerForEmail.schedule(helperMail, 1000 * sendEmailEvery, 1000 * sendEmailEvery);
			System.out.println("email Rate updated" + sendEmailEvery);
		} catch (Exception e) {
			System.out.println("not a number" + e.getMessage());
		}

	}

	public void UpdateThreshold() {
		try {
			threshold = Integer.parseInt(emailThresholdTxt.getText().toString());

			System.out.println("Threshold updated" + threshold);
		} catch (Exception e) {
			System.out.println("not a number");
		}

	}

	public void updateEmails() {

		String string = emailsTxtUpd.getText().toString();
		String[] parts = string.split(";");
		for (String s : parts) {
			emails.add(s);
		}
		// emailHolder.sendMail(emails, "good vibes oh yea!!!!!");
		System.out.println(emails);
		System.out.println("emails updated");
	}

	public void Serach() {

		if (SerachTxt.getText().isEmpty() && thread.isAlive()) {
			return;
		}
		if (SerachTxt.getText().isEmpty() && !thread.isAlive()) {
			System.out.println("started  after serach ");
			thread = new Thread(instance);
			thread.start();
			return;

		}
		System.out.println(SerachTxt.getText().toString());
		if (savedCollectionNames.contains(SerachTxt.getText().toString())) {
			thread.stop();
			studentsModelsObserv = FXCollections.observableArrayList();
			for (int i = 0; i < myCollection.size(); i++)
				if (myCollection.get(i).getCollectionName().equals(SerachTxt.getText().toString())) {
					studentsModelsObserv.add(myCollection.get(i).getCollect(SerachTxt.getText().toString()));

					tbData.setItems(studentsModelsObserv);
					break;
				}
		}

	}

	public void start(Stage stage)   {

		System.out.println(getClass().getResource("mainFx.fxml"));
	
		Parent root;
		FXMLLoader loader = new FXMLLoader();
	try {
			root = loader.load(getClass().getResource("mainFx.fxml").openStream());
			Scene scene = new Scene(root);
			 //scene.getStylesheets().add(getClass().getResource("/gui/AcademicFrame.css").toExternalForm());
			stage.setTitle("NFT TOP 100");
			stage.setScene(scene);

			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// make sure the property value factory should be exactly same as the e.g
		// getStudentId from your model class
		collectionName.setCellValueFactory(new PropertyValueFactory<>("collectionName"));
		magicEdenPrice.setCellValueFactory(new PropertyValueFactory<>("magicEdenPrice"));
		openSeaPrice.setCellValueFactory(new PropertyValueFactory<>("openSeaPrice"));
		differnt.setCellValueFactory(new PropertyValueFactory<>("diff"));

		ObservableList<String> options = FXCollections.observableArrayList("10", "25", "50", "100");
		howManyid.getItems().addAll(options);
		howManyid.setValue("100");
		// strat the run that load the table and care of it and start the timer
		thread.start();
		timerForEmail.schedule(helperMail, 1000 * sendEmailEvery, 1000 * sendEmailEvery);

		SOLtoUSD = Logic.SOLtoUSD();

		currencyETH.setText(SOLtoUSD);

		// init the deafult 4 inputs textFields
		refTxtSec.setText(String.valueOf(timeToRefresh));
		emailTxtSec.setText(String.valueOf(sendEmailEvery));
		emailThresholdTxt.setText(String.valueOf(threshold));

		// progrees
		// progBarid= new ProgressBar(0);
		// progBarid.setProgress(0.5);
		LoadFinish.setText("Loading.. please wait until it finish");
	}

	@Override
	public void run() {// thread run to load the table
		System.out.println("Lets start to load the table ;)");
		myCollection = new ArrayList<ModelForTable>();
		studentsModelsObserv = FXCollections.observableArrayList();
		savedCollectionNames = new HashSet<String>();

		try {
			ArrayList<String> symbolStrings = Logic.GetCollectionsSymbols();
			// System.out.println(symbolStrings.size());
			for (int i = 0; i < symbolStrings.size() ; i++) {
				String magicEP, openEP, symbol, openEPSOL;
				symbol = symbolStrings.get(i);
				magicEP = Logic.GetfloorPriceMagicEden(symbolStrings.get(i));
				openEP = Logic.GetfloorPriceOpenSea(symbolStrings.get(i));
				openEPSOL = Logic.floorPriceETHtoSOL(openEP);

				Double differnt = Logic.getDiffertns(openEPSOL, magicEP);

				// if(!(magicEP.equals("N/A")||openEP.equals("N/A")))
				// {

				System.out.println(symbolStrings.get(i));
				myCollection.add(new ModelForTable(symbol, magicEP, openEPSOL, differnt));
				savedCollectionNames.add(symbol);
				// studentsModelsObserv.addAll(myCollection);
				studentsModelsObserv.add(new ModelForTable(symbol, magicEP, openEPSOL, differnt));
				tbData.setItems(studentsModelsObserv);
				progBarid.setProgress((double) (i + 1) / symbolStrings.size());
				//
				/// }
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		// studentsModelsObserv.addAll(myCollection);

		// tbData.setItems(studentsModelsObserv);
		if(symbolsFromFile!=null)
		for (int i = 0; i < symbolsFromFile.size(); i++) {
			String magicEP, openEP, symbol, openEPSOL;
			symbol = symbolsFromFile.get(i);
			magicEP = Logic.GetfloorPriceMagicEden(symbolsFromFile.get(i));
			openEP = Logic.GetfloorPriceOpenSea(symbolsFromFile.get(i));
			openEPSOL = Logic.floorPriceETHtoSOL(openEP);

			Double differnt = Logic.getDiffertns(openEPSOL, magicEP);

			// if(!(magicEP.equals("N/A")||openEP.equals("N/A")))
			// {

			System.out.println(symbolsFromFile.get(i));
			myCollection.add(new ModelForTable(symbol, magicEP, openEPSOL, differnt));
			savedCollectionNames.add(symbol);
			// studentsModelsObserv.addAll(myCollection);
			studentsModelsObserv.add(new ModelForTable(symbol, magicEP, openEPSOL, differnt));
			tbData.setItems(studentsModelsObserv);
			progBarid.setProgress((double) (i + 1) / symbolsFromFile.size());
		}
		if (timeToRefresh != -1) {
			Timer timer = new Timer();
			TimerTask task = new Helper();

			timer.schedule(task, 1000 * timeToRefresh);

		}

	}

	class Helper extends TimerTask {
		public int i = 0;

		public void run() {
			System.out.println("The Timer is callled and it wil call the thread...........");
			if (!thread.isAlive())
			{
			thread = new Thread(instance);
			thread.start();
			System.out.println("Finish.................................");
			}
		}
	}

	class HelperCallEmail extends TimerTask {

		@Override
		public void run() {
		
			System.out.println("sending mails to " + emails);
			if (emails.size() > 0) {
				for (int i = 0; i < myCollection.size(); i++) {
					if (myCollection.get(i).getDiff() > threshold) {
						emailHolder.sendMail(emails,
								"collection name :" + myCollection.get(i).getCollectionName() + "\n"
										+ "openSea price[sol]:" + myCollection.get(i).getOpenSeaPrice() + "\n"
										+ "magicEden price[sol]:" + myCollection.get(i).getMagicEdenPrice() + "\n"
										+ "Differance :" + myCollection.get(i).getDiff(),
								"get Your Chance!!!:" + myCollection.get(i).getCollectionName());
					}
				}
				// System.out.println("sending mails to "+emails);
				// emailHolder.sendMail(emails, "good vibes oh yea!!!!!");
			}
			// emailHolder.sendMail(emails, "good vibes oh yea!!");
		}
	}

	public void optionSelected() {
		System.out.println(howManyid.getValue());
		int limit = 0;
		if (!thread.isAlive()) {
			limit = Integer.parseInt(howManyid.getValue());
			studentsModelsObserv = FXCollections.observableArrayList();
			for (int i = 0; i < limit; i++) {
				studentsModelsObserv.add(myCollection.get(i));
			}
			tbData.setItems(studentsModelsObserv);
		}
	}
}
