// frontend/src/services/api/employeeApi.js
import axiosInstance from '../utils/axiosConfig';

const EMPLOYEE_API = '/api/employees';

export const employeeApi = {
  getEmployees: (branchId) =>
    axiosInstance.get(EMPLOYEE_API, { params: { branchId } }),

  getEmployeeById: (employeeId) =>
    axiosInstance.get(`${EMPLOYEE_API}/${employeeId}`),

  createEmployee: (employeeData) =>
    axiosInstance.post(EMPLOYEE_API, employeeData),

  updateEmployee: (employeeId, employeeData) =>
    axiosInstance.put(`${EMPLOYEE_API}/${employeeId}`, employeeData),

  deleteEmployee: (employeeId) =>
    axiosInstance.delete(`${EMPLOYEE_API}/${employeeId}`),

  getEmployeeStats: (employeeId) =>
    axiosInstance.get(`${EMPLOYEE_API}/${employeeId}/stats`),
};
