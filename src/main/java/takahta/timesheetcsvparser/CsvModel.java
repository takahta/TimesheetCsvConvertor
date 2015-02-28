package takahta.timesheetcsvparser;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CsvModel {
    /**
     * 日付
     */
    private String date;
    /**
     * 曜日
     */
    private String day;
    /**
     * 出勤
     */
    private String startTime;
    /**
     * 退勤
     */
    private String endTime;
    /**
     * 作業内容
     */
    private String jobContext;
    /**
     * 作業日区分
     */
    private String jobDiv;
    /**
     * 休暇区分
     */
    private String holidayDiv;
    /**
     * 控除通常
     */
    private String deduction;
    /**
     * 控除深夜
     */
    private String deductionNight;
    /**
     * 勤務時数
     */
    private String workTime;
    /**
     * 残業時数
     */
    private String overWork;
    /**
     * 休日勤務
     */
    private String holidayWork;
    /**
     * 深夜勤務
     */
    private String midnightWork;

}
