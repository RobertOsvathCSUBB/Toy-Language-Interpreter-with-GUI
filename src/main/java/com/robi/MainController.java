package com.robi;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.List;
import com.robi.models.state.PrgState;
import com.robi.view.Interpreter;

public class MainController implements Initializable
{
    private List<PrgState> programs;
    private PrgState selectedProgram;

    @FXML
    private ListView<String> listView;

    public MainController()
    {
        this.programs = Interpreter.generatePrograms();
        this.listView = new ListView<String>();
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources)
    {
        this.populateListView();
    }

    public void populateListView()
    {
        int index = 1;
        for (PrgState p : this.programs)
        {
            listView.getItems().add(index + ": " + p.getOriginalPrg().toString());
            index++;
        }
    }

    @FXML
    public void setSelectedCommand() throws IOException
    {
        Integer index = this.listView.getSelectionModel().getSelectedIndex();
        this.selectedProgram = this.programs.get(index);
    }

    @FXML
    public void runSelectedCommand() throws IOException
    {
        App.openProgramRunWindow(this.selectedProgram);
    }
}
