package com.example.employeeturniketapp.service;

import com.example.employeeturniketapp.constants.AppConst;
import com.example.employeeturniketapp.entity.EmpTurnAction;
import com.example.employeeturniketapp.entity.Employee;
import com.example.employeeturniketapp.entity.temp.ApiResult;
import com.example.employeeturniketapp.payload.reqDto.DateAndTimeDto;
import com.example.employeeturniketapp.payload.reqDto.EmpTurnActionReqDTO;
import com.example.employeeturniketapp.payload.resDto.EmpTurnActionResDTO;
import com.example.employeeturniketapp.repository.EmpTurnActionRepository;
import com.example.employeeturniketapp.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpTurnActionServiceImpl implements EmpTurnActionService {

    private final EmpTurnActionRepository empTurnActionRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ApiResult getAllEmpTurnAction() {
        List<EmpTurnActionResDTO> empTurnActionResDTOS = empTurnActionRepository.findAll()
                .stream().map(this::castEmpTurnActionToEmpTurnActionResDto).collect(Collectors.toList());
        return new ApiResult(empTurnActionResDTOS, true);
    }

    @Override
    public ApiResult getEmpTurnActionById(Integer id) {
        Optional<EmpTurnAction> optionalEmpTurnAction = empTurnActionRepository.findById(id);
        if (optionalEmpTurnAction.isPresent()) {
            EmpTurnActionResDTO empTurnActionResDTO = castEmpTurnActionToEmpTurnActionResDto(optionalEmpTurnAction.get());
            return new ApiResult(empTurnActionResDTO, true);
        } else {
            return new ApiResult("Bunday Turniket tarihi topilmadi", false);
        }
    }

    @Override
    public ApiResult addNewEmpTurnAction(EmpTurnActionReqDTO empTurnActionReqDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empTurnActionReqDTO.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            EmpTurnAction empTurnAction = castEmpTurnActionReqDtoToEmpTurnAction(empTurnActionReqDTO, optionalEmployee.get());
            empTurnActionRepository.save(empTurnAction);
            return new ApiResult("Employee Turnicet Action muvaffaqiyatli saqlandi", true);
        } else {
            return new ApiResult("Employee Turnicet Action uchun Employee topilmadi", false);
        }

    }

    /*
     UPDATE QILINGANDA CREATEDAT YA`NI YOZILGAN VAQTINI O`ZGARTIRILMADI.
     */
    @Override
    public ApiResult editEmpTurnAction(Integer id, EmpTurnActionReqDTO empTurnActionReqDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empTurnActionReqDTO.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            Optional<EmpTurnAction> optionalEmpTurnAction = empTurnActionRepository.findById(id);
            if (optionalEmpTurnAction.isPresent()) {
                EmpTurnAction empTurnAction = optionalEmpTurnAction.get();
                empTurnAction.setEmployee(employee);
                empTurnAction.setActionIncomeYokiOutCome(empTurnActionReqDTO.getActionIncomeYokiOutCome());
                empTurnActionRepository.save(empTurnAction);
                return new ApiResult("Employee Turnicet Action muvaffaqiyatli o`zgartirildi", true);
            } else {
                return new ApiResult("Bunday Employee Turnicet Action topilmadi", false);
            }
        } else {
            return new ApiResult("Bunday Employee topilmadi", false);
        }
    }

    @Override
    public ApiResult deleteEmpTurnAction(Integer id) {
        try {
            empTurnActionRepository.deleteById(id);
            return new ApiResult("Employee Turnicet Action o`chirildi", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult("Employee Turnicet Action o`chirilmadi", false);
        }
    }

    @Override
    public ApiResult getAllIncomeOrOutcomeEmpTurnActionByEmployee(EmpTurnActionReqDTO empTurnActionReqDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empTurnActionReqDTO.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            /*
            AGAR INCOMEOROUTCOME NULL BO`LSA EMPLOYEE ID BO`YICHA KIRISH VA CHIQISHLARNING BARCHASINI CHIQARADI
             */
            if (empTurnActionReqDTO.getActionIncomeYokiOutCome() == null) {
                List<EmpTurnActionResDTO> allByEmployeeId = empTurnActionRepository.findAllByEmployeeId(empTurnActionReqDTO.getEmployeeId())
                        .stream().map(this::castEmpTurnActionToEmpTurnActionResDto).collect(Collectors.toList());
                return new ApiResult(allByEmployeeId, true);
            } else {
                /*
                ELSE GA TUSHSA EMPLOYEE ID BO`YICHA KIRISH YOKI CHIQISHLARNING BARCHASINI CHIQARADI
                 */
                List<EmpTurnActionResDTO> allByEmployeeIdAndActionIncomeYokiOutCome =
                        empTurnActionRepository.findAllByEmployeeIdAndActionIncomeYokiOutCome(empTurnActionReqDTO.getEmployeeId(), empTurnActionReqDTO.getActionIncomeYokiOutCome())
                                .stream().map(this::castEmpTurnActionToEmpTurnActionResDto).collect(Collectors.toList());
                return new ApiResult(allByEmployeeIdAndActionIncomeYokiOutCome, true);
            }
        } else {
            return new ApiResult("Employee Turnicet Actionlar uchun Employee topilmadi", false);
        }
    }

    /*
    ISHGA VAQTIDA KELGANLAR VA ISHDAN ERTA KETGANLAR RO`YXATINI QAYTARUVCHI METOD
     */
    @Override
    public ResponseEntity<Resource> getEmployeesBeforeIncomeOrBeforeOutcome(DateAndTimeDto dateAndTimeDto) throws IOException, InvalidFormatException {
        Timestamp fromTimestamp = getFromTimestampOrUntilTimeStamp(dateAndTimeDto, AppConst.FIRST);
        Timestamp untilTimeStamp = getFromTimestampOrUntilTimeStamp(dateAndTimeDto, AppConst.MIDDLE);

        // BERILGAN VAQTDAN OLDIN TURNICETDAN KIRGAN XODIMLARNI DB DAN OLIB BERYAPTI
        List<EmpTurnAction> all = empTurnActionRepository.findAllByCreatedAtAfterAndCreatedAtBeforeAndActionIncomeYokiOutCome(fromTimestamp, untilTimeStamp, dateAndTimeDto.getIncomeOrOutcome());

        String filename;
        if (dateAndTimeDto.getIncomeOrOutcome()) {
            filename = "Ishga vaqtida kelganlar ro`yxati.xlsx";
        } else {
            filename = "Ishdan erta ketganlar ro`yxati.xlsx";
        }
        //DB DAN KELGAN XODIMLAR RO`YXATINI SHU METODGA BERILSA EXCELGA YOZIB EXCEL FAYLNING BYTEARRAYINPUTSTREAMINI QAYTARYAPTI
        ByteArrayInputStream inputStream = workTimeGenerateToExcel(all);
        InputStreamResource excelFile = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(excelFile);
    }

    /*
    ISHGA KECH KELGANLAR VA ISHDAN KECH KETGANLAR RO`YXATINI QAYTARUVCHI METOD
     */
    @Override
    public ResponseEntity<Resource> getEmployeesAfterIncomeOrAfterOutcome(DateAndTimeDto dateAndTimeDto) throws IOException, InvalidFormatException {

        Timestamp fromTimestamp = getFromTimestampOrUntilTimeStamp(dateAndTimeDto, AppConst.MIDDLE);
        Timestamp lastTimestamp = getFromTimestampOrUntilTimeStamp(dateAndTimeDto, AppConst.LAST);

        //BERILGAN VAQTDAN KEYIN TURNICETDAN KIRGAN YOKI TURNIKETDAN CHIQQAN XODIMLARNI YA`NI KECHIKKANLARNI VA KECH KETGANLARNI DB DAN OLIB KELYAPTI
        List<EmpTurnAction> all = empTurnActionRepository.findAllByCreatedAtAfterAndCreatedAtBeforeAndActionIncomeYokiOutCome(fromTimestamp, lastTimestamp, dateAndTimeDto.getIncomeOrOutcome());
        String filename;
        if (dateAndTimeDto.getIncomeOrOutcome()) {
            filename = "Ishga kech kelganlar ro`yxati.xlsx";
        } else {
            filename = "Ishdan kech ketganlar ro`yxati.xlsx";
        }
        //DB DAN KELGAN XODIMLAR RO`YXATINI SHU METODGA BERILSA EXCELGA YOZIB EXCEL FAYLNING BYTEARRAYINPUTSTREAMINI QAYTARYAPTI
        ByteArrayInputStream inputStream = workTimeGenerateToExcel(all);
        InputStreamResource excelFile = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(excelFile);
    }

    /*
    EMPTURNACTIONREQDTO DAN EMPTURNACTION GA CAST QILIB EMPTURNACTION QAYTARUVCHI METOD
    */
    public EmpTurnAction castEmpTurnActionReqDtoToEmpTurnAction(EmpTurnActionReqDTO empTurnActionReqDTO, Employee employee) {
        EmpTurnAction empTurnAction = new EmpTurnAction();
        empTurnAction.setActionIncomeYokiOutCome(empTurnActionReqDTO.getActionIncomeYokiOutCome());
        empTurnAction.setEmployee(employee);
        return empTurnAction;
    }

    /*
    EMPTURNECTIONDAN EMPTURNACTIONRESDTO GA CAST QILIB EMPTURNACTIONRESDTO QAYTARUVCHI METOD
     */
    public EmpTurnActionResDTO castEmpTurnActionToEmpTurnActionResDto(EmpTurnAction empTurnAction) {
        EmpTurnActionResDTO empTurnActionResDTO = new EmpTurnActionResDTO();
        empTurnActionResDTO.setId(empTurnAction.getId());
        empTurnActionResDTO.setActionIncomeYokiOutCome(empTurnAction.getActionIncomeYokiOutCome());
        empTurnActionResDTO.setEmployee(empTurnAction.getEmployee());
        empTurnActionResDTO.setCreatedAt(empTurnAction.getCreatedAt());
        return empTurnActionResDTO;
    }

    /*
    EMPLOYEELAR NING LISTI BERILSA SHU LISTDAGI MA`LUMOTLARNI EXCELGA GENERATSIYA QILIB, SHU EXCEL FAYLNING BYTEARRAYINPUTSTREAMINI QAYTARUVCHI METOD
     */
    public ByteArrayInputStream workTimeGenerateToExcel(List<EmpTurnAction> empTurnActions) throws IOException, InvalidFormatException {

        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("Sheet 1");


// By applying style for cells we can see the total text in the cell for specified width


        XSSFRow rowHead = sheet.createRow(0);
        rowHead.setHeight((short) 500);

        rowHead.createCell(0).setCellValue("№");
        rowHead.createCell(1).setCellValue("Ismi");
        rowHead.createCell(2).setCellValue("Familiyasi");
        rowHead.createCell(3).setCellValue("Telefon raqami");
        rowHead.createCell(4).setCellValue("Time");
        rowHead.createCell(5).setCellValue("Keldi-ketdi");

        short rowCount = 1;
        int count = 1;
        for (EmpTurnAction empTurnAction : empTurnActions) {
            XSSFRow rowEmployee = sheet.createRow(rowCount++);
            for (int i = 0; i < 5; i++) {
                XSSFCellStyle cellStyle = excel.createCellStyle();
                rowEmployee.createCell(i).setCellStyle(cellStyle);
                cellStyle.setWrapText(true);
            }
            rowEmployee.createCell(0).setCellValue(count++);
            rowEmployee.createCell(1).setCellValue(empTurnAction.getEmployee().getFirstname());
            rowEmployee.createCell(2).setCellValue(empTurnAction.getEmployee().getLastname());
            rowEmployee.createCell(3).setCellValue(empTurnAction.getEmployee().getPhoneNumber());
            rowEmployee.createCell(4).setCellValue(empTurnAction.getCreatedAt().toString());

            if (empTurnAction.getActionIncomeYokiOutCome()) {
                rowEmployee.createCell(5).setCellValue("KELGAN");
            } else {
                rowEmployee.createCell(5).setCellValue("KETGAN");
            }
        }
        for (int i=0; i<6; i++){
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        excel.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    /*
    TIMESTAMP QAYTARUVCHI METOD FIRST BERILSA FROMDATETIME (dan masalan: 2022-02-18 00:00:00 bu erta kelganlar uchun),
    MIDDLE BO`LSA UNTILDATETIME (gacha masalan: 2022-02-18 09:00:01 bu yerda erta kelganlarning vaqtini hisoblash uchun),
    LAST BERILSA FROMDATETIME VA UNTILDATE TIME BIROZ O`ZGARADI. (masalan: FROMDATETIME 2022-02-18 12:00:00 dan UNTILDATETIME 2022-02-18 23:59:59 gacha)
     */
    public Timestamp getFromTimestampOrUntilTimeStamp(DateAndTimeDto dateAndTimeDto, String action) {

        int year = dateAndTimeDto.getLocalDateTime().getYear();
        int day = dateAndTimeDto.getLocalDateTime().getDayOfMonth();
        int month = dateAndTimeDto.getLocalDateTime().getMonthValue() - 1;
        int hour = dateAndTimeDto.getLocalDateTime().getHour();
        int minute = dateAndTimeDto.getLocalDateTime().getMinute();
        int second = dateAndTimeDto.getLocalDateTime().getSecond();

        if (action.equals(AppConst.FIRST)) {
            GregorianCalendar fromDateTime = new GregorianCalendar(year, month, day, 0, 0, 0);
            long fromDateTimeSecond = fromDateTime.getTimeInMillis();
            return new Timestamp(fromDateTimeSecond);
        } else if (action.equals(AppConst.MIDDLE)) {
            GregorianCalendar untilDateTime = new GregorianCalendar(year, month, day, hour, minute, second);
            long untilDateTimeSecond = untilDateTime.getTimeInMillis();
            return new Timestamp(untilDateTimeSecond);
        } else if (action.equals(AppConst.LAST)) {
            GregorianCalendar fromDateTime = new GregorianCalendar(year, month, day, 23, 59, 59);
            long fromDateTimeSecond = fromDateTime.getTimeInMillis();
            return new Timestamp(fromDateTimeSecond);
        } else {
            return null;
        }
    }
}
