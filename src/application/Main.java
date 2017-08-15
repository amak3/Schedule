package application;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;


public class Main extends Application 
{
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws IOException 
	{
		VBox vbox = new VBox();
		Scene scene = new Scene(vbox);
		stage.setTitle("WMI UAM - Plan zajêæ");
		
		BorderPane root = new BorderPane();
		TableView<Row> table = new  TableView<Row>();

		RowReader rowReader = new RowReader();
		List<Row> list = rowReader.parseJsonToJavaObjects();		
		
		final ObservableList<Row> data = FXCollections.observableList(list);	
		
		Label labelForSubject = new Label("Rok studiów:");
		Label labelForYear = new Label("Przedmiot:");
		Label labelForLeader = new Label("Wyk³adowca:");
		Label labelForStudy = new Label("Kierunek studiów:");
		Label labelForCalendar = new Label("Od dnia:");
		Label labelForGroup = new Label("Grupa:");
		Label labelForCounter = new Label("Iloœæ zajêæ w semestrze:");
		
		TableColumn<Row, String> whenCol = new TableColumn<>("Data");
		TableColumn<Row, String> hourCol = new TableColumn<>("Godzina rozpoczêcia");
		TableColumn<Row, String> groupCol = new TableColumn<>("Grupa");
		TableColumn<Row, String> subjectCol = new TableColumn<>("Przedmiot");
		subjectCol.setMinWidth(200);
		TableColumn<Row, Integer> yearCol = new TableColumn<>("Rok studiów");
		TableColumn<Row, String> studyCol = new TableColumn<>("Kierunek");
		TableColumn<Row, String> leaderCol = new TableColumn<>("Wyk³adowca");
		TableColumn<Row, String> room1Col = new TableColumn<>("Sala 1");
		TableColumn<Row, String> room2Col = new TableColumn<>("Sala 2");
				
		whenCol.setCellValueFactory(new PropertyValueFactory<>("when"));
		hourCol.setCellValueFactory(new PropertyValueFactory<>("hour"));
		groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
		subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
		studyCol.setCellValueFactory(new PropertyValueFactory<>("study"));
		leaderCol.setCellValueFactory(new PropertyValueFactory<>("leader"));
		room1Col.setCellValueFactory(new PropertyValueFactory<>("room1"));
		room2Col.setCellValueFactory(new PropertyValueFactory<>("room2"));
		
		
		    
		FilteredList<Row> filteredData = new FilteredList<>(data, p -> true);
		
		TextField filterFieldSubject = new TextField();
		filterFieldSubject.setTooltip(new Tooltip("Zacznij wpisywaæ"));
		
		TextField filterFieldLeader = new TextField();
		filterFieldLeader.setTooltip(new Tooltip("Zacznij wpisywaæ"));
        
        ObservableList<String> studyYears = FXCollections.observableArrayList("Rok pierwszy", "Rok drugi", "Rok trzeci");
		ChoiceBox<String> buttonYear = new ChoiceBox<>(studyYears);
        buttonYear.setTooltip(new Tooltip("Wybierz rok studiów"));
      
        ObservableList<String> studySubjects = FXCollections.observableArrayList(
        	    "Informatyka - In¿ynier", "Informatyka - Uzupe³niaj¹ce (magister po in¿ynier)", "Informatyka - Uzupe³niaj¹ce (magister in¿ynier)",
        	    "Nauczanie Matematyki i Informatyki - Licencjat", "Matematyka - Licencjackie", "Nauczanie Matematyki i Informatyki - Licencjat", 
        	    "Nauczanie Matematyki i Informatyki - Uzupe³niaj¹ce", "Podyplomowe - Matematyka");
		ChoiceBox<String> buttonStudy = new ChoiceBox<>(studySubjects);
        buttonStudy.setTooltip(new Tooltip("Wybierz kierunek studiów"));
        
        ObservableList<String> studyGroup = FXCollections.observableArrayList("1WA", "1CA", "1CB", "1CC", "1CD", "1CE");
        ChoiceBox<String> buttonGroup = new ChoiceBox<>(studyGroup);
        buttonGroup.setTooltip(new Tooltip("Wybierz grupê"));
     
        DatePicker calendar = new DatePicker();   

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() -> row -> isDisplayingRow(row, filterFieldSubject, filterFieldLeader, buttonYear, buttonStudy, buttonGroup, calendar),
        		filterFieldSubject.textProperty(), filterFieldLeader.textProperty(), buttonYear.valueProperty(), buttonStudy.valueProperty(), buttonGroup.valueProperty(), calendar.valueProperty()));
       
                
        SortedList<Row> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        Button subjectCounterButton = new Button();
        subjectCounterButton.setText("Policz zajêcia");
        final Text subjectCounterText = new Text();
        subjectCounterButton.setOnAction(new EventHandler<ActionEvent>() {


        	@Override
        	public void handle(ActionEvent event) {

        		String yearFromButton = buttonYear.getValue(); 
        		
        		if (yearFromButton != null)
        		{
        			int yearFromList = test(yearFromButton);
        			
        			Map<String, Integer> subjectCounter = new HashMap<>();	

        			for (int i = 0; i < filteredData.size(); i++) 
        			{
        				int year =  filteredData.get(i).getYear();
        				if (year == yearFromList)
        				{
        					String subject = filteredData.get(i).getSubject();
        					if (subjectCounter.containsKey(subject))
        					{
        						Integer counter = subjectCounter.get(subject);
        						counter += 1;
        						subjectCounter.put(subject, counter);
        					}
        					else
        					{
        						subjectCounter.put(subject, 1);
        					}	
        				}	            		
        			}
        			subjectCounterText.setText(subjectCounter.toString());	        		
        		}
        		else
        		{	            	  
        			subjectCounterText.setText("Wybierz rok");

        		}
        	}
        });
		ObservableList<TableColumn<Row, ?>> columns = table.getColumns();
		columns.addAll(Arrays.asList(whenCol, hourCol, groupCol, subjectCol, yearCol, studyCol, leaderCol, room1Col, room2Col));
		table.getSortOrder().add(whenCol);
		table.getSortOrder().add(hourCol);
		table.getSortOrder().add(groupCol);
				
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.autosize();

		vbox.getChildren().addAll(labelForYear, filterFieldSubject, labelForLeader, filterFieldLeader, labelForSubject, buttonYear, labelForStudy, buttonStudy, labelForGroup,buttonGroup, labelForCalendar, calendar, labelForCounter, subjectCounterButton, subjectCounterText, root, table); 
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.autosize();

		stage.setScene(scene);
		stage.show();
	}
	
	boolean isDisplayingRow(Row row, TextField filterFieldSubject, TextField filterFieldLeader, ChoiceBox<String> buttonYear, ChoiceBox<String> buttonStudy, ChoiceBox<String> buttonGroup, DatePicker calendar) 
	{
		String dataFromFilterFieldSubject = filterFieldSubject.getText().toLowerCase();
		String dataFromFilterFieldLeader = filterFieldLeader.getText().toLowerCase();
		int dataFromButtonYear = buttonYear.getSelectionModel().getSelectedIndex();
		String dataFromButtonStudy = buttonStudy.getSelectionModel().getSelectedItem();
		LocalDate dataFromCalendar = calendar.getValue();
		String dataFromButtonGroup = buttonGroup.getSelectionModel().getSelectedItem();
		
		boolean result = (row.getSubject().toLowerCase().contains(dataFromFilterFieldSubject) || dataFromFilterFieldSubject.equals(""));
		result = result && (row.getLeader().toLowerCase().contains(dataFromFilterFieldLeader) || dataFromFilterFieldLeader.equals(""));
        result = result && (row.getYear() == dataFromButtonYear + 1 || dataFromButtonYear == -1);
        result = result && (row.getStudy().equals(dataFromButtonStudy) || dataFromButtonStudy == null);
        result = result && ((row.getWhen().equals(dataFromCalendar) || dataFromCalendar == null) || row.getWhen().isAfter(dataFromCalendar));
        result = result && (row.getGroup().equals(dataFromButtonGroup) || dataFromButtonGroup == null);
        
        return result;
	}
	
	public int test(String yearFromButton)
	{
		int yearFromList = 0;
		
		switch (yearFromButton)
		{
		case "Rok pierwszy":
			yearFromList = 1;
			break;
		case "Rok drugi":
			yearFromList = 2;
			break;
		case "Rok trzeci":
			yearFromList = 3;
			break;
		}
		return yearFromList;
	}	
}
