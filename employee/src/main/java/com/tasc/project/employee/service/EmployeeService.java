package com.tasc.project.employee.service;

import com.tasc.entity.BaseStatus;
import com.tasc.model.ApplicationException;
import com.tasc.model.BaseResponseV2;
import com.tasc.model.ERROR;
import com.tasc.model.dto.employee.EmployeeDTO;
import com.tasc.project.employee.connector.UserConnector;
import com.tasc.project.employee.entity.Department;
import com.tasc.project.employee.entity.Employee;
import com.tasc.project.employee.model.request.CreateEmployeeRequest;
import com.tasc.project.employee.repository.DepartmentRepository;
import com.tasc.project.employee.repository.EmployeeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserConnector userConnector;
    @Autowired
    private DepartmentRepository departmentRepository;

    public BaseResponseV2<EmployeeDTO> findById(long id) throws ApplicationException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Not found employee with ID: " + id);
        }

        Employee employee = optionalEmployee.get();

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .gender(employee.getGender())
                .dob(employee.getDob())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .salary(employee.getSalary())
                .description(employee.getDescription())
                .detail(employee.getDetail())
                .status(employee.getStatus())
                .userId(employee.getUserId())
                .montlyRate(employee.getMonthlyRate())
                .departmentId(employee.getDepartment().getId())
                .build();

        return new BaseResponseV2<>(employeeDTO);
    }

    public BaseResponseV2<List<EmployeeDTO>> findByFullName(String fullName) throws ApplicationException {
        List<Employee> employeeList = employeeRepository.findEmployeesByFullNameContainingIgnoreCase(fullName);

        if (employeeList.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Not found employee with full name: " + fullName);
        }

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        for (Employee employee : employeeList) {
            EmployeeDTO employeeDTO = EmployeeDTO.builder()
                    .id(employee.getId())
                    .fullName(employee.getFullName())
                    .gender(employee.getGender())
                    .dob(employee.getDob())
                    .email(employee.getEmail())
                    .phone(employee.getPhone())
                    .salary(employee.getSalary())
                    .description(employee.getDescription())
                    .detail(employee.getDetail())
                    .status(employee.getStatus())
                    .userId(employee.getUserId())
                    .montlyRate(employee.getMonthlyRate())
                    .departmentId(employee.getDepartment().getId())
                    .build();

            employeeDTOList.add(employeeDTO);
        }

        return new BaseResponseV2<List<EmployeeDTO>>(employeeDTOList);
    }

    public BaseResponseV2<Page<Employee>> findAll(BaseStatus status, int page, int pageSize) throws ApplicationException {
        if (page <= 0) {
            page = 1;
        }

        if (pageSize <= 0) {
            pageSize = 10;
        }

        Page<Employee> employeeList = employeeRepository.findEmployeesByStatus(status, PageRequest.of(page - 1, pageSize));

        return new BaseResponseV2<>(employeeList);
    }

    public BaseResponseV2<EmployeeDTO> addUser(long employeeId, long userId) throws ApplicationException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Not found employee with id: " + employeeId);
        }

        Employee employee = optionalEmployee.get();
        employee.setUserId(userId);

        employeeRepository.save(employee);

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .gender(employee.getGender())
                .dob(employee.getDob())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .salary(employee.getSalary())
                .description(employee.getDescription())
                .detail(employee.getDetail())
                .status(employee.getStatus())
                .userId(userId)
                .montlyRate(employee.getMonthlyRate())
                .departmentId(employee.getDepartment().getId())
                .build();

        return new BaseResponseV2<>(employeeDTO);
    }

    public BaseResponseV2<Employee> createdEmployee(CreateEmployeeRequest request) throws ApplicationException {
        invalidationEmployeeRequest(request);

        Optional<Department> optionalDepartment = departmentRepository.findById(request.getDepartmentId());

        if (optionalDepartment.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Not found department with ID : " + request.getDepartmentId());
        }

        Department department = optionalDepartment.get();

        Employee employee = Employee.builder()
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dob(request.getDob())
                .email(request.getEmail())
                .phone(request.getPhone())
                .description(request.getDescription())
                .detail(request.getDetail())
                .monthlyRate(request.getMonthlyRate())
                .department(department)
                .build();
        employee.setStatus(BaseStatus.ACTIVE);

        employeeRepository.save(employee);

        return new BaseResponseV2<>(employee);
    }

    public BaseResponseV2<EmployeeDTO> updateEmployee(long id, CreateEmployeeRequest request) throws ApplicationException {
        invalidationEmployeeRequest(request);

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Not found employee with ID : " + id);
        }

        Employee existedEmployee = optionalEmployee.get();

        Optional<Department> optionalDepartment = departmentRepository.findById(request.getDepartmentId());

        if (optionalDepartment.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Not found department with ID : " + request.getDepartmentId());
        }

        Department department = optionalDepartment.get();

        existedEmployee.setFullName(request.getFullName());
        existedEmployee.setGender(request.getGender());
        existedEmployee.setDob(request.getDob());
        existedEmployee.setEmail(request.getEmail());
        existedEmployee.setPhone(request.getPhone());
        existedEmployee.setDescription(request.getDescription());
        existedEmployee.setDetail(request.getDetail());
        existedEmployee.setMonthlyRate(request.getMonthlyRate());
        existedEmployee.setDepartment(department);

        employeeRepository.save(existedEmployee);

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dob(request.getDob())
                .email(request.getEmail())
                .phone(request.getPhone())
                .description(request.getDescription())
                .detail(request.getDetail())
                .montlyRate(request.getMonthlyRate())
                .departmentId(request.getDepartmentId())
                .build();

        return new BaseResponseV2<>(employeeDTO);
    }

    private void invalidationEmployeeRequest(CreateEmployeeRequest request) throws ApplicationException {
        if (StringUtils.isBlank(request.getFullName())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Full name is empty");
        }

        if (StringUtils.isBlank(request.getGender())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Gender is empty");
        }

        if (StringUtils.isBlank(request.getDob().toString())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Date of birth is empty");
        }

        if (StringUtils.isBlank(request.getEmail())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Email is empty");
        }

        if (StringUtils.isBlank(request.getPhone())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Phone is empty");
        }

        if (StringUtils.isBlank(request.getDescription())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Description is empty");
        }

        if (StringUtils.isBlank(request.getDetail())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Detail is empty");
        }

        if (StringUtils.isBlank(String.valueOf(request.getDepartmentId()))) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Department is empty");
        }
    }
}
