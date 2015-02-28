package takahta.timesheetcsvparser;

import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.commons.io.FilenameUtils;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;

    @FXML
    private TextArea filepath;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        String[] files = filepath.getText().split("\n");
        for (String file : files) {
            modifyCsv(file);
        }
    }

    private void modifyCsv(String file) throws IOException {
        List<CsvModel> list;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // map csv to bean
        HeaderColumnNameTranslateMappingStrategy mapper = new HeaderColumnNameTranslateMappingStrategy();
        mapper.setType(CsvModel.class);
        mapper.setColumnMapping(createMapping());

        list = new CsvToBean().parse(mapper, fileReader);
        // 最終行は明細ではないので削除する
        list.remove(list.size() - 1);
        for (CsvModel bean : list) {
            // 日付に年月を追加する
            bean.setDate(FilenameUtils.getBaseName(file) + bean.getDate());
            // 作業内容の改行を<br>に変換する
            bean.setJobContext(bean.getJobContext().replaceAll("\n", "<br>"));
        }
        
        // output
        String outFile = FilenameUtils.getFullPath(file) + FilenameUtils.getBaseName(file) + "_mod.csv";
        ColumnPositionMappingStrategy<CsvModel> stat = new ColumnPositionMappingStrategy<>();
        stat.setType(CsvModel.class);
        stat.setColumnMapping(createColumnMap());
        BeanToCsv<CsvModel> bean = new BeanToCsv<>();

        try (CSVWriter writer = new CSVWriter(new FileWriter(outFile), '\t', CSVWriter.NO_QUOTE_CHARACTER)) {
            bean.write(stat, writer, list);
        }
    }

    private Map<String, String> createMapping() {
        Map<String, String> map = new HashMap<>();
        map.put("日付", "date");
        map.put("曜日", "day");
        map.put("出勤", "startTime");
        map.put("退勤", "endTime");
        map.put("作業内容", "jobContext");
        map.put("作業日区分", "jobDiv");
        map.put("休暇区分", "holidayDiv");
        map.put("控除通常", "deduction");
        map.put("控除深夜", "deductionNight");
        map.put("勤務時数", "workTime");
        map.put("残業時数", "overWork");
        map.put("休日勤務", "holidayWork");
        map.put("深夜勤務", "midnightWork");
        return map;
    }
    
    private String[] createColumnMap() {
        return new String[] {
                "date"
                ,"day"
                ,"startTime"
                ,"endTime"
                ,"jobContext"
                ,"jobDiv"
                ,"holidayDiv"
                ,"deduction"
                ,"deductionNight"
                ,"workTime"
                ,"overWork"
                ,"holidayWork"
                ,"midnightWork"};
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
