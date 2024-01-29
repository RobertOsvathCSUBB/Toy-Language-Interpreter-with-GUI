package com.robi;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.ILatchTable;
import com.robi.models.ADTs.IMap;
import com.robi.models.ADTs.IStack;
import com.robi.models.ADTs.MyHeap;
import com.robi.models.exception.MyException;
import com.robi.models.state.PrgState;
import com.robi.models.statements.IStatement;
import com.robi.models.values.IValue;
import com.robi.models.values.RefValue;
import com.robi.repository.IRepository;
import com.robi.repository.MemRepository;
import javafx.util.Pair;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class ProgramViewController
{
    private PrgState program;
    private IRepository repo;
    private ExecutorService executor;
    private Integer currentShownThreadID;
    private ObservableList<Pair<Integer, String>> heapEntries;
    private ObservableList<String> outEntries;
    private ObservableList<String> fileEntries;
    private ObservableList<String> threadIDs;
    private ObservableList<String> stackEntries;
    private ObservableList<Pair<String, String>> symTableEntries;
    private ObservableList<Pair<Integer, Integer>> latchTableEntries;

    @FXML
    private TextField initialProgram;
    @FXML
    private TextField nrOfProgramStates;
    @FXML
    private TableView<Pair<Integer, String>> heapTable;
    @FXML
    private ListView<String> outList;
    @FXML
    private ListView<String> fileTable;
    @FXML
    private ComboBox<String> threadList;
    @FXML
    private ListView<String> stackList;
    @FXML
    private TableView<Pair<String, String>> symTable;
    @FXML
    private TableView<Pair<Integer, Integer>> latchTable;

    public ProgramViewController()
    {
        this.repo = new MemRepository();
        this.executor = Executors.newFixedThreadPool(2);
        this.heapEntries = FXCollections.observableArrayList();
        this.outEntries = FXCollections.observableArrayList();
        this.fileEntries = FXCollections.observableArrayList();
        this.threadIDs = FXCollections.observableArrayList();
        this.stackEntries = FXCollections.observableArrayList();
        this.symTableEntries = FXCollections.observableArrayList();
        this.latchTableEntries = FXCollections.observableArrayList();
        
        this.initialProgram = new TextField();
        this.nrOfProgramStates = new TextField();

        this.heapTable = new TableView<>();
        TableColumn<Pair<Integer, String>, Integer> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        locationColumn.setMinWidth(100);
        TableColumn<Pair<Integer, String>, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        valueColumn.setMinWidth(100);

        this.outList = new ListView<>();
        this.fileTable = new ListView<>();
        this.threadList = new ComboBox<>();
        this.stackList = new ListView<>();

        this.symTable = new TableView<>();
        TableColumn<Pair<String, String>, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        nameColumn.setMinWidth(100);
        TableColumn<Pair<String, String>, String> valueColumn2 = new TableColumn<>("Value");
        valueColumn2.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        valueColumn2.setMinWidth(100);

        this.latchTable = new TableView<>();
        TableColumn<Pair<Integer, Integer>, Integer> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        locationCol.setMinWidth(100);
        TableColumn<Pair<Integer, Integer>, Integer> counterCol = new TableColumn<>("Count");
        counterCol.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        counterCol.setMinWidth(100);


        Platform.runLater(() -> {
            this.heapTable.getColumns().addAll(locationColumn, valueColumn);
            this.heapTable.setItems(this.heapEntries);
            this.outList.setItems(outEntries);
            this.fileTable.setItems(fileEntries);
            this.threadList.setItems(threadIDs);
            this.threadList.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
                this.threadIDs.forEach(id -> {
                    if (id.equals(newVal) && this.currentShownThreadID != Integer.parseInt(newVal))
                    {
                        this.currentShownThreadID = Integer.parseInt(newVal);
                        System.out.println("Thread changed to " + newVal);
                        this.refresh();
                    }
                });
            });
            this.stackList.setItems(stackEntries);
            this.symTable.getColumns().addAll(nameColumn, valueColumn2);
            this.symTable.setItems(this.symTableEntries);
            this.latchTable.getColumns().addAll(locationCol, counterCol);
            this.latchTable.setItems(this.latchTableEntries);
        });
    }

    public void setProgram(PrgState program)
    {
        this.program = program;
        this.repo.add(program);
        this.initialProgram.setText(this.program.getOriginalPrg().toString());
        this.currentShownThreadID = this.program.getID();
        this.repo.setLogFile("log" + this.program.getID() + ".txt");
        Platform.runLater(() -> {
            this.refresh();
        });
    }

    @FXML
    public void oneStepForAllPrg() throws IOException 
    {
        List<PrgState> states = this.removeCompletedPrg(this.repo.getPrgList());

        if (states.size() == 0)
        {
            this.executor.shutdownNow();
            this.repo.setPrgList(states);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Program has finished");
            alert.setContentText("There are no more threads to execute");
            alert.showAndWait();
        }

        states.forEach(prg -> {
            try {
                this.repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });

        IHeap heap = this.repo.getPrgList().get(0).getHeap();
        heap.setContent(this.garbageCollector(this.getAddrFromSymTables(), heap));

        List<Callable<PrgState>> callList = states.stream()
                                                    .map((PrgState p) -> (Callable<PrgState>)(p::oneStep))
                                                    .collect(Collectors.toList()); 

        try 
        {
            List<PrgState> newStates = this.executor.invokeAll(callList).stream()
                                                                        .map(future -> {
                                                                            try {
                                                                                return future.get();
                                                                            }
                                                                            catch (Exception e) {
                                                                                Alert alert = new Alert(AlertType.ERROR);
                                                                                alert.setTitle("Error");
                                                                                alert.setHeaderText("Error while executing program");
                                                                                alert.setContentText(e.getMessage());
                                                                                alert.showAndWait();
                                                                                return null;
                                                                            }
                                                                        })
                                                                        .filter(p -> p != null)
                                                                        .collect(Collectors.toList());

            states.addAll(newStates);

            states.forEach(prg -> {
                try {
                    this.repo.logPrgStateExec(prg);
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                }
            });
            this.repo.setPrgList(states);
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        this.refresh();
    }

    private List<Integer> getAddrFromSymTables()
    {
        List<PrgState> states = this.repo.getPrgList();
        List<IValue> values = states.stream()
                .map(PrgState::getSymTable)
                .map(IMap::values)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        return values.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue)v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }

    private IHeap garbageCollector(List<Integer> symTableAddr, IHeap heap)
    {
        Map<Integer, IValue> newHeapMap = heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        IHeap newHeap = new MyHeap();
        newHeapMap.forEach( (Integer k, IValue v) -> {
            try {
                newHeap.add(k, v);
            } catch (MyException e) {}
        });
        return newHeap;
    }

    private List<PrgState> removeCompletedPrg(List<PrgState> states)
    {   
        return states.stream()
                     .filter(p -> {
                        if (!p.isNotCompleted())
                        {
                            if (p.getID().equals(this.currentShownThreadID))
                            {
                                for (PrgState prg : states)
                                {
                                    if (prg.getID() != this.currentShownThreadID && prg.isNotCompleted())
                                    {
                                        this.currentShownThreadID = prg.getID();
                                        break;
                                    }
                                }
                                this.refresh();
                            }
                            return false;
                        }
                        return true;
                     })
                     .collect(Collectors.toList());
    }

    public void refresh()
    {   
        this.nrOfProgramStates.setText("Number of program states: " + this.repo.getPrgList().size());
        this.heapEntries.clear();
        this.heapEntries.addAll(this.repo.getPrgList().get(0).getHeap().entrySet().stream()
                                                                                        .map(e -> new Pair<>(e.getKey(), e.getValue().toString()))
                                                                                        .collect(Collectors.toList())
        );

        this.outEntries.clear();
        this.outEntries.addAll(this.repo.getPrgList().get(0).getOut().getAll().stream()
                                                                                    .map(IValue::toString)
                                                                                    .collect(Collectors.toList())
        );

        this.fileEntries.clear();
        this.fileEntries.addAll(this.repo.getPrgList().get(0).getFileTable().keys().stream()
                                                                                         .map(IValue::toString)
                                                                                         .collect(Collectors.toList())
        );

        this.threadIDs.clear();
        this.threadIDs.addAll(this.repo.getPrgList().stream()
                                                     .map(PrgState::getID)
                                                     .map(Object::toString)
                                                     .collect(Collectors.toList())
        );
        this.threadList.setValue(this.currentShownThreadID.toString());
        
        List<String> newStackEntries = this.repo.getPrgList().stream()
                                                             .filter(p -> p.getID().equals(this.currentShownThreadID))
                                                             .map(PrgState::getStack)
                                                             .map(IStack::getAll)
                                                             .flatMap(Collection::stream)
                                                             .map(IStatement::toString)
                                                             .collect(Collectors.toList());
        Collections.reverse(newStackEntries);
        this.stackEntries.clear();
        this.stackEntries.addAll(newStackEntries);


        this.symTableEntries.clear();
        this.symTableEntries.addAll(this.repo.getPrgList().stream()
                                                           .filter(p -> p.getID().equals(this.currentShownThreadID))
                                                           .map(PrgState::getSymTable)
                                                           .map(IMap::entrySet)
                                                           .flatMap(Collection::stream)
                                                           .map(e -> new Pair<>(e.getKey(), e.getValue().toString()))
                                                           .collect(Collectors.toList())
        );

        this.latchTableEntries.clear();
        this.latchTableEntries.addAll(this.repo.getPrgList().get(0).getLatchTable().entrySet().stream()
                                                                                        .map(e -> new Pair<>(e.getKey(), e.getValue()))
                                                                                        .collect(Collectors.toList())
        );
    }
}
