package com.tasc.project.salary.service;

import com.tasc.model.ApplicationException;
import com.tasc.model.BaseResponseV2;
import com.tasc.model.ERROR;
import com.tasc.model.dto.AttendanceDTO;
import com.tasc.model.dto.employee.EmployeeDTO;
import com.tasc.project.salary.connector.AttendanceConnector;
import com.tasc.project.salary.connector.EmployeeConnector;
import com.tasc.project.salary.repository.SalaryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Log4j2
public class SalaryService {
    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    EmployeeConnector employeeConnector;

    @Autowired
    AttendanceConnector attendanceConnector;

    public BaseResponseV2<BigDecimal> calculateSalary(long employeeId, LocalDate startDate, LocalDate endDate) throws ApplicationException {

        // check employee is existed or not
        BaseResponseV2<EmployeeDTO> employeeResponseInfo = employeeConnector.findById(employeeId);

        if (!employeeResponseInfo.isSuccess()) {
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        EmployeeDTO employeeDTO = employeeResponseInfo.getData();

        if (employeeDTO == null) {
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        // check list attendance by employee id and date range
        BaseResponseV2<List<AttendanceDTO>> listAttendanceResponseInfo = attendanceConnector.findByEmployeeAndDateRange(employeeId, startDate, endDate);

        if (!listAttendanceResponseInfo.isSuccess()) {
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        List<AttendanceDTO> attendanceDTOList = listAttendanceResponseInfo.getData();

        if (attendanceDTOList == null) {
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        BigDecimal totalMonths = BigDecimal.ZERO;

//        BigDecimal totalHours = BigDecimal.ZERO;
//
//        for (AttendanceDTO attendanceDTO : attendanceDTOList) {
//            Duration duration = Duration.between(attendanceDTO.getCheckIn(), attendanceDTO.getCheckOut());
//            totalHours = totalHours.add(new BigDecimal(duration.toHours()));
//        }

        Period period = Period.between(startDate, endDate);
        totalMonths = totalMonths.add(new BigDecimal(period.getMonths() + period.getYears() * 12));

        BigDecimal monthlyRate = employeeDTO.getMontlyRate();

        log.info("Hourly rate : {}", monthlyRate);
        log.info("Total hours : {}", totalMonths);

        return new BaseResponseV2<>(totalMonths.multiply(monthlyRate));


    }

}
